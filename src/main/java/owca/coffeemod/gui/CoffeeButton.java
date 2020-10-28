package owca.coffeemod.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.Item;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owca.coffeemod.tileentity.CoffeeType;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static owca.coffeemod.gui.GuiUtil.buildCoffeeItemTooltip;

@OnlyIn(Dist.CLIENT)
public class CoffeeButton extends Button {

    private static final ResourceLocation BACKGROUND = new ResourceLocation("coffeemod:textures/gui/coffee_button.png");

    private final ResourceLocation iconResourceLocation;
    private final int xTexStart;
    private final int yTexStart;
    private final int yDiffTex;
    private final int textureWidth;
    private final int textureHeight;
    private final int iconOffsetX;
    private final int iconOffsetY;
    private final Function<CoffeeType, ButtonStatus> buttonStatusFunction;
    private final Consumer<CoffeeType> onCoffeeHovered;
    private final CoffeeType coffeeType;
    private int tooltipTick = 0;
    private DeluxeCoffeeMachineScreen screen;

    public CoffeeButton(int x, int y, CoffeeType coffeeType, IPressable onPressIn, Function<CoffeeType, ButtonStatus> buttonStatusFunction, Consumer<CoffeeType> onCoffeeHovered, DeluxeCoffeeMachineScreen screen) {
        this(x, y, 24, 24, 0, 0, 24, coffeeType.getIcon(), 4, 3, 24, 72, onPressIn, StringTextComponent.EMPTY, buttonStatusFunction, onCoffeeHovered, coffeeType);
        this.screen = screen;
    }

    private CoffeeButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffTex, ResourceLocation iconResourceLocation,
                         int iconOffsetX, int iconOffsetY, int p_i232261_9_, int p_i232261_10_, IPressable p_i232261_11_, ITextComponent p_i232261_12_,
                         Function<CoffeeType, ButtonStatus> buttonStatusFunction, Consumer<CoffeeType> onCoffeeHovered, CoffeeType coffeeType) {
        super(x, y, width, height, p_i232261_12_, p_i232261_11_, (p_238488_0_, p_238488_1_, p_238488_2_, p_238488_3_) -> {});
        this.textureWidth = p_i232261_9_;
        this.textureHeight = p_i232261_10_;
        this.xTexStart = xTexStart;
        this.yTexStart = yTexStart;
        this.yDiffTex = yDiffTex;
        this.iconResourceLocation = iconResourceLocation;
        this.iconOffsetX = iconOffsetX;
        this.iconOffsetY = iconOffsetY;
        this.buttonStatusFunction = buttonStatusFunction;
        this.onCoffeeHovered = onCoffeeHovered;
        this.coffeeType = coffeeType;
    }

    public void tick() {
        if (isHovered()) {
            tooltipTick++;
        } else {
            tooltipTick = 0;
        }
    }

    public void renderToolTip(MatrixStack matrixStack, int p_230443_2_, int p_230443_3_) {
        if (tooltipTick > 10) { //delay
            Item item = coffeeType.getItem().get();
            List<IReorderingProcessor> transform = Lists.transform(buildCoffeeItemTooltip(item, true), ITextComponent::func_241878_f);
            screen.renderTooltip(matrixStack, transform, p_230443_2_, p_230443_3_);
        }
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(BACKGROUND);
        int i = this.yTexStart;
        if (this.isHovered()) {
            onCoffeeHovered.accept(coffeeType);
            switch (buttonStatusFunction.apply(coffeeType)) {
                case AVAILABLE:
                    i += this.yDiffTex;
                    break;
                case NOT_AVAILABLE:
                    i += this.yDiffTex * 2;
                    break;
                case DISABLED:
                    //do nothing
            }
        }

        RenderSystem.enableDepthTest();
        blit(matrixStack, this.x, this.y, (float) this.xTexStart, (float) i, this.width, this.height, this.textureWidth, this.textureHeight);

        minecraft.getTextureManager().bindTexture(iconResourceLocation);
        blit(matrixStack, this.x + iconOffsetX, this.y + iconOffsetY, 0, 0, 16, 16, 16, 16);

        if (this.isHovered()) {
            renderToolTip(matrixStack, p_230431_2_,p_230431_3_);
        }
    }

    @Override
    public void onPress() {
        if (buttonStatusFunction.apply(coffeeType)== ButtonStatus.AVAILABLE) {
            super.onPress();
        }
    }

    public CoffeeType getCoffeeType() {
        return coffeeType;
    }

    public enum ButtonStatus {
        AVAILABLE,
        NOT_AVAILABLE,
        DISABLED
    }

}