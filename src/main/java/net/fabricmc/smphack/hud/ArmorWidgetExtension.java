package net.fabricmc.smphack.hud;

import com.tanishisherewith.dynamichud.helpers.ColorHelper;
import com.tanishisherewith.dynamichud.helpers.TextureHelper;
import com.tanishisherewith.dynamichud.util.TextGenerator;
import com.tanishisherewith.dynamichud.widget.armor.ArmorWidget;
import net.fabricmc.smphack.config.ConfigUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.Objects;
import java.util.function.Supplier;

public class ArmorWidgetExtension extends ArmorWidget {
    /**
     * Constructs an ArmorWidget object.
     *
     * @param client              The Minecraft client instance
     * @param slot                The equipment slot to display the armor item from
     * @param xPercent            The x position of the widget as a percentage of the screen width
     * @param yPercent            The y position of the widget as a percentage of the screen height
     * @param enabled
     * @param currentTextPosition
     * @param textGenerator
     * @param color
     */
    public ArmorWidgetExtension(MinecraftClient client, EquipmentSlot slot, float xPercent, float yPercent, boolean enabled, TextureHelper.Position currentTextPosition, TextGenerator textGenerator, Supplier<Color> color) {
        super(client, slot, xPercent, yPercent, enabled, currentTextPosition, textGenerator, color);
    }
    private static final int COLOR_GREEN = 0x00FF00;
    private static final int COLOR_YELLOW = 0xFFFF00;
    private static final int COLOR_RED = 0xFF0000;

    public static int getDurabilityColor(float durability) {
        if (durability > 0.5) {
            return interpolateColors(COLOR_YELLOW, COLOR_GREEN, (durability - 0.5f) * 2);
        } else {
            return interpolateColors(COLOR_RED, COLOR_YELLOW, durability * 2);
        }
    }

    private static int interpolateColors(int color1, int color2, float factor) {
        int red = (int) (((color1 >> 16) & 0xFF) * (1 - factor) + ((color2 >> 16) & 0xFF) * factor);
        int green = (int) (((color1 >> 8) & 0xFF) * (1 - factor) + ((color2 >> 8) & 0xFF) * factor);
        int blue = (int) ((color1 & 0xFF) * (1 - factor) + (color2 & 0xFF) * factor);
        return (red << 16) | (green << 8) | blue;
    }


    @Override
    public void render(DrawContext drawContext) {
            super.render(drawContext);
            String ArmorDurabilityDisplay = String.valueOf(ConfigUtil.config.ArmorDurability);
            int x=0;
            int y=0;
            if (Objects.equals(ArmorDurabilityDisplay, "Bar")) {
                ItemStack stack = MinecraftClient.getInstance().player.getEquippedStack(slot);
                if (!stack.isEmpty()) {
                    double durabilityPercentage = ((double) stack.getMaxDamage() - (double) stack.getDamage()) / (double) stack.getMaxDamage();
                    int barWidth = Math.round((float)durabilityPercentage * 16);
                    Color color= ColorHelper.getColorFromInt(getDurabilityColor((float)durabilityPercentage));
                    switch (currentTextPosition[0]) {
                        case ABOVE -> {
                            x = getX();
                            y = getY();
                        }
                        case BELOW -> {
                            x = getX();
                            y = getY() + 16;
                        }
                        case LEFT -> {
                            x = getX() - 18;
                            y = getY() + 8;
                        }
                        case RIGHT -> {
                            x = getX() + 18;
                            y = getY() + 8;
                        }
                    }
                    drawContext.fill( x, y + 1, x + 16, y - 1, Color.BLACK.getRGB());
                    drawContext.fill( x, y, x + barWidth, y - 1, color.getRGB());
                }
            }
    }
}
