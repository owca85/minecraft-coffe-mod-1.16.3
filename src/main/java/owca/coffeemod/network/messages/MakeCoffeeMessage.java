package owca.coffeemod.network.messages;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import owca.coffeemod.tileentity.CoffeeType;
import owca.coffeemod.tileentity.DeluxeCoffeeMachineTileEntity;

import java.util.function.Supplier;

public class MakeCoffeeMessage {

    private final BlockPos pos;
    private final CoffeeType coffeeType;

    public MakeCoffeeMessage(BlockPos pos, CoffeeType coffeeType) {
        this.pos = pos;
        this.coffeeType = coffeeType;
    }

    public static MakeCoffeeMessage readMessageData(PacketBuffer buf) {
        return new MakeCoffeeMessage(buf.readBlockPos(), buf.readEnumValue(CoffeeType.class));
    }

    public void writeMessageData(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeEnumValue(coffeeType);
    }

    public static void handlePacket(MakeCoffeeMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.get().getSender();// the client that sent this packet
            ServerWorld serverWorld = sender.getServerWorld();
            TileEntity tileEntity = serverWorld.getTileEntity(message.pos);
            if (tileEntity instanceof DeluxeCoffeeMachineTileEntity) {
                ((DeluxeCoffeeMachineTileEntity) tileEntity).startMakingCoffee(message.coffeeType);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
