package net.fabricmc.smphack.RGBtext;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.smphack.GeneralConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Optional;

import static net.fabricmc.loader.impl.util.StringUtil.capitalize;
import static net.fabricmc.smphack.Hacks.Fly.Modes.BoatFly.BoatFlyHack.player;
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
    static int fps;
    static int ping;

    static String Biome;
    static double deltaX,deltaY,deltaZ,floatbps;
    static int tw = 10; // Text width or X position for displaying
    static int th = 10; // Text Height or Y position for displaying
    static int tsw=10; // Text width or X position for displaying and for changing postition of the text
    static int tsh= windowHeight+1; // Text width or Y position for displaying and for changing postition of the text
    static String bps;
    static float hue;

    static boolean TextShadow;

    public static void coordinates(PlayerEntity player)
    {
        assert mc.player != null;
        xp = (int) mc.player.getX();// Players X-Y-Z positions
        yp = (int) mc.player.getY();//- ^
        zp = (int) mc.player.getZ();//- |
        deltaX = player.getX() - player.prevX;
        deltaY = player.getY() - player.prevY;
        deltaZ = player.getZ() - player.prevZ;
        floatbps = (float)Math.abs(Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2))*20);
        DecimalFormat df = new DecimalFormat("#.#");
        bps = df.format(floatbps);
        ping=getValue();
    }
    public static int getValue() {
        assert mc.player != null;
        PlayerListEntry entry = mc.player.networkHandler.getPlayerListEntry(mc.player.getUuid());
        if (entry != null) {
            return entry.getLatency(); // This method updates slow
        }
        return 0;
    }

    public static String getBiome() {
        String biomes = "";
        if (mc.world != null) {
            Optional<RegistryKey<net.minecraft.world.biome.Biome>> biome = mc.world.getBiome(player.getBlockPos()).getKey();

            if (biome.isPresent()) {
                String biomeName =  Text.translatable("biome." + biome.get().getValue().getNamespace() + "." + biome.get().getValue().getPath()).getString();;
                biomes = Text.translatable("text.biome", capitalize(biomeName)).getString();
                assert MinecraftClient.getInstance().world != null;
                assert MinecraftClient.getInstance().player != null;
            }
        }
        return biomes;
    }

    public static void displayposandcolour(TextRenderer font) {
        hue = (float) (System.currentTimeMillis() % 10000) / 10000; // RGB hue
        windowHeight = mc.getWindow().getScaledHeight();//idk why am i using this
        y = windowHeight - font.fontHeight - 10; // again idk why am i using this
        screenWidth = mc.getWindow().getScaledWidth();
        fps = mc.getCurrentFps();
        Biome=getBiome();
    }
    public static void rendertext(MatrixStack matrices,TextRenderer font,String text,float x,float y,int color)
    {
        if (TextShadow) {
            font.drawWithShadow(matrices,text , x, y, color, false);
        }
        else
        {
            font.draw(matrices, text, x, y, color);
        }
    }

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
                TextShadow=GeneralConfig.getConfig().getTextshadow();
                String TextSide = String.valueOf(config.Textside);
                String CoordSide = String.valueOf(config.Coordside);
                coordinates(player);
                displayposandcolour(font);
                int x_offset = 0;
                //End of initializing variables;
                String text = "XYZ " + xp + " " + yp + " " + zp;
                String LOGO = "SMP-Hack v2-4-3";

                int stringWidth = font.getWidth(LOGO);
                for (int i = 0; i < LOGO.length(); i++) {
                    char e = LOGO.charAt(i);
                    Color color = Color.getHSBColor(hue + ((float) i / (float) LOGO.length()), 1, 1);
                    int charWidth = font.getWidth(String.valueOf(e));
                    //tsw + ((float) (stringWidth - charWidth) / 2)
                    tsw=10;
                    rendertext(matrices,font,String.valueOf(e),-29 + ((float) (stringWidth - charWidth) / 2) + x_offset,th,color.getRGB());
                    x_offset += charWidth;
                }

                // Calculate text width and colors
                int textWidth = font.getWidth(text);
                Color[] colors = new Color[text.length()];
                for (int i = 0; i < text.length(); i++) {
                    float hueOffset = i / (float) text.length();
                    colors[i] = Color.getHSBColor(hue + hueOffset, 1, 1);
                }

                // Draw coordinate text
                for (int i = 0; i < text.length(); i++) {
                    char c = text.charAt(i);
                    int width = font.getWidth(String.valueOf(c));
                    switch (CoordSide) {
                        case "TopRight" -> {
                            tsw = screenWidth - textWidth - 20 + i * width;
                            y =16;
                        }
                        case "BottomRight" -> {
                            tsw = screenWidth-textWidth- 20 + i * width;
                            y = windowHeight-font.fontHeight-10;
                        }
                        case "BottomLeft" -> {
                            tsw = (tw + i * width);
                            y=windowHeight-font.fontHeight - 23;
                        }
                        case "Top"->{
                            tsw=(((screenWidth-textWidth)/2)+i*width) - 2;
                            y=5;
                        }

                    }
                    rendertext(matrices,font,String.valueOf(c),tsw,y,colors[i].getRGB());
                }

              // Draw FPS, BPS, Ping and Biome text
                String FPS = "FPS " + fps;
                String Speed = "BPS " + bps;
                String Ping = "Ping " + ping;
                int FPSWidth = font.getWidth(FPS);
                int PingWidth = font.getWidth(Ping);
                int SpeedWidth = font.getWidth(Speed);
                int pingX = screenWidth - PingWidth - 10;
                int biomeX = pingX - font.getWidth(Biome)+30;
                switch (TextSide) {
                    case "TopRight" -> {
                        FPS = fps + " FPS";
                        Speed = bps + " BPS";
                        Ping= ping +" Ping";
                        int pingY = tsh+114;
                        int biomeY = tsh+126;
                        rendertext(matrices,font,FPS,screenWidth - FPSWidth - 10, tsh + 90, colors[0].getRGB());
                        rendertext(matrices,font,Speed,screenWidth - SpeedWidth - 10, tsh + 102, colors[0].getRGB());
                        rendertext(matrices,font,Ping,pingX, pingY, colors[0].getRGB());
                        rendertext(matrices,font,Biome,biomeX, biomeY, colors[0].getRGB());
                    }
                    case "BottomRight" -> {
                        FPS = fps + " FPS";
                        Speed = bps + " BPS";
                        Ping= ping+" Ping";
                        tsw = screenWidth - SpeedWidth - 10;
                        y = windowHeight - 24;
                        int pingY = windowHeight -36;
                        int biomeY = windowHeight-48;
                        rendertext(matrices,font,FPS,screenWidth - FPSWidth - 10, tsh = windowHeight - 12, colors[0].getRGB());
                        rendertext(matrices,font,Speed,tsw, y, colors[0].getRGB());
                        rendertext(matrices,font,Ping,pingX, pingY, colors[0].getRGB());
                        rendertext(matrices,font,Biome,biomeX+1, biomeY, colors[0].getRGB());
                    }
                    case "Left" -> {
                        tsh=10;
                        tsw=10;
                        rendertext(matrices,font,FPS,tsw, tsh + 132, colors[0].getRGB());
                        rendertext(matrices,font,Speed,tsw, tsh + 144, colors[0].getRGB());
                        rendertext(matrices,font,Ping,tsw, tsh + 156, colors[0].getRGB());
                        rendertext(matrices,font,Biome,tsw-4, tsh + 168, colors[0].getRGB());
                    }
                }
            }
        });
    }
}








