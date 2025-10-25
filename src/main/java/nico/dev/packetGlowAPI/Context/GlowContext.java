package nico.dev.packetGlowAPI.Context;


import org.bukkit.ChatColor;

public class GlowContext {
    private final String entry;
    private final String teamName;
    private final ChatColor color;
    private int taskId = -1;

    public GlowContext(String entry, String teamName, ChatColor color) {
        this.entry = entry;
        this.teamName = teamName;
        this.color = color;
    }

    public String getEntry() { return entry; }
    public String getTeamName() { return teamName; }
    public ChatColor getColor() { return color; }
    public int getTaskId() { return taskId; }
    public void setTaskId(int taskId) { this.taskId = taskId; }
}
