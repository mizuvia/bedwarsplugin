package inventories;

import main.Plugin;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;

public class MenuInventory extends SimpleInventory {

    public MenuInventory(InventoryHolder owner, int size, String title, Plugin plugin) {
        super(owner, size, title);
        this.plugin = plugin;
        this.setMaxStackSize(1);
        this.addItems();
    }

    private void addItems() {
        this.addItem(this.createItem(1, Material.WHITE_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(this.createItem(7, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(this.createItem(1, Material.WHITE_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(this.createItem(1, Material.GRAY_STAINED_GLASS_PANE, 1, false,  " ", ""));
        this.addItem(this.createItem(1, Material.CRAFTING_TABLE, 1, true, "BuildBattle", ""));
        this.addItem(this.createItem(1, Material.COOKIE, 1 , true, "Creative", "§c§lНЕ ЗАХОДИТЬ!!!"));
        this.addItem(this.createItem(1, Material.DRAGON_EGG, 1,true, "EggWars", ""));
        this.addItem(this.createItem(1, Material.RED_BED, 1, true, "BedWars", ""));
        this.addItem(this.createItem(1, Material.ENDER_PEARL, 1,true, "SkyWars", ""));
        this.addItem(this.createItem(4, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", "" ));
        this.addItem(this.createItem(1, Material.OAK_WOOD, 1, true, "Survival", "§c§lНЕ ЗАХОДИТЬ!!!"));
        this.addItem(this.createItem(1, Material.DROPPER, 1, true, "Hide'n'Seek", ""));
        this.addItem(this.createItem(1, Material.GOLDEN_APPLE, 1, true, "UHC", ""));
        this.addItem(this.createItem(1, Material.CROSSBOW, 1, true, "Duels", "" ));
        this.addItem(this.createItem(1, Material.IRON_SWORD, 1, true, "MurderMystery", "" ));
        this.addItem(this.createItem(1, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(this.createItem(1, Material.PLAYER_HEAD, 1, true, "Профиль", ""));
        this.addItem(this.createItem(2, Material.GRAY_STAINED_GLASS_PANE, 1,false, " ", ""));
        this.addItem(this.createItem(1, Material.NOTE_BLOCK, 1,true, "BlockParty", ""));
        this.addItem(this.createItem(1, Material.GRASS_BLOCK, 1,true, "SkyBlock", "§c§lНЕ ЗАХОДИТЬ!!!"));
        this.addItem(this.createItem(1, Material.SMOOTH_STONE, 1,true, "Parkour", ""));
        this.addItem(this.createItem(1, Material.TNT, 1,true, "TNTRun", ""));
        this.addItem(this.createItem(1, Material.SKELETON_SKULL, 1,true, "DeathRun", ""));
        this.addItem(this.createItem(1, Material.GRAY_STAINED_GLASS_PANE, 1,false, " ", "" ));
        this.addItem(this.createItem(1, Material.WRITABLE_BOOK, 1,true, "PartyRooms", "" ));
        this.addItem(this.createItem(2, Material.GRAY_STAINED_GLASS_PANE, 1,false, " ", ""));
        this.addItem(this.createItem(1, Material.FIREWORK_ROCKET, 1,true, "Bingo", ""));
        this.addItem(this.createItem(1, Material.BLAZE_ROD, 1,true, "PixelPainters", ""));
        this.addItem(this.createItem(1, Material.PAPER, 1,true, "Crocodile", ""));
        this.addItem(this.createItem(1, Material.GOLDEN_BOOTS, 1,true, "SpeedRun", ""));
        this.addItem(this.createItem(1, Material.BONE, 1, true, "HungerGames", ""));
        this.addItem(this.createItem(3, Material.GRAY_STAINED_GLASS_PANE, 1,   false, " ", ""));
        this.addItem(this.createItem(1, Material.WHITE_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(this.createItem(7, Material.GRAY_STAINED_GLASS_PANE, 1, false,  " ", ""));
        this.addItem(this.createItem(1, Material.WHITE_STAINED_GLASS_PANE, 1, false, " ", ""));
    }

}
