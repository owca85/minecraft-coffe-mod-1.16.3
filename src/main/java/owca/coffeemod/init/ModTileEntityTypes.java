package owca.coffeemod.init;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import owca.coffeemod.CoffeeMod;
import owca.coffeemod.tileentity.CoffeeMachineTileEntity;
import owca.coffeemod.tileentity.DeluxeCoffeeMachineTileEntity;

public class ModTileEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, CoffeeMod.MOD_ID);

    public static final RegistryObject<TileEntityType<CoffeeMachineTileEntity>> COFFEE_MACHINE = TILE_ENTITY_TYPES.register("coffee_machine", () -> TileEntityType.Builder.create(CoffeeMachineTileEntity::new, BlockInit.COFFEE_MACHINE.get()).build(null));
    public static final RegistryObject<TileEntityType<DeluxeCoffeeMachineTileEntity>> DELUXE_COFFEE_MACHINE = TILE_ENTITY_TYPES.register("deluxe_coffee_machine", () -> TileEntityType.Builder.create(DeluxeCoffeeMachineTileEntity::new, BlockInit.DELUXE_COFFEE_MACHINE.get()).build(null));
}
