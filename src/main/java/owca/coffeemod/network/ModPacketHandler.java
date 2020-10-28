package owca.coffeemod.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import owca.coffeemod.CoffeeMod;
import owca.coffeemod.network.messages.AddIngredientMessage;
import owca.coffeemod.network.messages.MakeCoffeeMessage;
import owca.coffeemod.network.messages.OpenCoffeeMachineScreenMessage;

public class ModPacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    private static SimpleChannel INSTANCE;

    public static SimpleChannel getInstance() {
        int messageIndex = 0;
        if (INSTANCE == null) {
            INSTANCE = NetworkRegistry.newSimpleChannel(
                    new ResourceLocation(CoffeeMod.MOD_ID, "main"),
                    () -> PROTOCOL_VERSION,
                    PROTOCOL_VERSION::equals,
                    PROTOCOL_VERSION::equals
            );

            INSTANCE.registerMessage(--messageIndex, OpenCoffeeMachineScreenMessage.class,
                    OpenCoffeeMachineScreenMessage::writeMessageData,
                    OpenCoffeeMachineScreenMessage::readMessageData,
                    OpenCoffeeMachineScreenMessage::handlePacket);

            INSTANCE.registerMessage(--messageIndex, MakeCoffeeMessage.class,
                    MakeCoffeeMessage::writeMessageData,
                    MakeCoffeeMessage::readMessageData,
                    MakeCoffeeMessage::handlePacket);

            INSTANCE.registerMessage(--messageIndex, AddIngredientMessage.class,
                    AddIngredientMessage::writeMessageData,
                    AddIngredientMessage::readMessageData,
                    AddIngredientMessage::handlePacket);
        }
        return INSTANCE;
    }
}
