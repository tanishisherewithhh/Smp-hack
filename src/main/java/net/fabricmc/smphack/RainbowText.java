package net.fabricmc.smphack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Formatting;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class RainbowText {

    public static double getTPS() {
        MinecraftServer server = MinecraftClient.getInstance().getServer();
        assert server != null;
        return server.getTickTime();
    }
    public static void init() {

        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            //stupid checks to avoid crashes
            if (mc.isRunning()) {
                //font
                TextRenderer font = mc.textRenderer;
                if (font == null || mc.player == null) return;
                if (mc.options.debugEnabled) return;
                //end of stupid checks

                //Start of variables
                MinecraftServer server = mc.getServer();
                int fps = mc.getCurrentFps(); // Getting players fps (Slow: updates only once per second (same as fps counter from debug menu))
                int tw = 10; // Text width or X position for displaying
                int th = 10; // Text Height or Y position for displaying
                int xp = (int) mc.player.getX();// Players X-Y-Z positions
                int yp = (int) mc.player.getY();//- ^
                int zp = (int) mc.player.getZ();//- |
                int windowHeight = mc.getWindow().getScaledHeight();//idk why am i using this
                int y = windowHeight - font.fontHeight - 10; // again idk why am i using this
                float tps = 0;
                if (server != null) { //another stupid check
                    tps = server.getTickTime(); // going to update soon (this is not TPS of the server)
                }
                //End of initializing variables;
                String text = "X " + xp + "  Y " + yp + " Z " + zp;
                String FPS = "FPS " + fps;
                String LOGO = "SMP-Hack 1-9v";
                String TPS = "TPS " + getTPS();
                int x_offset = 0;
                int x_offset1 = 0;

                for (int i = 0; i < text.length(); i++) {
                    char c = text.charAt(i);
                    float hue = (float) (System.currentTimeMillis() % 10000) / 10000;
                    Color color = Color.getHSBColor(hue + (i / (float) text.length()), 1, 1);
                    int width = font.getWidth(String.valueOf(c));
                    font.draw(matrices, String.valueOf(c), tw + i * width, y, color.getRGB());
                }
                for (int i = 0; i < FPS.length(); i++) {
                    char d = FPS.charAt(i);
                    float hue = (float) (System.currentTimeMillis() % 10000) / 10000;
                    Color color = Color.getHSBColor(hue + (i / (float) FPS.length()), 1, 1);
                    int fpswidth = font.getWidth(String.valueOf(d));
                    font.draw(matrices, String.valueOf(d), tw + i * fpswidth, (th + 60), color.getRGB());
                }
                int stringWidth = font.getWidth(LOGO);
                for (int i = 0; i < LOGO.length(); i++) {
                    char e = LOGO.charAt(i);
                    float hue = (float) (System.currentTimeMillis() % 10000) / 10000;
                    Color color = Color.getHSBColor(hue + ((float) i / (float) LOGO.length()), 1, 1);
                    int charWidth = font.getWidth(String.valueOf(e));
                    font.draw(matrices, String.valueOf(e), -23 + ((float) (stringWidth - charWidth) / 2) + x_offset, th, color.getRGB());
                    x_offset += charWidth;
                }
                int TPSWidth = font.getWidth(TPS);
                for (int i = 0; i < TPS.length(); i++) {
                    char f = TPS.charAt(i);
                    float hue = (float) (System.currentTimeMillis() % 10000) / 10000;
                    Color color = Color.getHSBColor(hue + ((float) i / (float) TPS.length()), 1, 1);
                    int TPScharWidth = font.getWidth(String.valueOf(f));
                    font.draw(matrices, String.valueOf(f), -10 + ((float) (TPSWidth - TPScharWidth) / 2) + x_offset1, th + 72, color.getRGB());
                    x_offset1 += TPScharWidth;
                }
            }
        });
    }
}








