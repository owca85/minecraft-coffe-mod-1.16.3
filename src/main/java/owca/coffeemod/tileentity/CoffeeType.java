package owca.coffeemod.tileentity;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import owca.coffeemod.init.ItemInit;

import java.util.EnumSet;

public enum CoffeeType {
    ESPRESSO(EnumSet.of(Ingredient.WATER, Ingredient.COFFEE), ItemInit.ESPRESSO, new ResourceLocation("coffeemod:textures/items/espresso.png")),
    LATTE(EnumSet.of(Ingredient.WATER, Ingredient.COFFEE, Ingredient.MILK), ItemInit.LATTE, new ResourceLocation("coffeemod:textures/items/latte.png")),
    FRAPPE(EnumSet.of(Ingredient.SNOW, Ingredient.COFFEE,Ingredient.MILK, Ingredient.SUGAR), ItemInit.FRAPPE, new ResourceLocation("coffeemod:textures/items/frappe.png")),
    CARAMEL_MACCHIATO(EnumSet.of(Ingredient.WATER, Ingredient.COFFEE,Ingredient.MILK,Ingredient.CARAMEL), ItemInit.CARAMEL_MACCHIATO, new ResourceLocation("coffeemod:textures/items/caramel_macchiato.png")),
    MOCHA(EnumSet.of(Ingredient.WATER, Ingredient.COFFEE,Ingredient.MILK,Ingredient.COCOA, Ingredient.SUGAR), ItemInit.MOCHA, new ResourceLocation("coffeemod:textures/items/mocha.png")),
    COCOA_DRINK(EnumSet.of(Ingredient.MILK, Ingredient.COCOA, Ingredient.SUGAR), ItemInit.COCOA_DRINK, new ResourceLocation("coffeemod:textures/items/cocoa_drink.png"));

    private final EnumSet<Ingredient> ingredients;
    private final RegistryObject<Item> item;
    private final ResourceLocation icon;

    CoffeeType(EnumSet<Ingredient> ingredients, RegistryObject<Item> item, ResourceLocation icon){
        this.ingredients = ingredients;
        this.item = item;
        this.icon = icon;
    }

    public EnumSet<Ingredient> getIngredients() {
        return ingredients;
    }

    public RegistryObject<Item> getItem() {
        return item;
    }

    public ResourceLocation getIcon() {
        return icon;
    }
}
