package owca.coffeemod.init;

import net.minecraft.world.gen.blockplacer.BlockPlacerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import owca.coffeemod.CoffeeMod;
import owca.coffeemod.objects.blocks.CoffeeBushBlock;

public class BlockPlacerTypeInit {

    public static final DeferredRegister<BlockPlacerType<?>> BLOCKS_PLACER_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_PLACER_TYPES, CoffeeMod.MOD_ID);

    public static final RegistryObject<BlockPlacerType<?>> COFFEE_BUSH_PLACER = BLOCKS_PLACER_TYPES.register("coffee_bush_placer", () -> new BlockPlacerType<>(CoffeeBushBlock.CoffeeBushBlockPlacer.codec));

}
