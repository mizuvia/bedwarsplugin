package game;

public class LevelInfo {
    private final ItemPrice itemPrice;
    private final int enchantmentLevel;

    public LevelInfo(ItemPrice itemPrice, int level){
        this.itemPrice = itemPrice;
        this.enchantmentLevel = level;
    }

    public int getEnchantmentLevel() {
        return enchantmentLevel;
    }

    public ItemPrice getItemPrice() {
        return itemPrice;
    }
}
