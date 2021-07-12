package inventories;

import game.Participant;
import game.Team;
import main.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Traps implements IGUI{


    private final Team team;

    public Traps(Team team) {this.team = team;}

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem) {
        int price = (int) Math.pow(2, this.getTeam().getTraps().size());
        if(this.getTeam().getPlugin().getPlayers().get(whoClicked.getName()).takeItem(Material.DIAMOND, price)) {
            String name;

            switch (slot){
                case 10: name = "Задержка"; break;
                case 12: name = "Поддержка"; break;
                case 14: name = "Видимость"; break;
                case 16: name = "Усталость"; break;
                default: return;
            }

            Participant p = this.getTeam().getPlugin().getPlayers().get(whoClicked.getName());

            for(Participant participant : this.getTeam().getTeammates().values()){
                participant.getPlayer().sendMessage(PlayerManager.getCodeColor(p) + whoClicked.getName() + " §7купил ловушку §b" + name);
            }

            ItemStack item = this.getTeam().getUpgradesInventory().getItem(16);
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();

            int index = lore.indexOf("§8Стоимость: §b" + price + " алмаз" + (price != 1 ? "а" : ""));
            lore.remove(index);
            lore.add(index, "§8Стоимость: §b" + (price >= 4 ? 4 : price * 2) + " алмаза");

            int place = lore.indexOf("§8[ПУСТО]");
            lore.remove(place);
            lore.add(place, "§b" + name);

            meta.setLore(lore);
            item.setItemMeta(meta);

            this.getTeam().getTraps().add(name);

        } else whoClicked.sendMessage("§cНедостаточно средств.");
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return this.team.getTrapsInventory();
    }

    public Team getTeam() {return this.team; }
}
