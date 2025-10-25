package nico.dev.packetGlowAPI.Managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import nico.dev.packetGlowAPI.Colors.GlowColor;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class GlowManager {
    private final ProtocolManager protocol;
    private final Map<GlowColor, TeamData> teams = new EnumMap<>(GlowColor.class);
    private final AtomicInteger teamCounter = new AtomicInteger(0);

    public GlowManager() {
        this.protocol = ProtocolLibrary.getProtocolManager();
    }

    private static final class TeamData {
        final String name;
        final GlowColor color;
        final Set<UUID> members = new HashSet<>();

        TeamData(String name, GlowColor color) {
            this.name = name;
            this.color = color;
        }
    }

    private TeamData getOrCreateTeam(GlowColor color) {
        return teams.computeIfAbsent(color, c ->
                new TeamData("sg_team_" + c.name() + "_" + teamCounter.getAndIncrement(), c));
    }


    private void sendGlowFlag(Entity entity, Player viewer, GlowColor color, boolean glowing) {
        try {
            PacketContainer packet = new PacketContainer(PacketContainer.Type.Play.Server.ENTITY_METADATA);
            packet.getIntegers().write(0, entity.getEntityId());

            WrappedDataWatcher watcher = WrappedDataWatcher.getEntityWatcher(entity);

            WrappedDataWatcher.WrappedDataWatcherObject flagObj = watcher.getWatchableObject(0).getWatcherObject();
            byte originalFlag = watcher.getByte(flagObj.getIndex());
            byte newFlag = (byte) (glowing ? (originalFlag | 0x40) : (originalFlag & ~0x40));
            watcher.setObject(flagObj, newFlag);

            WrappedDataWatcher.WrappedDataWatcherObject colorObj = watcher.getWatchableObject(1).getWatcherObject();
            watcher.setObject(colorObj, (byte) color.value);

            packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
            protocol.sendServerPacket(viewer, packet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Envía el glow a un jugador, usando un equipo invisible solo para manejar el color.
     */
    private void sendTeamPacket(Player viewer, TeamData team, int mode, Collection<String> entries) {
        try {
            PacketContainer packet = new PacketContainer(PacketContainer.fromPacket(PacketType.Play.Server.SCOREBOARD_TEAM).getType());

            packet.getStrings().write(0, team.name); // nombre del team
            packet.getStrings().write(1, team.name); // displayName
            packet.getChatComponents().writeSafely(0, null);
            packet.getChatComponents().writeSafely(1, null);

            packet.getIntegers().writeSafely(0, 0); // friendlyFlags
            packet.getIntegers().writeSafely(1, team.color.value); // color
            packet.getIntegers().writeSafely(2, mode); // 0=create,4=remove

            // Nunca mostrar en scoreboard/tab
            packet.getStrings().write(2, "never"); // nameTagVisibility
            packet.getStrings().write(3, "never"); // collisionRule

            if (entries == null) entries = Collections.emptyList();
            packet.getSpecificModifier(Collection.class).write(0, new ArrayList<>(entries));

            protocol.sendServerPacket(viewer, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Glow principal para una colección de jugadores
     */
    public void setGlowing(Entity entity, Collection<Player> viewers, GlowColor color, boolean enable) {
        Collection<Player> target = viewers == null ? new ArrayList<>(Bukkit.getOnlinePlayers()) : viewers;
        String entry = entity.getUniqueId().toString();

        for (Player v : target) {
            TeamData team = getOrCreateTeam(color);
            if (enable) team.members.add(entity.getUniqueId());

            // Enviar el equipo invisible para manejar color
            sendTeamPacket(v, team, enable ? 0 : 4, Collections.singletonList(entry));

            // Enviar el flag de glowing a todos
            sendGlowFlag(entity, v, color, enable);
        }
    }

    public void setGlowingForAll(Entity entity, GlowColor color, boolean enable) {
        setGlowing(entity, null, color, enable);
    }

    public void setGlowingForPlayer(Entity entity, Player viewer, GlowColor color, boolean enable) {
        setGlowing(entity, Collections.singleton(viewer), color, enable);
    }

    public void removeEntityFromCache(UUID uuid) {
        for (Iterator<TeamData> it = teams.values().iterator(); it.hasNext(); ) {
            TeamData t = it.next();
            t.members.remove(uuid);
            if (t.members.isEmpty()) it.remove();
        }
    }
}