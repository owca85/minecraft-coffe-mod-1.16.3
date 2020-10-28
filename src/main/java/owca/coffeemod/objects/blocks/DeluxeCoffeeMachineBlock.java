package owca.coffeemod.objects.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import owca.coffeemod.init.ModTileEntityTypes;
import owca.coffeemod.network.ModPacketHandler;
import owca.coffeemod.network.messages.OpenCoffeeMachineScreenMessage;
import owca.coffeemod.tileentity.DeluxeCoffeeMachineTileEntity;

import javax.annotation.Nullable;

public class DeluxeCoffeeMachineBlock extends CoffeeMachineBlock {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final EnumProperty<State> STATE = EnumProperty.create("state", State.class);

    public DeluxeCoffeeMachineBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH).with(STATE, State.IDLE));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntityTypes.DELUXE_COFFEE_MACHINE.get().create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            DeluxeCoffeeMachineTileEntity tileEntity = getCoffeeMachineTileEntity(worldIn, pos);
            if (tileEntity != null) {
                if (tileEntity.isMakingCoffee()) {
                    return ActionResultType.SUCCESS;
                }
                ModPacketHandler.getInstance().send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new OpenCoffeeMachineScreenMessage(pos, tileEntity.getIngredients()));
            }
        }
        return ActionResultType.SUCCESS;
    }

    private DeluxeCoffeeMachineTileEntity getCoffeeMachineTileEntity(World worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof DeluxeCoffeeMachineTileEntity) {
            return (DeluxeCoffeeMachineTileEntity) tileEntity;
        }
        return null;
    }
}
