package owca.coffeemod.objects.blocks;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.blockplacer.BlockPlacer;
import net.minecraft.world.gen.blockplacer.BlockPlacerType;
import net.minecraft.world.server.ServerWorld;
import owca.coffeemod.init.BlockInit;
import owca.coffeemod.init.BlockPlacerTypeInit;

import java.util.List;
import java.util.Random;

public class CoffeeBushBlock extends CropsBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private static final List<Block> ALLOWED_SURFACES = ImmutableList.of(Blocks.FARMLAND, Blocks.GRASS_BLOCK);

    public CoffeeBushBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected IItemProvider getSeedsItem() {
        return BlockInit.COFFEE_SEED.get();
    }

    @Override
    protected int getBonemealAgeIncrease(World worldIn) {
        return MathHelper.nextInt(worldIn.rand, 1, 2);
    }

    @Override
    public void grow(World worldIn, BlockPos pos, BlockState state) {
        grow(worldIn, pos, state, getBonemealAgeIncrease(worldIn));
    }

    public void grow(World worldIn, BlockPos pos, BlockState state, int amount) {
        BlockPos lowerHalfPos = getLowerHalf(worldIn, state, pos);
        int currentAge = getAge(worldIn.getBlockState(lowerHalfPos));
        int newAge = Math.min(currentAge + amount, getMaxAge());

        worldIn.setBlockState(lowerHalfPos, this.getDefaultState().with(AGE, newAge).with(HALF, DoubleBlockHalf.LOWER), 3);
        if (newAge >= 4) {
            worldIn.setBlockState(lowerHalfPos.up(), this.getDefaultState().with(AGE, newAge).with(HALF, DoubleBlockHalf.UPPER), 3);
        }
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos lowerHalfPos = getLowerHalf(worldIn, state, pos);
        return ALLOWED_SURFACES.contains(worldIn.getBlockState(lowerHalfPos.down()).getBlock()) && (worldIn.getLightSubtracted(lowerHalfPos, 0) >= 8 || worldIn.canSeeSky(pos));
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        DoubleBlockHalf doubleblockhalf = state.get(HALF);
        BlockPos blockpos = doubleblockhalf == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
        BlockState blockstate = worldIn.getBlockState(blockpos);
        if (blockstate.getBlock() == this && blockstate.get(HALF) != doubleblockhalf) {
            worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 35);
            worldIn.playEvent(player, 2001, blockpos, Block.getStateId(blockstate));
            if (!worldIn.isRemote && !player.isCreative() && state.get(AGE) == getMaxAge()) { //TODO && age == 7
                spawnDrops(blockstate, worldIn, blockpos, (TileEntity) null, player, player.getHeldItemMainhand());
            }
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (getAge(state) == getMaxAge() && !worldIn.isRemote) {
            spawnDrops(state, worldIn, pos, (TileEntity) null, player, player.getHeldItemMainhand());
            grow(worldIn, pos, state, -1);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    private BlockPos getLowerHalf(IWorldReader worldIn, BlockState state, BlockPos pos) {
        DoubleBlockHalf half = state.get(HALF);
        if (half == DoubleBlockHalf.LOWER) {
            return pos;
        } else {
            BlockPos lower = pos.down();
            if (worldIn.getBlockState(lower).getBlock() == this) {
                return lower;
            } else {
                return pos; //if lower half no longer exists then return upper half
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        Thread.dumpStack();
        return super.getDrops(state, builder);
    }

    @Override
    public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
        this.grow(worldIn, pos, state);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(HALF);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!worldIn.isAreaLoaded(pos, 1))
            return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (worldIn.getLightSubtracted(pos, 0) >= 9) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                float f = getGrowthChance(this, worldIn, pos);
                if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int) (25.0F / f) + 1) == 0)) {
                    grow(worldIn, pos, state, 1);
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
                }
            }
        }
    }

    public BlockState getMaxState() {
        return getDefaultState().with(AGE, getMaxAge()).with(HALF, DoubleBlockHalf.LOWER);
    }

    public void placeAt(IWorld worldIn, BlockPos pos, int age, int flags) {
        worldIn.setBlockState(pos, this.getDefaultState().with(HALF, DoubleBlockHalf.LOWER).with(AGE, age), flags);
        worldIn.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER).with(AGE, age), flags);
    }

    public static class CoffeeBushBlockPlacer extends BlockPlacer {

        public static final Codec<CoffeeBushBlockPlacer> codec;
        public static final CoffeeBushBlockPlacer placer = new CoffeeBushBlockPlacer();

        @Override
        public void func_225567_a_(IWorld world, BlockPos pos, BlockState state, Random rand) {
            if (rand.nextInt(10) < 4) {
                CoffeeBushBlock coffeeBlock = (CoffeeBushBlock) state.getBlock();
                coffeeBlock.placeAt(world, pos, coffeeBlock.getMaxAge(), 2);
            }
        }

        @Override
        protected BlockPlacerType<?> func_230368_a_() {
            return BlockPlacerTypeInit.COFFEE_BUSH_PLACER.get();
        }

        static {
            codec = Codec.unit(() -> placer);
        }
    }
}
