package nico.dev.packetGlowAPI.Colors;

public enum GlowColor {
    WHITE(0),
    BLACK(15),
    DARK_BLUE(1),
    DARK_GREEN(2),
    DARK_AQUA(3),
    DARK_RED(4),
    DARK_PURPLE(5),
    GOLD(6),
    GRAY(7),
    DARK_GRAY(8),
    BLUE(9),
    GREEN(10),
    AQUA(11),
    RED(12),
    LIGHT_PURPLE(13),
    YELLOW(14);

        public final int value;
        GlowColor(int value) { this.value = value; }
}
