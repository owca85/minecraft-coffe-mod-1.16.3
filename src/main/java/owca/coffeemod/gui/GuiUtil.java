package owca.coffeemod.gui;

import net.minecraft.item.Item;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class GuiUtil {

    private GuiUtil(){}

    public static List<ITextComponent> buildCoffeeItemTooltip(Item item, boolean includeName){
        List<ITextComponent> tooltip = new ArrayList<>();
        if (includeName){
            tooltip.add(new TranslationTextComponent(item.getTranslationKey()).mergeStyle(TextFormatting.WHITE));
        }
        if (item.getFood() != null) {
            item.getFood().getEffects().forEach(e -> tooltip.add(new TranslationTextComponent(e.getFirst().getEffectName())
                    .appendString(" ")
                    .append(new TranslationTextComponent("enchantment.level." + (e.getFirst().getAmplifier() + 1)))
                    .appendString(formatDuration(e.getFirst().getDuration()))
                    .mergeStyle(TextFormatting.BLUE)
            ));
        }
        return tooltip;
    }

    private static String formatDuration(int duration){
        int minutes = duration / 20 / 60;
        int seconds = (duration / 20) % 60;
        return String.format(" (%02d:%02d)", minutes, seconds);
    }

}
