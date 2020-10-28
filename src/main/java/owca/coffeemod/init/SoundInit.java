package owca.coffeemod.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import owca.coffeemod.CoffeeMod;

public class SoundInit {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CoffeeMod.MOD_ID);

    public static final RegistryObject<SoundEvent> COFFEE_MACHINE_WORKING = SOUNDS.register("block.coffee_machine.working", () -> new SoundEvent(new ResourceLocation(CoffeeMod.MOD_ID, "block.coffee_machine.working")));

}
