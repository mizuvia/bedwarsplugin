package game;

import inventories.*;
import loading.Sidebar;
import main.PlayerManager;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
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
import util.Utils;

import java.util.*;

public class Participant {

    private final Player player;
    private final Plugin plugin;
    private Team team;
    private String group;
    private final ShopInventory shop = new ShopInventory(new Shop(this), 54, "Магазин");
    private final ArmorInventory armor = new ArmorInventory(new Armor(this), 54, "Броня");
    private final ToolsInventory tools = new ToolsInventory(new Tools(this), 54, "Инструменты");
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
        this.clearParticles();
        player.getEnderChest().clear();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.sidebarObjective = scoreboard.registerNewObjective("sidebar", "dummy", Sidebar.SIDEBAR_NAME);
        this.lastDamager = new LastDamager();
        this.getPlayer().setScoreboard(scoreboard);
        plugin.getSidebar().fillPlayerSidebars(this);
        plugin.getTab().createTab(this.scoreboard);
        plugin.getTab().addPlayerToTabs(this);
        plugin.getPlayers().put(player.getUniqueId(), this);
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

    public ArmorInventory getArmorInventory(){ return this.armor; }

    public ToolsInventory getToolsInventory(){ return this.tools; }

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
        if(!this.getPlayer().getInventory().contains(mat)) return false;
        int amountInInv = 0;

        for(ItemStack stack : this.getPlayer().getInventory().all(mat).values()){
            amountInInv += stack.getAmount();
        }

        if(amountInInv < amount) return false;

        for(Integer index : this.getPlayer().getInventory().all(mat).keySet()){
            if(amount >= this.getPlayer().getInventory().getItem(index).getAmount()){
                amount -= this.getPlayer().getInventory().getItem(index).getAmount();
                this.getPlayer().getInventory().clear(index);
            }
            else {
                this.getPlayer().getInventory().getItem(index).setAmount(this.getPlayer().getInventory().getItem(index).getAmount() - amount);
                break;
            }

            if(amount == 0) break;
        }

        return true;
    }

    public void giveItem(ItemStack item){

        ItemMeta meta = item.getItemMeta();

        int slvl = this.getTeam().getTeamUpgrades().get("Sharpness");
        int elvl = this.getTeam().getTeamUpgrades().get("Haste");
        int plvl = this.getTeam().getTeamUpgrades().get("Protection");

        Map<Material, Material> secondItem = Map.ofEntries(
                Map.entry(Material.STONE_AXE, Material.WOODEN_AXE),
                Map.entry(Material.IRON_AXE, Material.STONE_AXE),
                Map.entry(Material.DIAMOND_AXE, Material.IRON_AXE),
                Map.entry(Material.GOLDEN_AXE, Material.DIAMOND_AXE),
                Map.entry(Material.STONE_PICKAXE, Material.WOODEN_PICKAXE),
                Map.entry(Material.IRON_PICKAXE, Material.STONE_PICKAXE),
                Map.entry(Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE),
                Map.entry(Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE),
                Map.entry(Material.GOLDEN_BOOTS, Material.GOLDEN_LEGGINGS),
                Map.entry(Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_LEGGINGS),
                Map.entry(Material.IRON_BOOTS, Material.IRON_LEGGINGS),
                Map.entry(Material.DIAMOND_BOOTS, Material.DIAMOND_LEGGINGS)
        );

        Map<Material, Integer> enchantmentLvl = Map.of(
                Material.WOODEN_AXE, 1,
                Material.WOODEN_PICKAXE, 1,
                Material.STONE_AXE, 2,
                Material.STONE_PICKAXE, 2,
                Material.IRON_AXE, 3,
                Material.IRON_PICKAXE, 3,
                Material.DIAMOND_AXE, 4,
                Material.DIAMOND_PICKAXE, 4,
                Material.GOLDEN_AXE, 5,
                Material.GOLDEN_PICKAXE, 5
        );

        PlayerInventory inv = player.getInventory();

        switch (item.getType()) {
            case LEATHER_CHESTPLATE, LEATHER_HELMET, LEATHER_BOOTS, LEATHER_LEGGINGS -> {
                Utils.setArmor(inv, item);
                return;
            }
            case GOLDEN_BOOTS, CHAINMAIL_BOOTS, IRON_BOOTS, DIAMOND_BOOTS -> {
                ItemStack leggings = new ItemStack(secondItem.get(item.getType()));
                ItemMeta leggingsMeta = leggings.getItemMeta();
                if (plvl != 0) {
                    leggingsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, plvl, true);
                    meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, plvl, true);
                }
                leggings.setItemMeta(leggingsMeta);
                item.setItemMeta(meta);
                Utils.setArmor(inv, item);
                Utils.setArmor(inv, leggings);
                return;
            }
            case WOODEN_AXE, WOODEN_PICKAXE -> meta.addEnchant(Enchantment.DIG_SPEED, 1 + elvl, true);
            case STONE_AXE, STONE_PICKAXE, IRON_AXE, IRON_PICKAXE, DIAMOND_AXE, DIAMOND_PICKAXE, GOLDEN_AXE, GOLDEN_PICKAXE -> {
                if (inv.first(secondItem.get(item.getType())) != -1)
                    inv.clear(inv.first(secondItem.get(item.getType())));
                meta.addEnchant(Enchantment.DIG_SPEED, enchantmentLvl.get(item.getType()) + elvl, true);
            }
            case WOODEN_SWORD, STONE_SWORD, IRON_SWORD, DIAMOND_SWORD -> {
                if (slvl != 0)
                    meta.addEnchant(Enchantment.DAMAGE_ALL, slvl, true);
            }
        }

        item.setItemMeta(meta);

        inv.addItem(item);
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
        this.setTeam(null);
        if (isInvisible()) showArmor();
        plugin.getPlayers().remove(getPlayer().getUniqueId());
    }

    public void clearParticles() {
        Collection<PotionEffect> effects = getPlayer().getActivePotionEffects();
        for (PotionEffect effect : effects) {
            getPlayer().removePotionEffect(effect.getType());
        }
    }
}
