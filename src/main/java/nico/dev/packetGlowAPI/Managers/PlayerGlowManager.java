package nico.dev.packetGlowAPI.Managers;

import nico.dev.packetGlowAPI.Context.GlowContext;
import nico.dev.packetGlowAPI.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerGlowManager {
        private static final String GLOW_PREFIX = "TTG";
        private static final int HASH_LENGTH = 8;

        private static final Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        private static final Map<UUID, GlowContext> activePlayerGlows = new HashMap<>();
        private static final Map<UUID, Scoreboard> mirroredBoards = new HashMap<>();


        public static void applyPlayerGlow(Player player, ChatColor color, int ticks) {
            UUID uuid = player.getUniqueId();
            cancelPlayerGlow(uuid);

            String glowTeamName = buildGlowTeamName(uuid);
            configureTeam(glowTeamName, team -> team.setColor(color));
            addEntryToTeam(glowTeamName, player.getName());

            player.setGlowing(true);
            ensureBoardRegistration(player);

            GlowContext context = new GlowContext(player.getName(), glowTeamName, color);
            activePlayerGlows.put(uuid, context);

            if (ticks > 0) {
                int taskId = Bukkit.getScheduler().runTaskLater(main.getInstance(),
                        () -> finishPlayerGlow(player.getUniqueId()), ticks).getTaskId();
                context.setTaskId(taskId);
            }
        }

        private static void finishPlayerGlow(UUID uuid) {
            GlowContext context = activePlayerGlows.remove(uuid);
            if (context == null) return;

            removeEntryFromTeam(context.getTeamName(), context.getEntry());
            cleanupTeam(context.getTeamName());

            Player player = Bukkit.getPlayer(uuid);
            if (player != null) player.setGlowing(false);
        }

        private static void cancelPlayerGlow(UUID uuid) {
            GlowContext context = activePlayerGlows.get(uuid);
            if (context == null) return;

            if (context.getTaskId() >= 0) {
                Bukkit.getScheduler().cancelTask(context.getTaskId());
            }

            finishPlayerGlow(uuid);
        }


        private static void configureTeam(String teamName, java.util.function.Consumer<Team> consumer) {
            Team team = mainScoreboard.getTeam(teamName);
            if (team == null) team = mainScoreboard.registerNewTeam(teamName);
            consumer.accept(team);
        }

        private static void addEntryToTeam(String teamName, String entry) {
            Team team = mainScoreboard.getTeam(teamName);
            if (team == null) team = mainScoreboard.registerNewTeam(teamName);
            if (!team.hasEntry(entry)) team.addEntry(entry);
        }

        private static void removeEntryFromTeam(String teamName, String entry) {
            Team team = mainScoreboard.getTeam(teamName);
            if (team != null) team.removeEntry(entry);
        }

        private static void cleanupTeam(String teamName) {
            Team team = mainScoreboard.getTeam(teamName);
            if (team != null && team.getEntries().isEmpty()) {
                try {
                    team.unregister();
                } catch (IllegalStateException ignored) {
                }
            }
        }

        private static void ensureBoardRegistration(Player player) {
            Scoreboard current = player.getScoreboard();
            if (current == null || current == mainScoreboard) return;
            mirroredBoards.put(player.getUniqueId(), current);
        }

        private static String buildGlowTeamName(UUID uuid) {
            String id = uuid.toString().replace("-", "");
            if (id.length() > HASH_LENGTH) id = id.substring(0, HASH_LENGTH);
            return GLOW_PREFIX + "_" + id;
        }
}
