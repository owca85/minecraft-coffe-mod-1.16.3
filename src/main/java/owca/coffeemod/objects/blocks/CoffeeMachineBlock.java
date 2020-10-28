package owca.coffeemod.objects.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import owca.coffeemod.init.ItemInit;
import owca.coffeemod.init.ModTileEntityTypes;
import owca.coffeemod.tileentity.CoffeeMachineTileEntity;

import javax.annotation.Nullable;
import java.util.Objects;

public class CoffeeMachineBlock extends Block {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final EnumProperty<State> STATE = EnumProperty.create("state",  State.class);

    private static final VoxelShape SHAPE_NS = Block.makeCuboidShape(1, 0, 1, 15, 16, 15);
    private static final VoxelShape SHAPE_EW = Block.makeCuboidShape(1, 0, 1, 15, 16, 15);

    public CoffeeMachineBlock(Properties properties) {
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
        return ModTileEntityTypes.COFFEE_MACHINE.get().create();
    }

    //bounding box
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.get(FACING)) {
            case NORTH:
                return SHAPE_NS;
            case SOUTH:
                return SHAPE_NS;
            case EAST:
                return SHAPE_EW;
            case WEST:
                return SHAPE_EW;
            default:
                return SHAPE_EW;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite()); //face towards player
    }

    //just for compatibility with other mods
    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        return state.with(FACING, direction.rotate(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(STATE);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack heldItemMainhand = player.getHeldItemMainhand();
        if (!worldIn.isRemote && canMakeCoffee(worldIn, player, pos)) {
//            worldIn.setBlockState(pos, state.with(STATE, State.WORKING));
            Objects.requireNonNull(getCoffeeMachineTileEntity(worldIn, pos)).startMakingCoffee();
            heldItemMainhand.shrink(1);
        }
        return ActionResultType.SUCCESS;
    }

    private boolean canMakeCoffee(World worldIn, PlayerEntity player, BlockPos pos) {
        ItemStack heldItemMainhand = player.getHeldItemMainhand();
        if (heldItemMainhand.getItem() == ItemInit.ROASTED_COFFEE_BEANS.get()) {
            CoffeeMachineTileEntity coffeeMachineTileEntity = getCoffeeMachineTileEntity(worldIn, pos);
            return coffeeMachineTileEntity != null && !coffeeMachineTileEntity.isMakingCoffee();
        }
        return false;
    }

    private CoffeeMachineTileEntity getCoffeeMachineTileEntity(World worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof CoffeeMachineTileEntity) {
            return (CoffeeMachineTileEntity) tileEntity;
        }
        return null;
    }

    public enum State implements IStringSerializable {
        IDLE, WORKING;

        public String toString() {
            return this.getString();
        }

        public String getString() {
            return this == IDLE ? "idle" : "working";
        }
    }
}
