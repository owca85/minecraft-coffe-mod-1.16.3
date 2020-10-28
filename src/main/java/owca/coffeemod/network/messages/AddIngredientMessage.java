package owca.coffeemod.network.messages;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import owca.coffeemod.network.ModPacketHandler;
import owca.coffeemod.tileentity.DeluxeCoffeeMachineTileEntity;
import owca.coffeemod.tileentity.Ingredient;

import java.util.function.Supplier;

public class AddIngredientMessage {

    private final BlockPos pos;
    private final Ingredient ingredient;
    private final int amount;

    public AddIngredientMessage(BlockPos pos, Ingredient ingredient, int amount) {
        this.pos = pos;
        this.ingredient = ingredient;
        this.amount = amount;
    }

    public static AddIngredientMessage readMessageData(PacketBuffer buf) {
        return new AddIngredientMessage(buf.readBlockPos(), buf.readEnumValue(Ingredient.class), buf.readInt());
    }

    public void writeMessageData(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeEnumValue(ingredient);
        buf.writeInt(amount);
    }

    public static void handlePacket(AddIngredientMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.get().getSender();// the client that sent this packet
            ServerWorld serverWorld = sender.getServerWorld();
            TileEntity tileEntity = serverWorld.getTileEntity(message.pos);
            if (tileEntity instanceof DeluxeCoffeeMachineTileEntity) {
                DeluxeCoffeeMachineTileEntity deluxeCoffeeMachineTileEntity = (DeluxeCoffeeMachineTileEntity) tileEntity;

                boolean itemFound = false;
                for (int i = 0; i < sender.inventory.getSizeInventory(); ++i) {
                    ItemStack stack = sender.inventory.getStackInSlot(i);
                    if (stack.getItem() == message.ingredient.getItem()) {
                        stack.shrink(1);
                        itemFound = true;
                        break;
                    }
                }

                if (itemFound) {
                    deluxeCoffeeMachineTileEntity.addIngredient(message.ingredient, message.amount);
                    ModPacketHandler.getInstance().send(PacketDistributor.PLAYER.with(() -> sender), new OpenCoffeeMachineScreenMessage(message.pos, deluxeCoffeeMachineTileEntity.getIngredients()));
                }

            }
        });
        ctx.get().setPacketHandled(true);
    }
}
