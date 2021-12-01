package game;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.List;

public class UpgradeInfo {
    private final List<LevelInfo> levels;
    private final List<Material> types;
    private final Enchantment enchantment;
    private final String name;

    public UpgradeInfo(String name, List<LevelInfo> levels, Enchantment enchantment, List<Material> types) {
        this.levels = levels;
        this.types = types;
        this.enchantment = enchantment;
        this.name = name;
    }

    public UpgradeInfo(String name, List<LevelInfo> levels){
        this.levels = levels;
        this.types = null;
        this.enchantment = null;
        this.name = name;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public List<LevelInfo> getLevels() {
        return levels;
    }

    public List<Material> getTypes() {
        return types;
    }

    public String getName() {
        return name;
    }
}
