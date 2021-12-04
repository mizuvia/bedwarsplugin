package inventories;

import game.*;
import main.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import util.Utils;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;

public class TeamUpgrades implements IGUI{
    private final Team team;
    private final Map<String, UpgradeInfo> upgrades = new HashMap<>();
    private final static List<String> UPGRADES_NAMES = Arrays.asList("Sharpness", "Protection", "Haste", "Bedrock", "Forge", "Healing", "Traps");
    private final static int BEDROCK_MAX_USAGES = 2;

    public TeamUpgrades(Team team){
        this.team = team;
        upgrades.put("Sharpness", new UpgradeInfo(
            "§bОстрота",
            Arrays.asList(new LevelInfo(new ItemPrice(Material.DIAMOND, 4), 1), new LevelInfo(new ItemPrice(Material.DIAMOND, 20), 2)),
            Enchantment.DAMAGE_ALL,
            Arrays.asList(Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD)
        ));
        upgrades.put("Protection", new UpgradeInfo(
            "§bЗащита",
            Arrays.asList(new LevelInfo(new ItemPrice(Material.DIAMOND, 2), 1), new LevelInfo(new ItemPrice(Material.DIAMOND, 4), 2), new LevelInfo(new ItemPrice(Material.DIAMOND, 6), 3), new LevelInfo(new ItemPrice(Material.DIAMOND, 10), 4)),
            Enchantment.PROTECTION_ENVIRONMENTAL,
            Arrays.asList(Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_HELMET, Material.DIAMOND_BOOTS, Material.DIAMOND_LEGGINGS, Material.IRON_BOOTS, Material.IRON_LEGGINGS)
        ));
        upgrades.put("Haste", new UpgradeInfo(
            "§bЭффективность",
            Arrays.asList(new LevelInfo(new ItemPrice(Material.DIAMOND, 2), 1), new LevelInfo(new ItemPrice(Material.DIAMOND, 4), 2)),
            Enchantment.DIG_SPEED,
            Arrays.asList(Material.WOODEN_AXE, Material.WOODEN_PICKAXE, Material.STONE_AXE, Material.STONE_PICKAXE, Material.IRON_AXE, Material.IRON_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_PICKAXE, Material.GOLDEN_AXE, Material.GOLDEN_PICKAXE)
        ));
        upgrades.put("Bedrock", new UpgradeInfo(
            "§bНесокрушимость",
            List.of(new LevelInfo(new ItemPrice(Material.DIAMOND, 6), 1), new LevelInfo(new ItemPrice(Material.DIAMOND, 6), 2))
        ));
        upgrades.put("Forge", new UpgradeInfo(
            "§bКузня",
            Arrays.asList(new LevelInfo(new ItemPrice(Material.DIAMOND, 2), 1), new LevelInfo(new ItemPrice(Material.DIAMOND, 6), 2), new LevelInfo(new ItemPrice(Material.DIAMOND, 10), 3))
        ));
        upgrades.put("Healing", new UpgradeInfo(
            "§bИсцеление",
            List.of(new LevelInfo(new ItemPrice(Material.DIAMOND, 2), 1))
        ));
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem) {

        if(isTrapsClicked(slot)){
            if(this.getTeam().getTraps().size() == 3) return;
            whoClicked.openInventory(this.getTeam().getTrapsInventory());
            return;
        }

        ItemMeta meta = clickedItem.getItemMeta();
        List<String> lore = meta.getLore();
        String name = UPGRADES_NAMES.get(slot - 10);
        int level = this.getTeam().getTeamUpgrades().get(name);
        if(name.equals("Bedrock")){
            for (String string : lore) {
                if (string.matches("(.*)ОСТАЛОСЬ ВРЕМЕНИ(.*)")) {
                    level = 2;
                    break;
                }
            }
        }
        if(level >= upgrades.get(name).getLevels().size()) return;

        if(!takePrice(name, level, whoClicked)) return;

        switch(slot){
            case 10, 11, 12 -> updateEnchantment(name, level, whoClicked);
            case 13 -> setBedrock(level, lore, clickedItem);
            case 14 -> changeResourcesTimeout(level);
        }

        informPlayers(name, whoClicked);
        if(!name.equals("Bedrock")) updateItem(name, level, meta);

        this.getTeam().getTeamUpgrades().replace(name, level + 1);

        clickedItem.setItemMeta(meta);
    }

    private boolean isTrapsClicked(int slot) {
        return slot == 16;
    }

    private boolean takePrice(String name, int level, Player whoClicked) {
        ItemPrice itemPrice = upgrades.get(name).getLevels().get(level).getItemPrice();
        boolean abil = this.getTeam().getTeammates().get(whoClicked.getName()).takeItem(itemPrice.getMaterial(), itemPrice.getPrice());
        if(!abil) whoClicked.sendMessage("§cНедостаточно ресурсов");
        return abil;
    }

    private void updateEnchantment(String name, int level, Player whoClicked){
        for(Participant p : this.getTeam().getTeammates().values()){
            for(ItemStack itemStack : p.getPlayer().getInventory().getContents()){
                if(itemStack == null) continue;
                if(!upgrades.get(name).getTypes().contains(itemStack.getType())) continue;

                ItemMeta meta = itemStack.getItemMeta();
                if(!name.equals("Haste"))
                    meta.addEnchant(upgrades.get(name).getEnchantment(), upgrades.get(name).getLevels().get(level).getEnchantmentLevel(), true);
                else {
                    switch (itemStack.getType()) {
                        case WOODEN_AXE, WOODEN_PICKAXE -> meta.addEnchant(Enchantment.DIG_SPEED, 3 + level, true);
                        case STONE_AXE, STONE_PICKAXE -> meta.addEnchant(Enchantment.DIG_SPEED, 4 + level, true);
                        case IRON_AXE, IRON_PICKAXE -> meta.addEnchant(Enchantment.DIG_SPEED, 5 + level, true);
                        case DIAMOND_AXE, DIAMOND_PICKAXE -> meta.addEnchant(Enchantment.DIG_SPEED, 6 + level, true);
                        case GOLDEN_AXE, GOLDEN_PICKAXE -> meta.addEnchant(Enchantment.DIG_SPEED, 7 + level, true);
                        default -> {
                        }
                    }
                }

                itemStack.setItemMeta(meta);
            }
        }
    }

    private void setBedrock(int level, List<String> lore, ItemStack item) {
        setAndDeleteBedrock();
        updateBedrockItem(level, lore, item);
    }

    private void setAndDeleteBedrock() {
        List<Block> bedrok = new ArrayList<>();

        fillBlocks(this.getTeam().getBedBottomLocation(), bedrok);
        fillBlocks(this.getTeam().getBedTopLocation(), bedrok);

        List<Material> previousBlocks = new ArrayList<>();

        for (Block b : bedrok) {
            if (b.getType().equals(Material.getMaterial(this.getTeam().getColor().toUpperCase(Locale.ROOT) + "_BED")))
                continue;
            previousBlocks.add(b.getType());
            b.setType(Material.BEDROCK);
        }

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getTeam().getPlugin(), () -> {
            for (Block b : bedrok) {
                if (b.getType().equals(Material.getMaterial(getTeam().getColor().toUpperCase(Locale.ROOT) + "_BED")))
                    continue;
                b.setType(previousBlocks.get(bedrok.indexOf(b)));
            }
        }, 1820L);
    }

    private void updateBedrockItem(int level, List<String> lore, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        lore.remove("§7Количество использований: §b" + (BEDROCK_MAX_USAGES - level));
        lore.add("§7Количество использований: §b" + (BEDROCK_MAX_USAGES - (level + 1)));
        if (level == 1) lore.remove("§8Стоимость: §b6 алмазов");

        meta.setLore(lore);

        int id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.getTeam().getPlugin(), new Runnable() {
            private int time = 90;

            @Override
            public void run() {
                if (time == 90) {
                    lore.add("  ");
                } else {
                    lore.remove("§7§lОСТАЛОСЬ ВРЕМЕНИ: §b§l" + Utils.getTime(time + 1));
                }
                lore.add("§7§lОСТАЛОСЬ ВРЕМЕНИ: §b§l" + Utils.getTime(time));

                meta.setLore(lore);
                item.setItemMeta(meta);

                time--;
            }
        }, 0L, 20L);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getTeam().getPlugin(), () -> {
            Bukkit.getServer().getScheduler().cancelTask(id);

            lore.remove("  ");
            lore.remove("§7§lОСТАЛОСЬ ВРЕМЕНИ: §b§l" + Utils.getTime(0));

            meta.setLore(lore);
            item.setItemMeta(meta);
        }, 1820L);
    }

    private void fillBlocks(Location loc, List<Block> bedrok) {
        bedrok.add(Bukkit.getWorld("world").getBlockAt(loc).getRelative(BlockFace.EAST));
        bedrok.add(Bukkit.getWorld("world").getBlockAt(loc).getRelative(BlockFace.WEST));
        bedrok.add(Bukkit.getWorld("world").getBlockAt(loc).getRelative(BlockFace.NORTH));
        bedrok.add(Bukkit.getWorld("world").getBlockAt(loc).getRelative(BlockFace.SOUTH));
        bedrok.add(Bukkit.getWorld("world").getBlockAt(loc).getRelative(BlockFace.UP));
    }

    private void changeResourcesTimeout(int level) {
    }

    private void informPlayers(String name, Player whoClicked) {
        for(Participant p : this.getTeam().getTeammates().values()){
            p.getPlayer().sendMessage(PlayerManager.getCodeColor(p) + whoClicked.getName() + " §7купил улучшение " + upgrades.get(name).getName());
        }
    }

    private void updateItem(String name, int level, ItemMeta meta) {
        List<String> lore = meta.getLore();

        String levelStr = "[Уровень: " + Utils.getRomanNumeral(level + 1) + "]";

        int var0 = lore.indexOf("§7" + levelStr);
        lore.remove("§7" + levelStr);
        lore.add(var0, "§c" + levelStr);

        List<LevelInfo> lvls = upgrades.get(name).getLevels();
        Logger.getLogger("").info(lvls.get(level).getItemPrice().toString());

        int var1 = lore.indexOf(lvls.get(level).getItemPrice().toString());
        lore.remove(lvls.get(level).getItemPrice().toString());

        if(level + 1 == lvls.size()){
            lore.add(" ");
            lore.add("§a§lКУПЛЕНО");
        }
        else lore.add(var1, lvls.get(level + 1).getItemPrice().toString());

        meta.setLore(lore);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return team.getUpgradesInventory();
    }

    private Team getTeam(){return this.team;}
}
