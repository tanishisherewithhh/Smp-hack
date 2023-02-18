package net.fabricmc.smphack.RGBtext;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.smphack.GeneralConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.entity.player.PlayerEntity;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class CharacterMode {
    static MinecraftClient mc = MinecraftClient.getInstance();
    public static void init() {

        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            //stupid checks to avoid crashes
            if (mc.isRunning()) {
                //font
                TextRenderer font = mc.textRenderer;
                if (font == null || mc.player == null) return;
                if (mc.options.debugEnabled) return;
                PlayerEntity player = MinecraftClient.getInstance().player;
                if (GeneralConfig.getConfig()==null){return;}
                assert player != null;
                //end of stupid checks

                //Start of variables
                String TextSide = GeneralConfig.getConfig().getTextSide();
                String CoordSide = GeneralConfig.getConfig().getCoordsSide();
                int fps = mc.getCurrentFps(); // Getting players fps (Slow: updates only once per second (same as fps counter from debug menu))
                int tw = 10; // Text width or X position for displaying
                int th = 10; // Text Height or Y position for displaying
                int tsw=10; // Text width or X position for displaying and for changing postition of the text
                int tsh=10; // Text width or Y position for displaying and for changing postition of the text
                int TextSideWidthSpeed=10;

                int xp = (int) mc.player.getX();// Players X-Y-Z positions
                int yp = (int) mc.player.getY();//- ^
                int zp = (int) mc.player.getZ();//- |

                var deltaX = player.getX() - player.prevX;
                var deltaY = player.getY() - player.prevY;
                var deltaZ = player.getZ() - player.prevZ;
                var floatbps = (float)Math.abs(Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2))*20);
                DecimalFormat df = new DecimalFormat("#.#");
                String bps = df.format(floatbps);

                float hue = (float) (System.currentTimeMillis() % 10000) / 10000; // RGB hue
                int windowHeight = mc.getWindow().getScaledHeight();//idk why am i using this
                int y = windowHeight - font.fontHeight - 10; // again idk why am i using this

                int screenWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
                //End of initializing variables;
                String text = "X " + xp + " Y " + yp + " Z " + zp;
                String FPS = "FPS "+fps;
                String LOGO = "SMP-Hack v2-2-2";
                String Speed="BPS "+bps;
                int x_offset = 0;

                for (int i = 0; i < text.length(); i++) {
                    char c = text.charAt(i);
                    int CoordWidth = font.getWidth(text);
                    Color color = Color.getHSBColor(hue + (i / (float) text.length()), 1, 1);
                    int width = font.getWidth(String.valueOf(c));
                    if (Objects.equals(CoordSide, "BottomLeft")) {
                        font.draw(matrices, String.valueOf(c), tw + i * width, y, color.getRGB());
                    }
                    else if(Objects.equals(CoordSide, "TopRight"))
                    {
                        GeneralConfig.getConfig().setCoordsSide("TopRight");
                        CoordSide="TopRight";
                        tw = screenWidth - CoordWidth - 20;
                        font.draw(matrices, String.valueOf(c), tw + i * width, 10, color.getRGB());
                    }
                    else if (Objects.equals(CoordSide, "BottomRight"))
                    {
                        GeneralConfig.getConfig().setCoordsSide("BottomRight");
                        CoordSide="BottomRight";
                        //y = windowHeight - 24;
                        tw = screenWidth - CoordWidth - 20;
                        font.draw(matrices, String.valueOf(c), tw + i * width, y, color.getRGB());
                    }
                }

                int stringWidth = font.getWidth(LOGO);
                for (int i = 0; i < LOGO.length(); i++) {
                    char e = LOGO.charAt(i);
                    Color color = Color.getHSBColor(hue + ((float) i / (float) LOGO.length()), 1, 1);
                    int charWidth = font.getWidth(String.valueOf(e));
                    font.draw(matrices, String.valueOf(e), -29 + ((float) (stringWidth - charWidth) / 2) + x_offset, th, color.getRGB());
                    x_offset += charWidth;
                }

                if (Objects.equals(TextSide, "Left"))
                {
                    font.draw(matrices, FPS, tsw, tsh + 72, Color.getHSBColor(hue, 1, 1).getRGB());
                    font.draw(matrices, Speed, tsw, tsh + 84, Color.getHSBColor(hue, 1, 1).getRGB());
                }
                else if (Objects.equals(TextSide, "TopRight"))
                {
                    FPS = fps+" FPS" ;
                    Speed = bps+" BPS";
                    int fpsWidth = font.getWidth(FPS);
                    int speedwidth = font.getWidth(Speed);
                    tsw = screenWidth - fpsWidth - 10;
                    TextSideWidthSpeed = screenWidth-speedwidth-10;
                    font.draw(matrices, FPS, tsw, tsh, Color.getHSBColor(hue, 1, 1).getRGB());
                    tsh += 12;
                    font.draw(matrices, Speed, TextSideWidthSpeed, tsh, Color.getHSBColor(hue, 1, 1).getRGB());
                    font.draw(matrices, bps, TextSideWidthSpeed, tsh, Color.getHSBColor(hue, 1, 1).getRGB());
                    //Display the text on the top right of the screen
                }
                else if (Objects.equals(TextSide, "BottomRight"))
                {
                    FPS = fps+" FPS" ;
                    Speed = bps+" BPS";
                    int fpsWidth = font.getWidth(FPS);
                    int bpswidth = font.getWidth(Speed);
                    tsw = screenWidth - fpsWidth - 10;
                    tsh = windowHeight - 24;
                    TextSideWidthSpeed = screenWidth-bpswidth-10;
                    font.draw(matrices, FPS, tsw, tsh, Color.getHSBColor(hue, 1, 1).getRGB());
                    tsh += 12;
                    font.draw(matrices, Speed, TextSideWidthSpeed, tsh, Color.getHSBColor(hue, 1, 1).getRGB());
                    font.draw(matrices, bps, TextSideWidthSpeed, tsh, Color.getHSBColor(hue, 1, 1).getRGB());
                    //Display the text on the bottom right of the screen
                }

            }
        });
    }
}








