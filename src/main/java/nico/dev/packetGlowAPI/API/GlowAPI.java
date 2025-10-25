package nico.dev.packetGlowAPI.API;

import nico.dev.packetGlowAPI.Managers.EntityGlowManager;
import nico.dev.packetGlowAPI.Managers.PlayerGlowManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class GlowAPI {
    public static void setCustomPlayerGlow(Player player, ChatColor color, int ticks) {
        PlayerGlowManager.applyPlayerGlow(player, color, ticks);
    }

    public static void setCustomEntityGlow(Entity entity, ChatColor color, int ticks) {
        EntityGlowManager.applyEntityGlow(entity, color, ticks);
    }

    public static void setCustomEntityGlow(Entity entity, ChatColor color) {
        EntityGlowManager.applyEntityGlow(entity, color, -1);
    }
}