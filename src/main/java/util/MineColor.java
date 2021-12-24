package util;


public enum MineColor {
    DARK_BLUE("§1"),
    GREEN("§2"),
    CYAN("§3"),
    DARK_RED("§4"),
    PURPLE("§5"),
    ORANGE("§6"),
    LIGHT_GRAY("§7"),
    GRAY("§8"),
    BLUE("§9"),
    BLACK("§0"),
    LIME("§a"),
    AQUA("§b"),
    RED("§c"),
    PINK("§d"),
    YELLOW("§e"),
    WHITE("§f"),
    RESET("§r");

    public final MineColor BOLD() {
        return this.addFormat("§l");
    }
    public final MineColor ITALIC() {
        return this.addFormat("§o");
    }
    public final MineColor UNDERLINE() {
        return this.addFormat("§n");
    }
    public final MineColor STRIKETHROUGH() {
        return this.addFormat("§m");
    }
    public final MineColor MAGIC() {
        return this.addFormat("§k");
    }

    private String string;

    MineColor(String string){
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

    private MineColor addFormat(String format){
        string += format;
        return this;
    }
}
