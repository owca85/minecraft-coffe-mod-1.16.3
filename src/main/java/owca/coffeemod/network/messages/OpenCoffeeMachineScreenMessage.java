package owca.coffeemod.network.messages;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import owca.coffeemod.gui.DeluxeCoffeeMachineScreen;
import owca.coffeemod.tileentity.Ingredient;
import owca.coffeemod.util.NbtUtil;

import java.util.Map;
import java.util.function.Supplier;

public class OpenCoffeeMachineScreenMessage {

    private final BlockPos pos;
    private final Map<Ingredient, Integer> ingredients;

    public OpenCoffeeMachineScreenMessage(BlockPos pos, Map<Ingredient, Integer> ingredients) {
        this.pos = pos;
        this.ingredients = ingredients;
    }

    public static OpenCoffeeMachineScreenMessage readMessageData(PacketBuffer buf) {
        BlockPos blockPos = buf.readBlockPos();
        Map<Ingredient, Integer> ingredients = NbtUtil.stringToIngredients(buf.readString(32767));
        return new OpenCoffeeMachineScreenMessage(blockPos, ingredients);
    }

    public void writeMessageData(PacketBuffer buf) {
        buf.writeBlockPos(this.pos);
        buf.writeString(NbtUtil.ingredientsToString(ingredients));
    }

    public static void handlePacket(OpenCoffeeMachineScreenMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> displayScreen(message));
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void displayScreen(OpenCoffeeMachineScreenMessage message) {
        Minecraft.getInstance().displayGuiScreen(new DeluxeCoffeeMachineScreen(message.pos, message.ingredients));
    }
}
