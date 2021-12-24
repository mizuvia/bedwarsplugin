package game;

import inventories.*;
import loading.PlayerSidebar;
import main.PlayerManager;
import main.Plugin;
import util.LastDamager;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

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
    private PlayerSidebar sidebar;
    private BukkitTask showTask;
    private ItemStack[] hidenArmor;
    private LastDamager lastDamager;
    
    public Participant(Player player, Plugin plugin){
        this.player = player;
        this.plugin = plugin;
        this.setGroup();
        this.sidebar = new PlayerSidebar(plugin, player.getUniqueId());
        this.lastDamager = new LastDamager();
    }

    public void increaseFinalKills() {
        this.finalKills++;
    }

    public PlayerSidebar getSidebar() {
    	return sidebar;
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
    	if (this.team != null) {
    		plugin.getPlayers().values().forEach(partic -> partic.getSidebar().leaveTeam(getPlayer().getName(), this.team));
    	}
        this.team = team;
        plugin.getPlayers().values().forEach(partic -> partic.getSidebar().joinTeam(getPlayer().getName(), team));
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
//        this.getPlugin().getSidebar().changeKilled(this);
    }

    public int getKilledPlayers() {return this.killedPlayers;}

    public LastDamager getLastDamager() {return lastDamager;}
    
    public void increaseBrokenBeds(){
        this.brokenBeds++;
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

        switch (item.getType()) {
            case LEATHER_CHESTPLATE:
                this.getPlayer().getInventory().setChestplate(item);
                return;
            case LEATHER_HELMET:
                this.getPlayer().getInventory().setHelmet(item);
                return;
            case LEATHER_LEGGINGS:
                this.getPlayer().getInventory().setLeggings(item);
                return;
            case LEATHER_BOOTS:
                this.getPlayer().getInventory().setBoots(item);
                return;
            case GOLDEN_BOOTS:
                ItemStack golden_leggings = new ItemStack(Material.GOLDEN_LEGGINGS, 1);
                ItemMeta gl_meta = golden_leggings.getItemMeta();
                if(plvl != 0) gl_meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, plvl, true);
                golden_leggings.setItemMeta(gl_meta);
                if(plvl != 0) meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, plvl, true);
                item.setItemMeta(meta);
                this.getPlayer().getInventory().setBoots(item);
                this.getPlayer().getInventory().setLeggings(golden_leggings);
                return;
            case CHAINMAIL_BOOTS:
                ItemStack chainmain_leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
                ItemMeta cl_meta = chainmain_leggings.getItemMeta();
                if(plvl != 0) cl_meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, plvl, true);
                chainmain_leggings.setItemMeta(cl_meta);
                if(plvl != 0) meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, plvl, true);
                item.setItemMeta(meta);
                this.getPlayer().getInventory().setBoots(item);
                this.getPlayer().getInventory().setLeggings(chainmain_leggings);
                return;
            case IRON_BOOTS:
                ItemStack iron_leggings = new ItemStack(Material.IRON_LEGGINGS, 1);
                ItemMeta il_meta = iron_leggings.getItemMeta();
                if(plvl != 0) il_meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, plvl, true);
                iron_leggings.setItemMeta(il_meta);
                if(plvl != 0) meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, plvl, true);
                item.setItemMeta(meta);
                this.getPlayer().getInventory().setBoots(item);
                this.getPlayer().getInventory().setLeggings(iron_leggings);
                return;
            case DIAMOND_BOOTS:
                ItemStack diamond_leggings = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
                ItemMeta dl_meta = diamond_leggings.getItemMeta();
                if(plvl != 0) dl_meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, plvl, true);
                diamond_leggings.setItemMeta(dl_meta);
                if(plvl != 0) meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, plvl, true);
                item.setItemMeta(meta);
                this.getPlayer().getInventory().setBoots(item);
                this.getPlayer().getInventory().setLeggings(diamond_leggings);
                return;
            case WOODEN_AXE:
            case WOODEN_PICKAXE:
                meta.addEnchant(Enchantment.DIG_SPEED, 1 + elvl, true);
                break;
            case STONE_AXE:
                if(this.getPlayer().getInventory().first(Material.WOODEN_AXE) != -1) this.getPlayer().getInventory().clear(this.getPlayer().getInventory().first(Material.WOODEN_AXE));
                meta.addEnchant(Enchantment.DIG_SPEED, 2 + elvl, true);
                break;
            case STONE_PICKAXE:
                if(this.getPlayer().getInventory().first(Material.WOODEN_PICKAXE) != -1) this.getPlayer().getInventory().clear(this.getPlayer().getInventory().first(Material.WOODEN_PICKAXE));
                meta.addEnchant(Enchantment.DIG_SPEED, 2 + elvl, true);
                break;
            case IRON_AXE:
                if(this.getPlayer().getInventory().first(Material.STONE_AXE) != -1) this.getPlayer().getInventory().clear(this.getPlayer().getInventory().first(Material.STONE_AXE));
                meta.addEnchant(Enchantment.DIG_SPEED, 3 + elvl, true);
                break;
            case IRON_PICKAXE:
                if(this.getPlayer().getInventory().first(Material.STONE_PICKAXE) != -1) this.getPlayer().getInventory().clear(this.getPlayer().getInventory().first(Material.STONE_PICKAXE));
                meta.addEnchant(Enchantment.DIG_SPEED, 3 + elvl, true);
                break;
            case DIAMOND_AXE:
                if(this.getPlayer().getInventory().first(Material.IRON_AXE) != -1) this.getPlayer().getInventory().clear(this.getPlayer().getInventory().first(Material.IRON_AXE));
                meta.addEnchant(Enchantment.DIG_SPEED, 4 + elvl, true);
                break;
            case DIAMOND_PICKAXE:
                if(this.getPlayer().getInventory().first(Material.IRON_PICKAXE) != -1) this.getPlayer().getInventory().clear(this.getPlayer().getInventory().first(Material.IRON_PICKAXE));
                meta.addEnchant(Enchantment.DIG_SPEED, 4 + elvl, true);
                break;
            case GOLDEN_AXE:
                if(this.getPlayer().getInventory().first(Material.DIAMOND_AXE) != -1) this.getPlayer().getInventory().clear(this.getPlayer().getInventory().first(Material.DIAMOND_AXE));
                meta.addEnchant(Enchantment.DIG_SPEED, 5 + elvl, true);
                break;
            case GOLDEN_PICKAXE:
                if(this.getPlayer().getInventory().first(Material.DIAMOND_PICKAXE) != -1) this.getPlayer().getInventory().clear(this.getPlayer().getInventory().first(Material.DIAMOND_PICKAXE));
                meta.addEnchant(Enchantment.DIG_SPEED, 5 + elvl, true);
                break;
            case WOODEN_SWORD:
            case STONE_SWORD:
            case IRON_SWORD:
            case DIAMOND_SWORD:
                if(slvl != 0) meta.addEnchant(Enchantment.DAMAGE_ALL, slvl, true);
                break;
            default: break;
        }

        item.setItemMeta(meta);

        this.getPlayer().getInventory().addItem(item);
    }
    
    public boolean inInvis() {
    	return showTask != null;
    }
    
    public void hide() {
    	Player bukkitPlayer = getPlayer();
    	if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
    		return;
    	}
    	if (inInvis()) {
    		showTask.cancel();
    	}
		hidenArmor = bukkitPlayer.getInventory().getArmorContents();
		bukkitPlayer.getInventory().setArmorContents(null);
		showTask = new BukkitRunnable() {
			@Override
			public void run() {
				if (bukkitPlayer.isOnline()) {
					bukkitPlayer.getInventory().setArmorContents(hidenArmor);
				}
				showTask = null;
			}
		}.runTaskLater(getPlugin(), 600);
    }
    
    public void show() {
    	Player bukkitPlayer = getPlayer();
    	if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
    		return;
    	}
    	if (inInvis()) {
    		showTask.cancel();
    		bukkitPlayer.getInventory().setArmorContents(hidenArmor);
    	}
    }
    
}
