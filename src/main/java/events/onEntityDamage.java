package events;

import game.Participant;
import inventories.ShopItems;
import main.Config;
import main.PlayerManager;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import util.PlayerInv;
import util.Utils;

public class onEntityDamage extends SimpleListener implements Listener, EventExecutor {

    public onEntityDamage(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {

        EntityDamageEvent e = (EntityDamageEvent) event;

        if(e.getEntity() instanceof Player){
            if(((Player) e.getEntity()).getHealth() - e.getDamage() <= 0) {
                e.setCancelled(true);
                if(this.getPlugin().isLoading()) {
                    if(this.getPlugin().isLoading()) PlayerInv.setWaitingInventory(this.getPlugin().getPlayers().get(e.getEntity().getName()));
                    e.getEntity().teleport(Utils.centralizeLocation(Bukkit.getWorld("waiting").getSpawnLocation()));
                } else {
                    boolean isFinal = this.getPlugin().getPlayers().get(e.getEntity().getName()).getTeam().isBroken();

                    if (!this.getPlugin().getGame().getPlayersDamagers().containsKey(e.getEntity().getName())) {
                        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                            player.sendMessage(PlayerManager.getCodeColor(this.getPlugin().getPlayers().get(e.getEntity().getName())) + e.getEntity().getName() + "§7 упал в бездну." + " " + (isFinal ? "§b§lФинальное убийство!" : ""));
                        }
                        if (isFinal) {
                            this.getPlugin().getPlayers().get(e.getEntity().getName()).getTeam().getBedDestroyer().increaseFinalKills();
                            this.getPlugin().getPlayers().get(e.getEntity().getName()).getTeam().getBedDestroyer().increaseKilledPlayers();
                        }
                    } else {
                        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                            player.sendMessage(PlayerManager.getCodeColor(this.getPlugin().getPlayers().get(e.getEntity().getName())) + e.getEntity().getName() + "§7 был зверски убит " + PlayerManager.getCodeColor(this.getPlugin().getPlayers().get(this.getPlugin().getGame().getPlayersDamagers().get(e.getEntity().getName()))) + this.getPlugin().getGame().getPlayersDamagers().get(e.getEntity().getName()) + " " + (isFinal ? "§b§lФинальное убийство!" : ""));
                        }
                        if (isFinal)
                            this.getPlugin().getPlayers().get(e.getEntity().getName()).getTeam().getBedDestroyer().increaseFinalKills();
                        this.getPlugin().getPlayers().get(this.getPlugin().getGame().getPlayersDamagers().get(e.getEntity().getName())).increaseKilledPlayers();

                        for (ItemStack item : ((Player) e.getEntity()).getInventory().getContents()) {
                            if(item == null) continue;

                            int amount = item.getAmount();
                            ItemStack resource;
                            ItemMeta meta;

                            switch(item.getType()){
                                case BRICK -> {
                                    resource = new ItemStack(Material.BRICK, amount);
                                    meta = resource.getItemMeta();
                                    meta.setDisplayName("§eБронза");
                                }
                                case IRON_INGOT -> {
                                    resource = new ItemStack(Material.IRON_INGOT, amount);
                                    meta = resource.getItemMeta();
                                    meta.setDisplayName("§eЖелезо");
                                }
                                case GOLD_INGOT -> {
                                    resource = new ItemStack(Material.GOLD_INGOT, amount);
                                    meta = resource.getItemMeta();
                                    meta.setDisplayName("§eЗолото");
                                }
                                case DIAMOND -> {
                                    resource = new ItemStack(Material.DIAMOND, amount);
                                    meta = resource.getItemMeta();
                                    meta.setDisplayName("§eАлмаз");
                                }
                                case EMERALD -> {
                                    resource = new ItemStack(Material.EMERALD, amount);
                                    meta = resource.getItemMeta();
                                    meta.setDisplayName("§eИзумруд");
                                }
                                default -> {
                                    continue;
                                }
                            }

                            resource.setItemMeta(meta);

                            this.getPlugin().getPlayers().get(this.getPlugin().getGame().getPlayersDamagers().get(e.getEntity().getName())).giveItem(resource);
                        }

                        this.getPlugin().getGame().getPlayersDamagers().remove(e.getEntity().getName());
                    }

                    if (isFinal) this.getPlugin().getPlayers().get(e.getEntity().getName()).getTeam().decreaseTeammatesAmount();

                    Participant participant = this.getPlugin().getPlayers().get(e.getEntity().getName());

                    for (ItemStack item : ((Player) e.getEntity()).getInventory().getContents()) {
                        if (item == null) continue;
                        switch(item.getType()){
                            case STONE_PICKAXE -> {
                                participant.getRespawnItems().add(Utils.clearItem(ShopItems.WOODEN_PICKAXE));
                                participant.getToolsInventory().setItem(30, ShopItems.STONE_PICKAXE);
                            }
                            case STONE_AXE -> {
                                participant.getRespawnItems().add(Utils.clearItem(ShopItems.WOODEN_AXE));
                                participant.getToolsInventory().setItem(31, ShopItems.STONE_AXE);
                            }
                            case IRON_PICKAXE -> {
                                participant.getRespawnItems().add(Utils.clearItem(ShopItems.STONE_PICKAXE));
                                participant.getToolsInventory().setItem(30, ShopItems.IRON_PICKAXE);
                            }
                            case IRON_AXE -> {
                                participant.getRespawnItems().add(Utils.clearItem(ShopItems.STONE_AXE));
                                participant.getToolsInventory().setItem(31, ShopItems.IRON_AXE);
                            }
                            case DIAMOND_PICKAXE -> {
                                participant.getRespawnItems().add(Utils.clearItem(ShopItems.IRON_PICKAXE));
                                participant.getToolsInventory().setItem(30, ShopItems.DIAMOND_PICKAXE);
                            }
                            case DIAMOND_AXE -> {
                                participant.getRespawnItems().add(Utils.clearItem(ShopItems.IRON_AXE));
                                participant.getToolsInventory().setItem(31, ShopItems.DIAMOND_AXE);
                            }
                            case GOLDEN_PICKAXE -> {
                                participant.getRespawnItems().add(Utils.clearItem(ShopItems.DIAMOND_PICKAXE));
                                participant.getToolsInventory().setItem(30, ShopItems.GOLDEN_PICKAXE);
                            }
                            case GOLDEN_AXE -> {
                                participant.getRespawnItems().add(Utils.clearItem(ShopItems.DIAMOND_AXE));
                                participant.getToolsInventory().setItem(31, ShopItems.GOLDEN_AXE);
                            }
                            case GOLDEN_BOOTS, CHAINMAIL_BOOTS, IRON_BOOTS, DIAMOND_BOOTS, FISHING_ROD, LEATHER_BOOTS,
                                    LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_HELMET, SHEARS, WOODEN_PICKAXE, WOODEN_AXE -> participant.getRespawnItems().add(item);
                        }
                    }

                    if (this.getPlugin().isWorking()) {
                        ((Player) e.getEntity()).setGameMode(GameMode.SPECTATOR);
                        ((Player) e.getEntity()).getInventory().clear();

                        ((Player) e.getEntity()).setHealth(20.0);

                        if (this.getPlugin().getPlayers().get(e.getEntity().getName()).getTeam().isBroken()) {
                            this.getPlugin().getTab().removePlayer(this.getPlugin().getPlayers().get(e.getEntity().getName()));
                            ((Player) e.getEntity()).setPlayerListName("§7Наблюдатель " + e.getEntity().getName());
                            e.getEntity().teleport(Config.getCenter());
                        } else {
                            e.getEntity().teleport(Config.getCenter());
                            ((Player) e.getEntity()).sendTitle("§cВы возродитесь через 5 секунд", "§7Ожидайте.", 10, 70, 20);

                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getPlugin(), () -> {
                                e.getEntity().teleport(this.getPlugin().getPlayers().get(e.getEntity().getName()).getTeam().getSpawnLocation());
                                PlayerInv.setPlayingInventory(this.getPlugin().getPlayers().get(e.getEntity().getName()));
                                ((Player) e.getEntity()).setGameMode(GameMode.SURVIVAL);
                            }, 100);
                        }
                    }
                }
            }
        }
    }
}
