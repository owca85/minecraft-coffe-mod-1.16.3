package owca.coffeemod.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owca.coffeemod.network.ModPacketHandler;
import owca.coffeemod.network.messages.AddIngredientMessage;
import owca.coffeemod.network.messages.MakeCoffeeMessage;
import owca.coffeemod.tileentity.CoffeeType;
import owca.coffeemod.tileentity.DeluxeCoffeeMachineTileEntity;
import owca.coffeemod.tileentity.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@OnlyIn(Dist.CLIENT)
public class DeluxeCoffeeMachineScreen extends Screen {

    public static final ResourceLocation BACKGROUND_RESOURCE_LOCATION = new ResourceLocation("coffeemod:textures/gui/background.png");

    private final BlockPos pos;
    private final Map<Ingredient, Integer> ingredients;

    private CoffeeType hoveredCoffee = null;

    private List<IngredientWidget> ingredientWidgets = new ArrayList<>();

    private List<CoffeeButton> coffeeButtons = new ArrayList<>();

    public DeluxeCoffeeMachineScreen(BlockPos pos, Map<Ingredient, Integer> ingredients) {
        super(new StringTextComponent(""));
        this.pos = pos;
        this.ingredients = ingredients;
    }

    @Override
    public void init() {
        Minecraft.getInstance().keyboardListener.enableRepeatEvents(true);
        buttons.clear();
        this.children.removeIf(c -> c instanceof Button);
        coffeeButtons.clear();
        ingredientWidgets.clear();

        int i = (this.width - 256) / 2;
        int j = (this.height - 256) / 2 + 64;

        coffeeButtons.add(new CoffeeButton(i + 80, j + 14 * 1, CoffeeType.ESPRESSO, onCoffeeSelected(), this::getButtonStatus, this::onCoffeeHovered,this));
        coffeeButtons.add(new CoffeeButton(i + 80, j + 14 * 3, CoffeeType.LATTE, onCoffeeSelected(), this::getButtonStatus, this::onCoffeeHovered,this));
        coffeeButtons.add(new CoffeeButton(i + 80, j + 14 * 5, CoffeeType.FRAPPE, onCoffeeSelected(), this::getButtonStatus, this::onCoffeeHovered,this));
        coffeeButtons.add(new CoffeeButton(i + 160, j + 14 * 1, CoffeeType.CARAMEL_MACCHIATO, onCoffeeSelected(), this::getButtonStatus, this::onCoffeeHovered,this));
        coffeeButtons.add(new CoffeeButton(i + 160, j + 14 * 3, CoffeeType.MOCHA, onCoffeeSelected(), this::getButtonStatus, this::onCoffeeHovered,this));
        coffeeButtons.add(new CoffeeButton(i + 160, j + 14 * 5, CoffeeType.COCOA_DRINK, onCoffeeSelected(), this::getButtonStatus, this::onCoffeeHovered,this));

        coffeeButtons.forEach(this::addButton);

        Stream.of(Ingredient.values()).forEach(ingredient -> ingredientWidgets.add(new IngredientWidget(this, 10, 40 + 21*ingredient.ordinal(), ingredient)));

    }

    void addButton(Button button){
        super.addButton(button);
    }

    private CoffeeButton.ButtonStatus getButtonStatus(CoffeeType coffeeType) {
        boolean hasRequiredIngredients = DeluxeCoffeeMachineTileEntity.hasRequiredIngredients(coffeeType, ingredients);
        return hasRequiredIngredients ? CoffeeButton.ButtonStatus.AVAILABLE : CoffeeButton.ButtonStatus.NOT_AVAILABLE; //TODO handle disables state - when coffeemachine is working
    }

    public Button.IPressable onIngredientAdd(Ingredient ingredient) {
        return (b) -> {
            ModPacketHandler.getInstance().sendToServer(new AddIngredientMessage(pos, ingredient, 1));
        };
    }

    protected Button.IPressable onCoffeeSelected() {
        return (b) -> {
            ModPacketHandler.getInstance().sendToServer(new MakeCoffeeMessage(pos, ((CoffeeButton) b).getCoffeeType()));
            close();
        };
    }

    protected void onCoffeeHovered(CoffeeType coffeeType) {
        hoveredCoffee = coffeeType;
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().keyboardListener.enableRepeatEvents(false);
    }

    @Override
    public void tick() {
        if (ingredientWidgets != null){
            ingredientWidgets.forEach(IngredientWidget::tick);
            coffeeButtons.forEach(CoffeeButton::tick);
        }
    }

    private void close() {
        Minecraft.getInstance().displayGuiScreen(null);
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (p_keyPressed_1_ == 256) { //ESC
            close();
        }
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public void render(MatrixStack matrixStack, int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground(matrixStack);
        drawGuiContainerBackgroundLayer(matrixStack);

        drawString(matrixStack, this.font, I18n.format("gui.ingredients") + ":", 20, 20, 16777215);
        ingredientWidgets.forEach(widget -> widget.render(matrixStack));

        hoveredCoffee = null;
        super.render(matrixStack, p_render_1_, p_render_2_, p_render_3_);
    }

    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(BACKGROUND_RESOURCE_LOCATION);
        int i = (this.width - 256) / 2;
        int j = (this.height - 256) / 2 + 64;
        this.blit(matrixStack, i, j, 0, 0, 256, 256);
    }

    public Map<Ingredient, Integer> getIngredients() {
        return ingredients;
    }

    public CoffeeType getHoveredCoffee() {
        return hoveredCoffee;
    }

    FontRenderer getFont(){
        return this.font;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
