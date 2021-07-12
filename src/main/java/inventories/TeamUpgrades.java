package inventories;

import game.Participant;
import game.Team;
import main.PlayerManager;
import org.bukkit.Bukkit;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TeamUpgrades implements IGUI{
    private final Team team;

    public TeamUpgrades(Team team){
        this.team = team;
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem) {
        ItemMeta meta = clickedItem.getItemMeta();
        List<String> lore = meta.getLore();
        switch (slot){
            case 10:
                int lvl1 = this.getTeam().getTeamUpgrades().get("Sharpness");
                switch (lvl1) {
                    case 0:
                        if(this.getTeam().getTeammates().get(whoClicked.getName()).takeItem(Material.DIAMOND, 4)) {

                            for(Participant p : this.getTeam().getTeammates().values()){
                                p.getPlayer().sendMessage(PlayerManager.getCodeColor(p) + whoClicked.getName() + " §7купил улучшение §bОстрота");

                                for(ItemStack itemStack : p.getPlayer().getInventory().getContents()){
                                    if(itemStack == null) continue;

                                    ItemMeta swordMeta = itemStack.getItemMeta();

                                    switch (itemStack.getType()) {
                                        case WOODEN_SWORD:
                                        case STONE_SWORD:
                                        case IRON_SWORD:
                                        case DIAMOND_SWORD:
                                            swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                                            break;
                                        default: break;
                                    }

                                    itemStack.setItemMeta(swordMeta);
                                }
                            }

                            int var0 = lore.indexOf("§7[Уровень: I]");
                            lore.remove("§7[Уровень: I]");
                            lore.add(var0, "§c[Уровень: I]");
                            int var1 = lore.indexOf("§8Стоимость: §b4 алмаза");
                            lore.remove("§8Стоимость: §b4 алмаза");
                            lore.add(var1, "§8Стоимость: §b20 алмазов");

                            meta.setLore(lore);

                            this.getTeam().getTeamUpgrades().replace("Sharpness", 1);
                        } else whoClicked.sendMessage("§cНедостаточно ресурсов");
                        break;
                    case 1:
                        if(this.getTeam().getTeammates().get(whoClicked.getName()).takeItem(Material.DIAMOND, 20)) {

                            for (Participant p : this.getTeam().getTeammates().values()) {
                                p.getPlayer().sendMessage(PlayerManager.getCodeColor(p) + whoClicked.getName() + " §7купил улучшение §bОстрота");

                                for(ItemStack itemStack : p.getPlayer().getInventory().getContents()){
                                    if(itemStack == null) continue;

                                    ItemMeta meta2 = itemStack.getItemMeta();

                                    switch (itemStack.getType()) {
                                        case WOODEN_SWORD:
                                        case STONE_SWORD:
                                        case IRON_SWORD:
                                        case DIAMOND_SWORD:
                                            meta2.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
                                            break;
                                        default: break;
                                    }

                                    itemStack.setItemMeta(meta2);
                                }
                            }

                            int var0 = lore.indexOf("§7[Уровень: II]");
                            lore.remove("§7[Уровень: II]");
                            lore.add(var0, "§c[Уровень: II]");
                            lore.add(" ");
                            lore.add("§a§lКУПЛЕНО");

                            lore.remove("§8Стоимость: §b20 алмазов");

                            meta.setLore(lore);

                            this.getTeam().getTeamUpgrades().replace("Sharpness", 2);
                        } else whoClicked.sendMessage("§cНедостаточно ресурсов");
                        break;
                }
                break;
            case 11:
                int lvl2 = this.getTeam().getTeamUpgrades().get("Protection");
                switch (lvl2) {
                    case 0:
                        if(this.getTeam().getTeammates().get(whoClicked.getName()).takeItem(Material.DIAMOND, 2)) {

                            for(Participant p : this.getTeam().getTeammates().values()){
                                p.getPlayer().sendMessage(PlayerManager.getCodeColor(p) + whoClicked.getName() + " §7купил улучшение §bЗащита");

                                for(ItemStack itemStack : p.getPlayer().getInventory().getContents()){
                                    if(itemStack == null) continue;

                                    ItemMeta meta2 = itemStack.getItemMeta();

                                    switch (itemStack.getType()) {
                                        case LEATHER_BOOTS:
                                        case LEATHER_CHESTPLATE:
                                        case LEATHER_HELMET:
                                        case LEATHER_LEGGINGS:
                                        case GOLDEN_BOOTS:
                                        case GOLDEN_LEGGINGS:
                                        case DIAMOND_BOOTS:
                                        case DIAMOND_LEGGINGS:
                                        case IRON_BOOTS:
                                        case IRON_LEGGINGS:
                                        case CHAINMAIL_BOOTS:
                                        case CHAINMAIL_LEGGINGS:
                                            meta2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                                            break;
                                        default: break;
                                    }

                                    itemStack.setItemMeta(meta2);
                                }
                            }

                            int var0 = lore.indexOf("§7[Уровень: I]");
                            lore.remove("§7[Уровень: I]");
                            lore.add(var0, "§c[Уровень: I]");
                            int var1 = lore.indexOf("§8Стоимость: §b2 алмаза");
                            lore.remove("§8Стоимость: §b2 алмаза");
                            lore.add(var1, "§8Стоимость: §b4 алмаза");

                            meta.setLore(lore);

                            this.getTeam().getTeamUpgrades().replace("Protection", 1);
                        } else whoClicked.sendMessage("§cНедостаточно ресурсов");
                        break;
                    case 1:
                        if(this.getTeam().getTeammates().get(whoClicked.getName()).takeItem(Material.DIAMOND, 4)) {

                            for (Participant p : this.getTeam().getTeammates().values()) {
                                p.getPlayer().sendMessage(PlayerManager.getCodeColor(p) + whoClicked.getName() + " §7купил улучшение §bЗащита");

                                for(ItemStack itemStack : p.getPlayer().getInventory().getContents()){
                                    if(itemStack == null) continue;

                                    ItemMeta meta2 = itemStack.getItemMeta();

                                    switch (itemStack.getType()) {
                                        case LEATHER_BOOTS:
                                        case LEATHER_CHESTPLATE:
                                        case LEATHER_HELMET:
                                        case LEATHER_LEGGINGS:
                                        case GOLDEN_BOOTS:
                                        case GOLDEN_LEGGINGS:
                                        case DIAMOND_BOOTS:
                                        case DIAMOND_LEGGINGS:
                                        case IRON_BOOTS:
                                        case IRON_LEGGINGS:
                                        case CHAINMAIL_BOOTS:
                                        case CHAINMAIL_LEGGINGS:
                                            meta2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                                            break;
                                        default: break;
                                    }

                                    itemStack.setItemMeta(meta2);
                                }
                            }

                            int var0 = lore.indexOf("§7[Уровень: II]");
                            lore.remove("§7[Уровень: II]");
                            lore.add(var0, "§c[Уровень: II]");
                            int var1 = lore.indexOf("§8Стоимость: §b4 алмаза");
                            lore.remove("§8Стоимость: §b4 алмаза");
                            lore.add(var1, "§8Стоимость: §b6 алмазов");

                            meta.setLore(lore);

                            this.getTeam().getTeamUpgrades().replace("Protection", 2);
                        } else whoClicked.sendMessage("§cНедостаточно ресурсов");
                        break;
                    case 2:
                        if(this.getTeam().getTeammates().get(whoClicked.getName()).takeItem(Material.DIAMOND, 6)) {

                            for (Participant p : this.getTeam().getTeammates().values()) {
                                p.getPlayer().sendMessage(PlayerManager.getCodeColor(p) + whoClicked.getName() + " §7купил улучшение §bЗащита");

                                for(ItemStack itemStack : p.getPlayer().getInventory().getContents()){
                                    if(itemStack == null) continue;

                                    ItemMeta meta2 = itemStack.getItemMeta();

                                    switch (itemStack.getType()) {
                                        case LEATHER_BOOTS:
                                        case LEATHER_CHESTPLATE:
                                        case LEATHER_HELMET:
                                        case LEATHER_LEGGINGS:
                                        case GOLDEN_BOOTS:
                                        case GOLDEN_LEGGINGS:
                                        case DIAMOND_BOOTS:
                                        case DIAMOND_LEGGINGS:
                                        case IRON_BOOTS:
                                        case IRON_LEGGINGS:
                                        case CHAINMAIL_BOOTS:
                                        case CHAINMAIL_LEGGINGS:
                                            meta2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
                                            break;
                                        default: break;
                                    }

                                    itemStack.setItemMeta(meta2);
                                }
                            }

                            int var0 = lore.indexOf("§7[Уровень: III]");
                            lore.remove("§7[Уровень: III]");
                            lore.add(var0, "§c[Уровень: III]");
                            int var1 = lore.indexOf("§8Стоимость: §b6 алмазов");
                            lore.remove("§8Стоимость: §b6 алмазов");
                            lore.add(var1, "§8Стоимость: §b10 алмазов");

                            meta.setLore(lore);

                            this.getTeam().getTeamUpgrades().replace("Protection", 3);
                        } else whoClicked.sendMessage("§cНедостаточно ресурсов");
                        break;
                    case 3:
                        if(this.getTeam().getTeammates().get(whoClicked.getName()).takeItem(Material.DIAMOND, 10)) {

                            for (Participant p : this.getTeam().getTeammates().values()) {
                                p.getPlayer().sendMessage(PlayerManager.getCodeColor(p) + whoClicked.getName() + "§7купил улучшение §bЗащита");

                                for(ItemStack itemStack : p.getPlayer().getInventory().getContents()){
                                    if(itemStack == null) continue;

                                    ItemMeta meta2 = itemStack.getItemMeta();

                                    switch (itemStack.getType()) {
                                        case LEATHER_BOOTS:
                                        case LEATHER_CHESTPLATE:
                                        case LEATHER_HELMET:
                                        case LEATHER_LEGGINGS:
                                        case GOLDEN_BOOTS:
                                        case GOLDEN_LEGGINGS:
                                        case DIAMOND_BOOTS:
                                        case DIAMOND_LEGGINGS:
                                        case IRON_BOOTS:
                                        case IRON_LEGGINGS:
                                        case CHAINMAIL_BOOTS:
                                        case CHAINMAIL_LEGGINGS:
                                            meta2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
                                            break;
                                        default: break;
                                    }

                                    itemStack.setItemMeta(meta2);
                                }
                            }

                            int var1 = lore.indexOf("§7[Уровень: IV]");
                            lore.remove("§7[Уровень: IV]");
                            lore.add(var1, "§c[Уровень: IV]");
                            lore.add(" ");
                            lore.add("§a§lКУПЛЕНО");

                            lore.remove("§8Стоимость: §b10 алмазов");

                            meta.setLore(lore);

                            this.getTeam().getTeamUpgrades().replace("Protection", 4);
                        } else whoClicked.sendMessage("§cНедостаточно ресурсов");
                        break;
                }
                break;
            case 12:
                int lvl3 = this.getTeam().getTeamUpgrades().get("Haste");
                switch (lvl3) {
                    case 0:
                        if(this.getTeam().getTeammates().get(whoClicked.getName()).takeItem(Material.DIAMOND, 2)) {

                            for(Participant p : this.getTeam().getTeammates().values()){
                                p.getPlayer().sendMessage(PlayerManager.getCodeColor(p) + whoClicked.getName() + " §7купил улучшение §bЭффективность");

                                for(ItemStack itemStack : p.getPlayer().getInventory().getContents()){
                                    if(itemStack == null) continue;

                                    ItemMeta meta2 = itemStack.getItemMeta();

                                    switch (itemStack.getType()) {
                                        case WOODEN_AXE:
                                        case WOODEN_PICKAXE:
                                            meta2.addEnchant(Enchantment.DIG_SPEED, 2, true);
                                            break;
                                        case STONE_AXE:
                                        case STONE_PICKAXE:
                                            meta2.addEnchant(Enchantment.DIG_SPEED, 3, true);
                                            break;
                                        case IRON_AXE:
                                        case IRON_PICKAXE:
                                            meta2.addEnchant(Enchantment.DIG_SPEED, 4, true);
                                            break;
                                        case DIAMOND_AXE:
                                        case DIAMOND_PICKAXE:
                                            meta2.addEnchant(Enchantment.DIG_SPEED, 5, true);
                                            break;
                                        case GOLDEN_AXE:
                                        case GOLDEN_PICKAXE:
                                            meta2.addEnchant(Enchantment.DIG_SPEED, 6, true);
                                            break;
                                        default: break;
                                    }

                                    itemStack.setItemMeta(meta2);
                                }
                            }

                            int var0 = lore.indexOf("§7[Уровень: I]");
                            lore.remove("§7[Уровень: I]");
                            lore.add(var0, "§c[Уровень: I]");
                            int var1 = lore.indexOf("§8Стоимость: §b2 алмаза");
                            lore.remove("§8Стоимость: §b2 алмаза");
                            lore.add(var1, "§8Стоимость: §b4 алмаза");

                            meta.setLore(lore);

                            this.getTeam().getTeamUpgrades().replace("Haste", 1);
                        } else whoClicked.sendMessage("§cНедостаточно ресурсов");
                        break;
                    case 1:
                        if(this.getTeam().getTeammates().get(whoClicked.getName()).takeItem(Material.DIAMOND, 4)) {

                            for (Participant p : this.getTeam().getTeammates().values()) {
                                p.getPlayer().sendMessage(PlayerManager.getCodeColor(p) + whoClicked.getName() + " §7купил улучшение §bЭффективность");

                                for(ItemStack itemStack : p.getPlayer().getInventory().getContents()){
                                    if(itemStack == null) continue;

                                    ItemMeta meta2 = itemStack.getItemMeta();

                                    switch (itemStack.getType()) {
                                        case WOODEN_AXE:
                                        case WOODEN_PICKAXE:
                                            meta2.addEnchant(Enchantment.DIG_SPEED, 3, true);
                                            break;
                                        case STONE_AXE:
                                        case STONE_PICKAXE:
                                            meta2.addEnchant(Enchantment.DIG_SPEED, 4, true);
                                            break;
                                        case IRON_AXE:
                                        case IRON_PICKAXE:
                                            meta2.addEnchant(Enchantment.DIG_SPEED, 5, true);
                                            break;
                                        case DIAMOND_AXE:
                                        case DIAMOND_PICKAXE:
                                            meta2.addEnchant(Enchantment.DIG_SPEED, 6, true);
                                            break;
                                        case GOLDEN_AXE:
                                        case GOLDEN_PICKAXE:
                                            meta2.addEnchant(Enchantment.DIG_SPEED, 7, true);
                                            break;
                                        default: break;
                                    }

                                    itemStack.setItemMeta(meta2);
                                }
                            }

                            int var0 = lore.indexOf("§7[Уровень: II]");
                            lore.remove("§7[Уровень: II]");
                            lore.add(var0, "§c[Уровень: II]");
                            lore.add(" ");
                            lore.add("§a§lКУПЛЕНО");

                            lore.remove("§8Стоимость: §b4 алмаза");

                            meta.setLore(lore);

                            this.getTeam().getTeamUpgrades().replace("Haste", 2);
                        } else whoClicked.sendMessage("§cНедостаточно ресурсов");
                        break;
                }
                break;

            case 13:
                int lvl4 = this.getTeam().getTeamUpgrades().get("Bedrok");

                for(String string : lore){
                    if (string.matches("(.*)ОСТАЛОСЬ ВРЕМЕНИ(.*)")) {
                        lvl4 = 0;
                        break;
                    }
                }

                if(lvl4 == 0) return;

                if(this.getTeam().getTeammates().get(whoClicked.getName()).takeItem(Material.DIAMOND, 6)) {
                    lore.remove("§7Количество использований: §b" + lvl4);
                    lore.add("§7Количество использований: §b" + (lvl4 - 1));

                    for(Participant p : this.getTeam().getTeammates().values()){
                        p.getPlayer().sendMessage(PlayerManager.getCodeColor(p) + whoClicked.getName() + " §7купил улучшение §bНесокрушимость");
                    }

                    this.getTeam().getTeamUpgrades().replace("Bedrok", lvl4 - 1);

                    if(lvl4 == 1) lore.remove("§8Стоимость: §b6 алмазов");

                    String cords_bottom = this.getTeam().getPlugin().getConfig().getString("teams." + this.getTeam().getColor() + ".bed_bottom");
                    String cords_top = this.getTeam().getPlugin().getConfig().getString("teams." + this.getTeam().getColor() + ".bed_top");

                    List<Block> bedrok = new ArrayList<>();

                    bedrok.add(Bukkit.getWorld("world").getBlockAt(Utils.getLocation(cords_bottom)).getRelative(BlockFace.EAST));
                    bedrok.add(Bukkit.getWorld("world").getBlockAt(Utils.getLocation(cords_bottom)).getRelative(BlockFace.WEST));
                    bedrok.add(Bukkit.getWorld("world").getBlockAt(Utils.getLocation(cords_bottom)).getRelative(BlockFace.NORTH));
                    bedrok.add(Bukkit.getWorld("world").getBlockAt(Utils.getLocation(cords_bottom)).getRelative(BlockFace.SOUTH));
                    bedrok.add(Bukkit.getWorld("world").getBlockAt(Utils.getLocation(cords_bottom)).getRelative(BlockFace.UP));
                    bedrok.add(Bukkit.getWorld("world").getBlockAt(Utils.getLocation(cords_top)).getRelative(BlockFace.EAST));
                    bedrok.add(Bukkit.getWorld("world").getBlockAt(Utils.getLocation(cords_top)).getRelative(BlockFace.WEST));
                    bedrok.add(Bukkit.getWorld("world").getBlockAt(Utils.getLocation(cords_top)).getRelative(BlockFace.NORTH));
                    bedrok.add(Bukkit.getWorld("world").getBlockAt(Utils.getLocation(cords_top)).getRelative(BlockFace.SOUTH));
                    bedrok.add(Bukkit.getWorld("world").getBlockAt(Utils.getLocation(cords_top)).getRelative(BlockFace.UP));

                    for (Block b : bedrok) {
                        if (b.getType().equals(Material.getMaterial(this.getTeam().getColor().toUpperCase(Locale.ROOT) + "_BED")))
                            continue;
                        b.setType(Material.BEDROCK);
                    }

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
                            clickedItem.setItemMeta(meta);

                            time--;
                        }
                    }, 0L, 20L);

                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getTeam().getPlugin(), () -> {
                        Bukkit.getServer().getScheduler().cancelTask(id);

                        for (Block b : bedrok) {
                            if (b.getType().equals(Material.getMaterial(getTeam().getColor().toUpperCase(Locale.ROOT) + "_BED")))
                                continue;
                            b.setType(Material.AIR);
                        }

                        lore.remove("  ");
                        lore.remove("§7§lОСТАЛОСЬ ВРЕМЕНИ: §b§l" + Utils.getTime(0));

                        meta.setLore(lore);
                        clickedItem.setItemMeta(meta);
                    }, 1820L);

                    meta.setLore(lore);

                } else whoClicked.sendMessage("§cНедостаточно ресурсов");
                break;
            case 14:
                int lvl5 = this.getTeam().getTeamUpgrades().get("Forge");
                switch (lvl5) {
                    case 0:
                        if(this.getTeam().getTeammates().get(whoClicked.getName()).takeItem(Material.DIAMOND, 2)) {

                            for(Participant p : this.getTeam().getTeammates().values()){
                                p.getPlayer().sendMessage(PlayerManager.getCodeColor(p) + whoClicked.getName() + " §7купил улучшение §bКузня");
                            }

                            this.getTeam().setSilverTimeout(2);
                            this.getTeam().setGoldTimeout(4);

                            int var0 = lore.indexOf("§7[Уровень: I]");
                            lore.remove("§7[Уровень: I]");
                            lore.add(var0, "§c[Уровень: I]");
                            int var1 = lore.indexOf("§8Стоимость: §b2 алмаза");
                            lore.remove("§8Стоимость: §b2 алмаза");
                            lore.add(var1, "§8Стоимость: §b6 алмаза");

                            meta.setLore(lore);

                            this.getTeam().getTeamUpgrades().replace("Forge", 1);
                        } else whoClicked.sendMessage("§cНедостаточно ресурсов");
                        break;
                    case 1:
                        if(this.getTeam().getTeammates().get(whoClicked.getName()).takeItem(Material.DIAMOND, 6)) {

                            for (Participant p : this.getTeam().getTeammates().values()) {
                                p.getPlayer().sendMessage(PlayerManager.getCodeColor(p) + whoClicked.getName() + " §7купил улучшение §bКузня");
                            }

                            this.getTeam().setSilverTimeout(1);
                            this.getTeam().setGoldTimeout(2);

                            int var0 = lore.indexOf("§7[Уровень: II]");
                            lore.remove("§7[Уровень: II]");
                            lore.add(var0, "§c[Уровень: II]");
                            int var1 = lore.indexOf("§8Стоимость: §b6 алмаза");
                            lore.remove("§8Стоимость: §b6 алмаза");
                            lore.add(var1, "§8Стоимость: §b10 алмазов");

                            meta.setLore(lore);

                            this.getTeam().getTeamUpgrades().replace("Forge", 2);
                        } else whoClicked.sendMessage("§cНедостаточно ресурсов");
                        break;
                    case 2:
                        if(this.getTeam().getTeammates().get(whoClicked.getName()).takeItem(Material.DIAMOND, 10)) {

                            for (Participant p : this.getTeam().getTeammates().values()) {
                                p.getPlayer().sendMessage(PlayerManager.getCodeColor(p) + whoClicked.getName() + " §7купил улучшение §bКузня");
                            }

                            this.getTeam().setGoldTimeout(1);

                            int var0 = lore.indexOf("§7[Уровень: III]");
                            lore.remove("§7[Уровень: III]");
                            lore.add(var0, "§c[Уровень: III]");
                            lore.add(" ");
                            lore.add("§a§lКУПЛЕНО");

                            lore.remove("§8Стоимость: §b10 алмазов");

                            meta.setLore(lore);

                            this.getTeam().getTeamUpgrades().replace("Forge", 3);
                        } else whoClicked.sendMessage("§cНедостаточно ресурсов");
                        break;
                }
                break;
            case 15:
                int lvl6 = this.getTeam().getTeamUpgrades().get("Healing");
                if (lvl6 == 0) {
                    if (this.getTeam().getTeammates().get(whoClicked.getName()).takeItem(Material.DIAMOND, 2)) {

                        for (Participant p : this.getTeam().getTeammates().values()) {
                            p.getPlayer().sendMessage(PlayerManager.getCodeColor(p) + whoClicked.getName() + " §7купил улучшение §bИсцеление");
                        }

                        int var0 = lore.indexOf("§7[Уровень: I]");
                        lore.remove("§7[Уровень: I]");
                        lore.add(var0, "§c[Уровень: I]");
                        lore.remove("§8Стоимость: §b2 алмаза");
                        lore.add(" ");
                        lore.add("§a§lКУПЛЕНО");

                        meta.setLore(lore);

                        this.getTeam().getTeamUpgrades().replace("Healing", 1);
                    } else whoClicked.sendMessage("§cНедостаточно ресурсов");
                }
                break;
            case 16:
                if(this.getTeam().getTraps().size() == 3) return;
                whoClicked.openInventory(this.getTeam().getTrapsInventory());
        }

        clickedItem.setItemMeta(meta);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return team.getUpgradesInventory();
    }

    private Team getTeam(){return this.team;}
}
