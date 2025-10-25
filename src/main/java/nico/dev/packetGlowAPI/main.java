package nico.dev.packetGlowAPI;

import nico.dev.packetGlowAPI.API.GlowAPI;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {
    public static main plugin;
    private static main instance;
    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("PacketGlowAPI habilitada correctamente!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PacketGlowAPI deshabilitada.");
    }

    public static main getInstance() {
        return instance;
    }

    public static main getPlugin() {
        return main.plugin;
    }
}
