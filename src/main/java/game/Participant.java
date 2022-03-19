package game;

import inventories.*;
import loading.Sidebar;
import main.PlayerManager;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import tab.Tab;
import util.LastDamager;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import util.PlayerInv;
import util.Utils;

import java.util.*;

public class Participant {

    private final Player player;
    private final Plugin plugin;
    private Team team;
    private String group;
    private final ShopInventory shop = new ShopInventory(new Shop(this), 54, "Магазин");
    private final Map<Integer, SimpleInventory> inventories = new HashMap<>();
    private final List<ItemStack> respawnItems = new ArrayList<>();
    private boolean isTeleporting = false;
    private boolean isUnderMilk = false;
    private int brokenBeds = 0;
    private int killedPlayers = 0;
    private int finalKills = 0;
    private final Scoreboard scoreboard;
    private final Objective sidebarObjective;
    private final List<org.bukkit.scoreboard.Team> sidebarTeams = new ArrayList<>();
    private BukkitTask showTask;
    private ItemStack[] hiddenArmor;
    private final LastDamager lastDamager;
    
    public Participant(Player player, Plugin plugin){
        this.player = player;
        this.plugin = plugin;
        this.setGroup();
        player.setInvisible(false);
        player.setCanPickupItems(true);
        this.clearPotionEffects();
        player.getEnderChest().clear();
        createInventories();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.sidebarObjective = scoreboard.registerNewObjective("sidebar", "dummy", Sidebar.SIDEBAR_NAME);
        this.sidebarObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.lastDamager = new LastDamager();
        this.getPlayer().setScoreboard(scoreboard);
        plugin.getSidebar().fillPlayerSidebars(this);
        plugin.getTab().createTab(this.scoreboard);
        plugin.getTab().addPlayerToTabs(this);
        plugin.getPlayers().put(player.getUniqueId(), this);
    }

    private void createInventories() {
        inventories.put(12, new SimpleInventory(plugin, "Броня", ShopItems.ARMOR));
        inventories.put(14, new SimpleInventory(plugin, "Инструменты", ShopItems.TOOLS));
        inventories.putAll(plugin.getGame().getInventories());
    }

    public Map<Integer, SimpleInventory> getInventories() {
        return inventories;
    }

    public List<org.bukkit.scoreboard.Team> getSidebarTeams() {
        return sidebarTeams;
    }

    public void increaseFinalKills() {
        this.finalKills++;
        this.increaseKilledPlayers();
        plugin.getSidebar().changeFinalKills(this);
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public Objective getSidebarObjective() {
        return sidebarObjective;
    }
    
    public int getFinalKills() { return this.finalKills; }

    public Plugin getPlugin(){ return this.plugin; }

    public Player getPlayer() {
        return this.player;
    }

    public Team getTeam() {
        return this.team;
    }

    public boolean hasTeam(){
        return this.team != null;
    }

    public void setTeam(Team team){
        Tab tab = this.plugin.getTab();

    	if (this.team != null) {
            TeamSelection.removePlayerFromItem(plugin, this);

            this.team.removeTeammate(this);
            tab.removePlayerFromTabs(this);
        }
        if (team != null) {
            TeamSelection.addPlayerToItem(plugin, team, this.getPlayer());
            tab.addPlayerToTabs(this);

            this.getPlayer().setDisplayName("§8§l[" + team.getName() + "§8§l]§r§7 " + this.getPlayer().getName());
            this.getPlayer().setPlayerListName("§8§l[" + team.getName() + "§8§l]§r§7 " + this.getPlayer().getName());

            team.addTeammate(this);
        }

        this.team = team;
    }

    public String getGroup(){
        return this.group;
    }

    public void setGroup(String group){
        this.group = group;
    }

    public void setGroup(){
        this.group = PlayerManager.getPlayerGroupName(this.player);
    }

    public ShopInventory getShopInventory(){
        return this.shop;
    }

    public List<ItemStack> getRespawnItems() {return this.respawnItems; }

    public boolean isTeleporting() {return this.isTeleporting;}

    public void setTeleporting(boolean isTeleporting) { this.isTeleporting = isTeleporting; }

    public boolean isUnderMilk() {return this.isUnderMilk;}

    public void setUnderMilk(boolean isUnderMilk) { this.isUnderMilk = isUnderMilk; }

    public void increaseKilledPlayers(){
        this.killedPlayers++;
        plugin.getSidebar().changeKilled(this);
    }

    public int getKilledPlayers() {return this.killedPlayers;}

    public LastDamager getLastDamager() {return lastDamager;}
    
    public void increaseBrokenBeds(){
        this.brokenBeds++;
        plugin.getSidebar().changeBrokenBeds(this);
    }

    public int getBrokenBeds(){return this.brokenBeds;}

    public boolean takeItem(Material mat, int amount){
        PlayerInventory inv = getPlayer().getInventory();
        if(!inv.contains(mat)) return false;
        int amountInInv = 0;

        for(ItemStack stack : inv.all(mat).values()){
            amountInInv += stack.getAmount();
        }

        if(amountInInv < amount) return false;

        for(int index : inv.all(mat).keySet()){
            ItemStack item = inv.getItem(index);
            int iAmount = item.getAmount();
            if(amount >= iAmount){
                amount -= iAmount;
                inv.clear(index);
            } else {
                item.setAmount(iAmount - amount);
                break;
            }

            if(amount == 0) break;
        }

        return true;
    }

    public void giveItem(ItemStack item){

        Material mat = item.getType();
        PlayerInventory inv = player.getInventory();

        Map<Material, Integer> toolEnchLvl = Map.of(
                Material.WOODEN_AXE, 1,
                Material.WOODEN_PICKAXE, 1,
                Material.STONE_AXE, 1,
                Material.STONE_PICKAXE, 2,
                Material.IRON_AXE, 2,
                Material.IRON_PICKAXE, 2,
                Material.DIAMOND_AXE, 3,
                Material.DIAMOND_PICKAXE, 3
        );

        ItemMeta meta = item.getItemMeta();
        ShopItem shopItem = ShopItem.getShopItem(meta.getDisplayName());
        int lvl = 0;
        Enchantment ench = null;

        if (ShopItems.isArmor(mat)) {
            lvl = team.getTeamUpgrades().get("Protection");
            ench = Enchantment.PROTECTION_ENVIRONMENTAL;
        } else if (ShopItems.isSword(mat)) {
            lvl = team.getTeamUpgrades().get("Sharpness");
            ench = Enchantment.DAMAGE_ALL;
        } else if (ShopItems.isTool(mat)) {
            lvl = team.getTeamUpgrades().get("Haste") + toolEnchLvl.get(mat);
            ench = Enchantment.DIG_SPEED;
            LinkedList<ShopItem> list = ShopItems.getList(ShopItems.TOOLS, shopItem);
            if (list.getFirst() != shopItem) {
                int index = list.indexOf(shopItem) - 1;
                PlayerInv.removeShopItem(inv, list.get(index), 1);
            }
        }

        if (ench != null && lvl != 0) {
            meta.addEnchant(ench, lvl, true);
        }

        item.setItemMeta(meta);

        if(ShopItems.isArmor(item.getType())) {
            if (mat.name().matches(".+BOOTS") && mat != Material.LEATHER_BOOTS) {
                ItemStack leggings = new ItemStack(Material.valueOf(mat.name().split("_")[0] + "_LEGGINGS"));
                ItemMeta leggingsMeta = leggings.getItemMeta();
                if (lvl != 0) {
                    leggingsMeta.addEnchant(ench, lvl, true);
                }
                leggings.setItemMeta(leggingsMeta);
                Utils.setArmor(inv, leggings);
            }
            Utils.setArmor(inv, item);
        } else inv.addItem(item);
    }
    
    public boolean isInvisible() {
    	return showTask != null;
    }
    
    public void hideArmor() {
    	Player bukkitPlayer = getPlayer();
    	if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
    		return;
    	}
    	if (isInvisible()) {
    		showTask.cancel();
    	}
        hiddenArmor = bukkitPlayer.getInventory().getArmorContents();
		bukkitPlayer.getInventory().setArmorContents(null);
		showTask = new BukkitRunnable() {
			@Override
			public void run() {
				if (bukkitPlayer.isOnline()) {
					bukkitPlayer.getInventory().setArmorContents(hiddenArmor);
				}
				showTask = null;
			}
		}.runTaskLater(getPlugin(), 600);
    }
    
    public void showArmor() {
    	Player bukkitPlayer = getPlayer();
    	if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
    		return;
    	}
    	if (isInvisible()) {
    		showTask.cancel();
    		bukkitPlayer.getInventory().setArmorContents(hiddenArmor);
    	}
    }

    public void destroy() {
        setTeam(null);
        getPlayer().getInventory().clear();
        clearPotionEffects();
        if (isInvisible()) showArmor();
        plugin.getPlayers().remove(getPlayer().getUniqueId());
    }

    public void clearPotionEffects() {
        Collection<PotionEffect> effects = getPlayer().getActivePotionEffects();
        for (PotionEffect effect : effects) {
            getPlayer().removePotionEffect(effect.getType());
        }
    }

    public SimpleInventory getShopInventory(ShopItem icon) {
        int index = 0;
        for (Map.Entry<Integer, ShopItem> entry : SimpleInventory.SHOPS.entrySet()) {
            if(entry.getValue() == icon) {
                index = entry.getKey();
                break;
            }
        }
        return inventories.get(index);
    }
}
