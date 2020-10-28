package owca.coffeemod.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owca.coffeemod.tileentity.Ingredient;

import java.util.ArrayList;
import java.util.List;

import static owca.coffeemod.tileentity.DeluxeCoffeeMachineTileEntity.MAX_INGREDIENT_AMOUNT;

@OnlyIn(Dist.CLIENT)
public class IngredientWidget extends AbstractGui {

    private static final int RED = 0xAA0000;
    private static final int GREEN = 0x00AA00;
    private static final int WHITE = 0xFFFFFF;

    private final DeluxeCoffeeMachineScreen screen;
    private final int x;
    private final int y;
    private final Ingredient ingredient;
    private final Button button;
    private int tooltipTick = 0;

    public IngredientWidget(DeluxeCoffeeMachineScreen screen, int x, int y, Ingredient ingredient) {
        this.screen = screen;
        this.x = x;
        this.y = y;
        this.ingredient = ingredient;
        this.button = new Button(x + 50, y - 7, 20, 20, new StringTextComponent("+"), screen.onIngredientAdd(ingredient), onTooltip());
        screen.addButton(button);

    }

    private ITextComponent getTooltipText() {
        if (button.active) {
            return new TranslationTextComponent("gui.ingredient.add.from.inventory.x.available", ingredient.getItem().getName(), getItemCountInInventory(ingredient));
        } else {
            if (screen.getIngredients().get(ingredient) >= MAX_INGREDIENT_AMOUNT) {
                return new TranslationTextComponent("gui.ingredient.full");
            } else {
                return new TranslationTextComponent("gui.ingredient.not.enough", ingredient.getItem().getName());
            }
        }
    }

    private int getItemCountInInventory(Ingredient ingredient) {
        int count = 0;
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player == null) {
            return count;
        }

        for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack.getItem() == ingredient.getItem()) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public void tick() {
        if (button.isHovered()) {
            tooltipTick++;
        } else {
            tooltipTick = 0;
        }
    }

    private Button.ITooltip onTooltip() {
        return (p_238659_1_, p_238659_2_, p_238659_3_, p_238659_4_) -> {
            if (tooltipTick > 10) { //delay
                List<ITextComponent> list = new ArrayList<>();
                list.add(getTooltipText());
                List<IReorderingProcessor> transform = Lists.transform(list, ITextComponent::func_241878_f);
                screen.renderTooltip(p_238659_2_, transform, p_238659_3_, p_238659_4_);
            }
        };
    }

    public void render(MatrixStack matrixStack) {
        Integer amount = screen.getIngredients().get(ingredient);
        renderIcon(matrixStack, ingredient.getIcon(), x, y - 5);
        int fontColor = WHITE;
        if (shouldHighlightIngredient(ingredient)) {
            fontColor = amount > 0 ? GREEN : RED;
        }
        drawString(matrixStack, screen.getFont(), amount + "/" + MAX_INGREDIENT_AMOUNT, x + 17, y, fontColor);
        button.active = amount < MAX_INGREDIENT_AMOUNT && getItemCountInInventory(ingredient) > 0;
    }

    private boolean shouldHighlightIngredient(Ingredient ingredient) {
        if (screen.getHoveredCoffee() == null) {
            return false;
        }
        return screen.getHoveredCoffee().getIngredients().contains(ingredient);
    }

    private void renderIcon(MatrixStack matrixStack, ResourceLocation resourceLocation, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(resourceLocation);
        blit(matrixStack, x, y, 0, 0, 16, 16, 16, 16);
    }

}
