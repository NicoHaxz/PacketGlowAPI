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

    /**
     * Construye y envía un paquete SCOREBOARD_TEAM a un viewer.
     */
    private void sendTeamPacket(Player viewer, TeamData team, int mode, Collection<String> players) {
        try {
            PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);
            packet.getStrings().write(0, team.name); // team name
            packet.getStrings().write(1, team.name); // display name

            // Evitamos campos problemáticos (prefix/suffix) -> nulls
            packet.getChatComponents().writeSafely(0, null);
            packet.getChatComponents().writeSafely(1, null);

            packet.getIntegers().writeSafely(0, 0); // friendlyFlags (legacy)
            packet.getIntegers().writeSafely(1, team.color.value); // color
            packet.getIntegers().writeSafely(2, mode); // mode

            if (players == null) players = Collections.emptyList();
            // Usamos getSpecificModifier(Collection.class) para compatibilidad 1.21.4
            packet.getSpecificModifier(Collection.class).write(0, new ArrayList<>(players));

            protocol.sendServerPacket(viewer, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Envía metadata de glowing a un viewer específico.
     */
    private void sendGlowingMetadata(Entity entity, Player viewer, boolean glowing) {
        try {
            PacketContainer meta = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
            meta.getIntegers().write(0, entity.getEntityId());

            // bitmask 0x40 = glowing
            byte flags = (byte) (glowing ? 0x40 : 0x00);

            WrappedWatchableObject wwo = new WrappedWatchableObject(0, flags);
            List<WrappedWatchableObject> list = Collections.singletonList(wwo);

            meta.getWatchableCollectionModifier().write(0, list);
            protocol.sendServerPacket(viewer, meta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hace glow de 'entity' visible para 'viewers' en color 'color'.
     * Si viewers == null -> envía a todos online.
     */
    public void setGlowing(Entity entity, Collection<Player> viewers, GlowColor color, boolean enable) {
        Collection<Player> target = (viewers == null)
                ? new ArrayList<>(Bukkit.getOnlinePlayers())
                : viewers;

        String entry = entity.getUniqueId().toString();

        if (enable) {
            TeamData t = getOrCreateTeam(color);
            t.members.add(entity.getUniqueId());

            for (Player v : target) {
                // Enviamos CREATE (mode 0) por seguridad
                sendTeamPacket(v, t, 0, Collections.singletonList(entry));
                sendGlowingMetadata(entity, v, true);
            }
        } else {
            for (Player v : target) {
                for (TeamData t : teams.values()) {
                    sendTeamPacket(v, t, 4, Collections.singletonList(entry)); // mode 4 = remove players
                }
                sendGlowingMetadata(entity, v, false);
            }
        }
    }

    // Wrappers convenientes
    public void setGlowingForAll(Entity entity, GlowColor color, boolean enable) {
        setGlowing(entity, null, color, enable);
    }

    public void setGlowingForPlayer(Entity entity, Player viewer, GlowColor color, boolean enable) {
        setGlowing(entity, Collections.singleton(viewer), color, enable);
    }

    /**
     * Limpieza del cache interno (llamar cuando muere una entidad o se descarga).
     */
    public void removeEntityFromCache(UUID uuid) {
        for (Iterator<TeamData> it = teams.values().iterator(); it.hasNext(); ) {
            TeamData t = it.next();
            t.members.remove(uuid);
            if (t.members.isEmpty()) it.remove();
        }
    }
}
