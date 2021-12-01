package game;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Locale;

public class ItemPrice {
    private Material material;
    private int price;

    public ItemPrice(Material material, int price){
        this.material = material;
        this.price = price;
    }

    public ItemPrice(String str){
        if(str.matches("(.*)жел(.*)")){
            price = Integer.parseInt(str.replace("§8Стоимость: §7", "").split(" ")[0]);
            material = Material.IRON_INGOT;
        }
        if(str.matches("(.*)зол(.*)")){
            price = Integer.parseInt(str.replace("§8Стоимость: §e", "").split(" ")[0]);
            material = Material.GOLD_INGOT;
        }
        if(str.matches("(.*)изум(.*)")){
            price = Integer.parseInt(str.replace("§8Стоимость: §2", "").split(" ")[0]);
            material = Material.EMERALD;
        }
        if(str.matches("(.*)алм(.*)")){
            price = Integer.parseInt(str.replace("§8Стоимость: §b", "").split(" ")[0]);
            material = Material.DIAMOND;
        }
    }

    public String toString(){
        return "§8Стоимость: " + getColor() + price + " " + makeEnding();
    }

    public void setPrice(int price){
        this.price = price;
    }

    public void setMaterial(Material material){
        this.material = material;
    }

    public int getPrice() {
        return price;
    }

    public Material getMaterial() {
        return material;
    }

    private ChatColor getColor(){
        return switch (material){
            case IRON_INGOT -> ChatColor.GRAY;
            case GOLD_INGOT -> ChatColor.YELLOW;
            case DIAMOND -> ChatColor.AQUA;
            case EMERALD -> ChatColor.DARK_GREEN;
            default -> null;
        };
    }

    private String getWord(){
        return switch (material){
            case IRON_INGOT -> "железо";
            case GOLD_INGOT -> "золото";
            case DIAMOND -> "алмаз";
            case EMERALD -> "изумруд";
            default -> null;
        };
    }

    private String makeEnding(){
        String word = getWord();
        char lastLetter = word.toLowerCase(Locale.ROOT).charAt(word.length() - 1);

        String ending = "";
        if(word.equals("железо") && word.equals("золото")){
            word = word.substring(0, word.length() - 2);
            if(price > 10 && price < 20) ending = "а";
            else {
                if(price % 10 < 5 && price % 10 > 1) ending = "а";
                if(price % 10 == 0 || price % 10 > 4) ending = "а";
                if(price % 10 == 1) ending = "о";
            }
        } else if(word.equals("изумруд") && word.equals("алмаз")){
            if(price > 10 && price < 20) ending = "ов";
            else {
                if(price % 10 < 5 && price % 10 > 1) ending = "ов";
                if(price % 10 == 0 || price % 10 > 4) ending = "а";
                if(price % 10 == 1) ending = "";
            }
        }
        return word.concat(ending);
    }
}
