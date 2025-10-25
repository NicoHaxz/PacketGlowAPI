package nico.dev.packetGlowAPI;

import nico.dev.packetGlowAPI.API.GlowAPI;
import nico.dev.packetGlowAPI.Managers.GlowManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {
    public static main plugin;
    private static main instance;
    private GlowManager glowManager;

    @Override
    public void onEnable() {
        instance = this;
        glowManager = new GlowManager();
        getLogger().info("SpectralGlowAPI habilitada correctamente!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SpectralGlowAPI deshabilitada.");
    }

    public GlowManager getGlowManager() {
        return glowManager;
    }
    public static main getInstance() {
        return instance;
    }

    public static main getPlugin() {
        return main.plugin;
    }
}
