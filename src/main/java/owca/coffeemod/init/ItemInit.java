package owca.coffeemod.init;

import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import owca.coffeemod.CoffeeMod;
import owca.coffeemod.objects.items.CoffeeItem;

public class ItemInit {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CoffeeMod.MOD_ID);

    public static final RegistryObject<Item> ROASTED_COFFEE_BEANS = ItemInit.ITEMS.register("roasted_coffee_beans", () -> new Item(new Item.Properties().group(ItemGroup.BREWING)));
    public static final RegistryObject<Item> COFFEE = ITEMS.register("coffee", () -> new CoffeeItem(new Item.Properties()
            .maxStackSize(1)
            .group(ItemGroup.BREWING)
            .food(new Food.Builder()
                    .effect(new EffectInstance(Effects.SPEED, 20*60, 1, false,false), 1)
                    .effect(new EffectInstance(Effects.HASTE, 20*60, 0, false, false), 1)
                    .build())
    ));

    //deluxe coffee machine

    public static final RegistryObject<Item> ESPRESSO = ITEMS.register("espresso", () -> new CoffeeItem(new Item.Properties()
            .maxStackSize(1)
            .group(ItemGroup.BREWING)
            .food(new Food.Builder()
                    .effect(new EffectInstance(Effects.SPEED, 20*60, 1, false,false), 1)
                    .effect(new EffectInstance(Effects.HASTE, 20*60, 0, false, false), 1)
                    .build())
    ));

    public static final RegistryObject<Item> LATTE = ITEMS.register("latte", () -> new CoffeeItem(new Item.Properties()
            .maxStackSize(1)
            .group(ItemGroup.BREWING)
            .food(new Food.Builder()
                    .effect(new EffectInstance(Effects.SPEED, 20*60, 1, false,false), 1)
                    .effect(new EffectInstance(Effects.STRENGTH, 20*60, 0, false, false), 1)
                    .build())
    ));

    public static final RegistryObject<Item> CARAMEL_MACCHIATO = ITEMS.register("caramel_macchiato", () -> new CoffeeItem(new Item.Properties()
            .maxStackSize(1)
            .group(ItemGroup.BREWING)
            .food(new Food.Builder()
                    .effect(new EffectInstance(Effects.SPEED, 20*60, 1, false,false), 1)
                    .effect(new EffectInstance(Effects.LUCK, 20*60, 0, false, false), 1)
                    .build())
    ));

    public static final RegistryObject<Item> MOCHA = ITEMS.register("mocha", () -> new CoffeeItem(new Item.Properties()
            .maxStackSize(1)
            .group(ItemGroup.BREWING)
            .food(new Food.Builder()
                    .effect(new EffectInstance(Effects.SPEED, 20*60, 1, false,false), 1)
                    .effect(new EffectInstance(Effects.JUMP_BOOST, 20*60, 0, false, false), 1)
                    .build())
    ));

    public static final RegistryObject<Item> FRAPPE = ITEMS.register("frappe", () -> new CoffeeItem(new Item.Properties()
            .maxStackSize(1)
            .group(ItemGroup.BREWING)
            .food(new Food.Builder()
                    .effect(new EffectInstance(Effects.SPEED, 20*60, 1, false,false), 1)
                    .effect(new EffectInstance(Effects.FIRE_RESISTANCE, 20*30, 0, false, false), 1)
                    .build())
    ));

    public static final RegistryObject<Item> COCOA_DRINK = ITEMS.register("cocoa_drink", () -> new CoffeeItem(new Item.Properties()
            .maxStackSize(1)
            .group(ItemGroup.BREWING)
            .food(new Food.Builder()
                    .effect(new EffectInstance(Effects.JUMP_BOOST, 20*60, 1, false,false), 1)
                    .build())
    ));

    public static final RegistryObject<Item> CARAMEL = ITEMS.register("caramel", () -> new Item(new Item.Properties()
            .maxStackSize(64)
            .group(ItemGroup.BREWING)
    ));
}
