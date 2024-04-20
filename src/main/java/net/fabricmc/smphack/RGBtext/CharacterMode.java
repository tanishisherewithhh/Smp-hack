package net.fabricmc.smphack.RGBtext;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.smphack.GeneralConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;

import java.awt.*;

import static net.fabricmc.smphack.config.ConfigUtil.config;

@Environment(EnvType.CLIENT)
public class CharacterMode {
    static MinecraftClient mc = MinecraftClient.getInstance();
    static int xp;
    static int yp;
    static int zp;
    static int y;
    static int windowHeight;
    static int screenWidth;
    static int tw = 10; // Text width or X position for displaying
    static int th = 10; // Text Height or Y position for displaying
    static int tsw = 10; // Text width or X position for displaying and for changing postition of the text
    static float hue;

    static boolean TextShadow;

    public static void coordinates(PlayerEntity player) {
        assert mc.player != null;
        xp = (int) mc.player.getX();// Players X-Y-Z positions
        yp = (int) mc.player.getY();//- ^
        zp = (int) mc.player.getZ();//- |
    }

    public static void displayposandcolour(TextRenderer font) {
        hue = (float) (System.currentTimeMillis() % 10000) / 10000; // RGB hue
        windowHeight = mc.getWindow().getScaledHeight();//idk why am i using this
        y = windowHeight - font.fontHeight - 10; // again idk why am i using this
        screenWidth = mc.getWindow().getScaledWidth();
    }

    public static void rendertext(DrawContext drawContext, TextRenderer font, String text, float x, float y, int color) {
        drawContext.drawText(font,text, (int) x, (int) y,color,TextShadow);
    }

    public static void init() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            //stupid checks to avoid crashes
            if (mc.isRunning()) {
                //font
                TextRenderer font = mc.textRenderer;
                if (font == null || mc.player == null) return;
                if (mc.getDebugHud().shouldShowDebugHud()) return;
                PlayerEntity player = MinecraftClient.getInstance().player;
                if (GeneralConfig.getConfig() == null) {
                    return;
                }
                assert player != null;
                //end of stupid checks

                //Start of variables
                TextShadow = GeneralConfig.getConfig().getTextshadow();
                coordinates(player);
                displayposandcolour(font);
                int x_offset = 0;
                //End of initializing variables;
                String LOGO = "SMP-Hack v2-6";

                int stringWidth = font.getWidth(LOGO);
                for (int i = 0; i < LOGO.length(); i++) {
                    char e = LOGO.charAt(i);
                    Color color = Color.getHSBColor(hue + ((float) i / (float) LOGO.length()), 1, 1);
                    int charWidth = font.getWidth(String.valueOf(e));
                    tsw = 10;
                    rendertext(drawContext, font, String.valueOf(e), -29 + ((float) (stringWidth - charWidth) / 2) + x_offset, th, color.getRGB());
                    x_offset += charWidth;
                }

            }
        });
    }
}








