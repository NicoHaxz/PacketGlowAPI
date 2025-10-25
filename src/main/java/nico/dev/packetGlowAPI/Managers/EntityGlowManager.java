package nico.dev.packetGlowAPI.Managers;

import nico.dev.packetGlowAPI.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityGlowManager {
    private static final Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    private static final int HASH_LENGTH = 8;
    private static final Map<UUID, EntityGlowContext> entityGlows = new HashMap<>();

    public static void applyEntityGlow(Entity entity, ChatColor color, int ticks) {
        cancelEntityGlow(entity, false);

        String teamName = buildEntityGlowTeamName(entity.getUniqueId(), color);
        Team glowTeam = mainScoreboard.getTeam(teamName);
        if (glowTeam == null) glowTeam = mainScoreboard.registerNewTeam(teamName);

        glowTeam.setColor(color);
        glowTeam.addEntity(entity);
        entity.setGlowing(true);

        int taskId = -1;
        if (ticks > 0) {
            UUID uuid = entity.getUniqueId();
            String trackedTeam = teamName;
            taskId = Bukkit.getScheduler().runTaskLater(main.getInstance(), () -> {
                Entity target = Bukkit.getEntity(uuid);
                if (target != null) cancelEntityGlow(target, true);
                else {
                    cleanupTeam(mainScoreboard.getTeam(trackedTeam));
                    entityGlows.remove(uuid);
                }
            }, ticks).getTaskId();
        }

        entityGlows.put(entity.getUniqueId(), new EntityGlowContext(teamName, taskId));
    }

    private static void cancelEntityGlow(Entity entity, boolean clearGlowingState) {
        UUID uuid = entity.getUniqueId();
        EntityGlowContext context = entityGlows.remove(uuid);
        if (context == null) {
            if (clearGlowingState) entity.setGlowing(false);
            return;
        }

        if (context.taskId >= 0) Bukkit.getScheduler().cancelTask(context.taskId);

        Team glowTeam = mainScoreboard.getTeam(context.teamName);
        if (glowTeam != null) {
            glowTeam.removeEntity(entity);
            cleanupTeam(glowTeam);
        }

        if (clearGlowingState) entity.setGlowing(false);
    }

    private static void cleanupTeam(Team team) {
        if (team != null && team.getEntries().isEmpty()) {
            try { team.unregister(); } catch (IllegalStateException ignored) {}
        }
    }

    private static String buildEntityGlowTeamName(UUID uuid, ChatColor color) {
        String base = uuid.toString().replace("-", "").toLowerCase();
        if (base.length() > HASH_LENGTH) base = base.substring(0, HASH_LENGTH);
        return "TTGE_" + color.ordinal() + "_" + base;
    }

    private record EntityGlowContext(String teamName, int taskId) {}
}
