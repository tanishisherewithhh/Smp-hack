package RGBtext;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class LineMode {
    static MinecraftClient mc = MinecraftClient.getInstance();
    static StringBuilder Coords = new StringBuilder();
    static StringBuilder Logo   = new StringBuilder();
    static StringBuilder Fps  = new StringBuilder();
    static StringBuilder Tps  = new StringBuilder();

    public static void init() {
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (!mc.isRunning()) {
                return;
            }

            TextRenderer font = mc.textRenderer;
            if (font == null || mc.player == null || mc.options.debugEnabled) {
                return;
            }
            int fps = mc.getCurrentFps();
            int tw = 10;
            int th = 10;
            int xp = (int) mc.player.getX();
            int yp = (int) mc.player.getY();
            int zp = (int) mc.player.getZ();
            float hue = (float) (System.currentTimeMillis() % 10000) / 10000;
            int windowHeight = mc.getWindow().getScaledHeight();
            int y = windowHeight - font.fontHeight - 10;
            //float tps = server == null ? 0 : server.getTickTime();
            String FPS = "FPS " + fps;
            String LOGO = "SMP-Hack 2v";
            //String TPS = "TPS " + getTPS();

            Coords.append("X: ").append(xp).append(" Y: ").append(yp).append(" Z: ").append(zp);
            Fps.append(FPS);
            Logo.append(LOGO);
            //Tps.append(TPS);

            font.draw(matrices, String.valueOf(Coords), tw, y, Color.getHSBColor(hue, 1, 1).getRGB());
            font.draw(matrices, String.valueOf(Fps), tw, th+60, Color.getHSBColor(hue, 1, 1).getRGB());
            font.draw(matrices, String.valueOf(Logo), tw, th, Color.getHSBColor(hue, 1, 1).getRGB());
            //font.draw(matrices, String.valueOf(Tps), tw, y, Color.getHSBColor(hue, 1, 1).getRGB());

        });
    }

}








