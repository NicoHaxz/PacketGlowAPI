package nico.dev.packetGlowAPI.API;

import nico.dev.packetGlowAPI.Colors.GlowColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public interface IGlowAPI {
    void setGlowing(Entity entity, Collection<Player> viewers, GlowColor color, boolean enable);
    void setGlowingForAll(Entity entity, GlowColor color, boolean enable);
    void setGlowingForPlayer(Entity entity, Player viewer, GlowColor color, boolean enable);
    void removeEntityFromCache(UUID uuid);
}
