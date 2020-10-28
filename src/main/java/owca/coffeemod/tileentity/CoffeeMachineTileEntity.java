package owca.coffeemod.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import owca.coffeemod.init.ItemInit;
import owca.coffeemod.init.ModTileEntityTypes;
import owca.coffeemod.init.SoundInit;
import owca.coffeemod.objects.blocks.CoffeeMachineBlock;

public class CoffeeMachineTileEntity extends TileEntity implements ITickableTileEntity {

    private static final int PREPARATION_TIME = 20*8;
    private int tick;

    public CoffeeMachineTileEntity() {
        super(ModTileEntityTypes.COFFEE_MACHINE.get());
    }

    @Override
    public void tick() {
        if (tick > 0) {
            tick--;
            if (tick == 0) {
                makeCoffee();
            }
        }
    }

    public boolean isMakingCoffee() {
        return tick > 0;
    }

    public void startMakingCoffee() {
        this.world.playSound(null, pos, SoundInit.COFFEE_MACHINE_WORKING.get(), SoundCategory.BLOCKS, 1f, 1f);
        this.world.setBlockState(pos, this.getBlockState().with(CoffeeMachineBlock.STATE, CoffeeMachineBlock.State.WORKING));
        tick = PREPARATION_TIME;
    }

    private void makeCoffee() {
        Item coffee = ItemInit.COFFEE.get();
        Block.spawnAsEntity(this.world, pos.add(0, 1.5, 0), new ItemStack(coffee));
        this.world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundCategory.BLOCKS, 1f, 1f);
        this.world.setBlockState(pos, this.getBlockState().with(CoffeeMachineBlock.STATE, CoffeeMachineBlock.State.IDLE));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("tick", tick);
        return super.write(compound);
    }

    @Override
    public void read(BlockState blockState, CompoundNBT compound) {
        super.read(blockState, compound);
        this.tick = compound.getInt("tick");
    }
}
