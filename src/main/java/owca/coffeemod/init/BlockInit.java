package owca.coffeemod.init;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import owca.coffeemod.CoffeeMod;
import owca.coffeemod.objects.blocks.CoffeeBushBlock;
import owca.coffeemod.objects.blocks.CoffeeMachineBlock;
import owca.coffeemod.objects.blocks.DeluxeCoffeeMachineBlock;

public class BlockInit {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CoffeeMod.MOD_ID);

    public static final RegistryObject<Block> COFFEE_BUSH = BLOCKS.register("coffee_bush", () -> new CoffeeBushBlock(Block.Properties.create(Material.PLANTS)
            .doesNotBlockMovement()
            .hardnessAndResistance(2, 2)
            .tickRandomly()
            .sound(SoundType.CROP)));

    public static final RegistryObject<Block> COFFEE_MACHINE = BLOCKS.register("coffee_machine", () -> new CoffeeMachineBlock(Block.Properties
            .create(Material.IRON)
            .sound(SoundType.METAL)
            .hardnessAndResistance(2f, 15)
            .harvestTool(ToolType.PICKAXE) //needed for loot_tables to work
            .harvestLevel(0) //needed for loot_tables to work
    ));

    public static final RegistryObject<Block> DELUXE_COFFEE_MACHINE = BLOCKS.register("deluxe_coffee_machine", () -> new DeluxeCoffeeMachineBlock(Block.Properties
            .create(Material.IRON)
            .sound(SoundType.METAL)
            .hardnessAndResistance(2f, 15)
            .harvestTool(ToolType.PICKAXE) //needed for loot_tables to work
            .harvestLevel(0) //needed for loot_tables to work
    ));

    public static final RegistryObject<Item> COFFEE_MACHINE_ITEM = ItemInit.ITEMS.register("coffee_machine", () -> new BlockItem(COFFEE_MACHINE.get(), new Item.Properties()
            .maxStackSize(20)
            .group(ItemGroup.BREWING)));

    public static final RegistryObject<Item> DELUXE_COFFEE_MACHINE_ITEM = ItemInit.ITEMS.register("deluxe_coffee_machine", () -> new BlockItem(DELUXE_COFFEE_MACHINE.get(), new Item.Properties()
            .maxStackSize(20)
            .group(ItemGroup.BREWING)));

    public static final RegistryObject<Item> COFFEE_SEED = ItemInit.ITEMS.register("coffee_seed", () -> new BlockItem(COFFEE_BUSH.get(), new Item.Properties().group(ItemGroup.FOOD)));
}
