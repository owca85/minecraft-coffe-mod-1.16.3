package owca.coffeemod.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import owca.coffeemod.init.ModTileEntityTypes;
import owca.coffeemod.init.SoundInit;
import owca.coffeemod.objects.blocks.DeluxeCoffeeMachineBlock;
import owca.coffeemod.util.NbtUtil;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DeluxeCoffeeMachineTileEntity extends TileEntity implements ITickableTileEntity {

    public static final int MAX_INGREDIENT_AMOUNT = 20;
    private static final int PREPARATION_TIME = 20*8;
    public static final String NBT_INGREDIENTS = "ingredients";
    public static final String NBT_TICK = "tick";
    public static final String NBT_COFFEE_TYPE = "coffeeType";
    private int tick;
    private CoffeeType currentCoffeeType = CoffeeType.ESPRESSO;

    private Map<Ingredient, Integer> ingredients;

    public DeluxeCoffeeMachineTileEntity() {
        super(ModTileEntityTypes.DELUXE_COFFEE_MACHINE.get());
        ingredients = new EnumMap<>(Ingredient.class);
        Stream.of(Ingredient.values()).forEach(i -> ingredients.put(i, 0));
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

    //TODO move to util
    public static boolean hasRequiredIngredients(CoffeeType coffeeType, Map<Ingredient, Integer> ingredients){
        EnumSet<Ingredient> requiredIngredients = coffeeType.getIngredients();
        return requiredIngredients.stream().allMatch(i -> ingredients.get(i) > 0);
    }

    public void startMakingCoffee(CoffeeType coffeeType) {
        if (isMakingCoffee()){
            return;
        }
        EnumSet<Ingredient> requiredIngredients = coffeeType.getIngredients();
        boolean hasRequiredIngredients = requiredIngredients.stream().allMatch(this::hasIngredient);

        if (hasRequiredIngredients) {
            this.currentCoffeeType = coffeeType;
            this.world.playSound(null, pos, SoundInit.COFFEE_MACHINE_WORKING.get(), SoundCategory.BLOCKS, 1f, 1f);
            this.world.setBlockState(pos, this.getBlockState().with(DeluxeCoffeeMachineBlock.STATE, DeluxeCoffeeMachineBlock.State.WORKING));
            tick = PREPARATION_TIME;
            requiredIngredients.forEach(this::useIngredient);
        }
    }

    private void makeCoffee() {
        Item coffee = currentCoffeeType.getItem().get();
        Direction direction = this.world.getBlockState(pos).get(DeluxeCoffeeMachineBlock.FACING);
        Block.spawnAsEntity(this.world, pos.add(direction.getDirectionVec()), new ItemStack(coffee));
        this.world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundCategory.BLOCKS, 1f, 1f);
        this.world.setBlockState(pos, this.getBlockState().with(DeluxeCoffeeMachineBlock.STATE, DeluxeCoffeeMachineBlock.State.IDLE));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt(NBT_TICK, tick);
        compound.putString(NBT_INGREDIENTS, NbtUtil.ingredientsToString(ingredients));
        compound.putString(NBT_COFFEE_TYPE, currentCoffeeType.name());
        return super.write(compound);
    }

    @Override
    public void read(BlockState blockState, CompoundNBT compound) {
        super.read(blockState, compound);
        this.tick = compound.getInt(NBT_TICK);
        if (compound.contains(NBT_INGREDIENTS)) {
            this.ingredients = NbtUtil.stringToIngredients(compound.getString(NBT_INGREDIENTS));
            this.currentCoffeeType = CoffeeType.valueOf(compound.getString(NBT_COFFEE_TYPE));
        }
    }

    public Map<Ingredient, Integer> getIngredients() {
        return ingredients;
    }

    public boolean hasIngredient(Ingredient ingredient){
        return ingredients.get(ingredient) > 0;
    }

    public boolean canAddIngredient(Ingredient ingredient){
        return ingredients.get(ingredient) < MAX_INGREDIENT_AMOUNT;
    }
    public void addIngredient(Ingredient ingredient){
        if (!canAddIngredient(ingredient)) {
            return;
        }
        ingredients.put(ingredient, Math.min(ingredients.get(ingredient) + ingredient.getItemValue(), MAX_INGREDIENT_AMOUNT));
    }

    public void addIngredient(Ingredient ingredient, int amount){
        IntStream.range(0, amount).forEach(i -> {
            if (canAddIngredient(ingredient)){
                addIngredient(ingredient);
            }
        });
    }

    public void useIngredient(Ingredient ingredient){
        if (!hasIngredient(ingredient)) {
            return;
        }
        ingredients.put(ingredient, ingredients.get(ingredient) - 1);
    }

}
