package owca.coffeemod.tileentity;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import owca.coffeemod.init.ItemInit;

import java.util.Optional;
import java.util.stream.Stream;

public enum Ingredient {
    COFFEE(ItemInit.ROASTED_COFFEE_BEANS.get(), 1, new ResourceLocation("coffeemod:textures/items/roasted_coffee_beans.png")),
    WATER(Items.WATER_BUCKET, 10, new ResourceLocation("textures/item/water_bucket.png")),
    MILK(Items.MILK_BUCKET, 10, new ResourceLocation("textures/item/milk_bucket.png")),
    SUGAR(Items.SUGAR, 1, new ResourceLocation("textures/item/sugar.png")),
    COCOA(Items.COCOA_BEANS, 1, new ResourceLocation("textures/item/cocoa_beans.png")),
    SNOW(Items.SNOWBALL,1, new ResourceLocation("textures/item/snowball.png")),
    CARAMEL(ItemInit.CARAMEL.get(), 1, new ResourceLocation("coffeemod:textures/items/caramel.png"));

    private final Item item;
    private final int itemValue;
    private final ResourceLocation icon;

    Ingredient(Item item, int itemValue, ResourceLocation icon) {
        this.item = item;
        this.itemValue = itemValue;
        this.icon = icon;
    }

    public static Optional<Ingredient> findByItem(Item item){
        return Stream.of(values()).filter(i -> i.item == item).findFirst();
    }

    public Item getItem() {
        return item;
    }

    public int getItemValue()
    {
        return itemValue;
    }

    public ResourceLocation getIcon() {
        return icon;
    }
}
