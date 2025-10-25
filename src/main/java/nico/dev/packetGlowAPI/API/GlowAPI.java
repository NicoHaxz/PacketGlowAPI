package nico.dev.packetGlowAPI.API;

import nico.dev.packetGlowAPI.Colors.GlowColor;
import nico.dev.packetGlowAPI.Managers.GlowManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class GlowAPI {
    private static GlowManager manager;

    private GlowAPI() {}

    public static void initialize() {
        if (manager == null) manager = new GlowManager();
    }

    public static void shutdown() {
        manager = null;
    }

    private static void requireInit() {
        if (manager == null) throw new IllegalStateException("GlowAPI no inicializada. Llama a GlowAPI.initialize() desde tu plugin.");
    }

    public static void setGlowing(Entity entity, Player viewer, GlowColor color, boolean enable) {
        requireInit();
        if (viewer == null) manager.setGlowingForAll(entity, color, enable);
        else manager.setGlowingForPlayer(entity, viewer, color, enable);
    }

    public static void setGlowing(Entity entity, java.util.Collection<Player> viewers, GlowColor color, boolean enable) {
        requireInit();
        manager.setGlowing(entity, viewers, color, enable);
    }

    public static void setGlowingForAll(Entity entity, GlowColor color, boolean enable) {
        requireInit();
        manager.setGlowingForAll(entity, color, enable);
    }
}