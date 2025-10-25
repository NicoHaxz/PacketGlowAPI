package nico.dev.packetGlowAPI.Managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import nico.dev.packetGlowAPI.Colors.GlowColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase interna que maneja la lógica de envío de paquetes (teams + metadata)
 * para que entidades brillen con color para viewers específicos.
 *
 * Notas:
 * - Usa equipos 'fake' por color. En vanilla el color del outline depende del equipo en el cliente.
 * - Para que solo ciertos jugadores lo vean, enviamos los paquetes únicamente a esos viewers.
 */
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

    private void sendTeamPacket(Player viewer, TeamData team, int mode, Collection<String> players) {
        try {
            PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);
            packet.getStrings().write(0, team.name);
            packet.getStrings().write(1, team.name);
            packet.getChatComponents().writeSafely(0, null);
            packet.getChatComponents().writeSafely(1, null);
            packet.getIntegers().writeSafely(0, 0);
            packet.getIntegers().writeSafely(1, team.color.value);
            packet.getIntegers().writeSafely(2, mode);

            if (players == null) players = Collections.emptyList();
            packet.getSpecificModifier(Collection.class).write(0, new ArrayList<>(players));

            protocol.sendServerPacket(viewer, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendGlowingMetadata(Entity entity, Player viewer, GlowColor color, boolean glowing) {
        try {
            WrappedDataWatcher watcher = WrappedDataWatcher.getEntityWatcher(entity);

            WrappedDataWatcher.WrappedDataWatcherObject flags = watcher.getWatchableObject(0).getWatcherObject();
            byte original = watcher.getByte(flags.getIndex());
            byte newFlags = (byte) (glowing ? (original | 0x40) : (original & ~0x40));
            watcher.setObject(flags, newFlags);

            WrappedDataWatcher.WrappedDataWatcherObject colorObj = watcher.getWatchableObject(1).getWatcherObject();
            watcher.setObject(colorObj, (byte) color.value);

            PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
            packet.getIntegers().write(0, entity.getEntityId());
            packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

            protocol.sendServerPacket(viewer, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setGlowing(Entity entity, Collection<Player> viewers, GlowColor color, boolean enable) {
        Collection<Player> target = (viewers == null)
                ? new ArrayList<>(Bukkit.getOnlinePlayers())
                : viewers;

        String entry = entity.getUniqueId().toString();

        if (enable) {
            TeamData t = getOrCreateTeam(color);
            t.members.add(entity.getUniqueId());

            for (Player v : target) {
                sendTeamPacket(v, t, 0, Collections.singletonList(entry));
                sendGlowingMetadata(entity, v, color, true);
            }
        } else {
            for (Player v : target) {
                for (TeamData t : teams.values()) {
                    sendTeamPacket(v, t, 4, Collections.singletonList(entry));
                }
                sendGlowingMetadata(entity, v, color, false);
            }
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