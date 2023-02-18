package net.fabricmc.smphack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.smphack.Hacks.Fly.Fly;
import net.fabricmc.smphack.Hacks.Fly.Flymodes;
import net.fabricmc.smphack.Hacks.Jesus.Jesusm;
import net.fabricmc.smphack.Hacks.Jesus.jesus;
import net.fabricmc.smphack.Hacks.NoWeather.NoWeather;
import net.fabricmc.smphack.Hacks.Nofall.Nofall;
import net.fabricmc.smphack.Hacks.Speed.Speed;
import net.fabricmc.smphack.Hacks.SpeedMine.SpeedMine;
import net.fabricmc.smphack.Hacks.fullbright.Fullbright;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import static net.fabricmc.smphack.Hacks.Fly.Modes.BoatFly.BoatFly.player;

// I know no one is gonna check the source code but for the few people who do and might need help, I am glad for you;
// This shit took me much time. I didnt know How the fuck do you even use keybinds and onHudRender was. Heck I didnt even know what is the main class of fabric is. Bruh.
// This is not supposed to be a full on hack client
// Use this for fun with your friends and on shitty servers with shitty AntiCheat.

@Environment(EnvType.CLIENT)
public class HUDoverlay implements HudRenderCallback {

    //new object from ___ class
    Fly fly = new Fly();
    Nofall noFall = new Nofall();
    Jesusm jesus = new Jesusm();
    Speed speed = new Speed();
    jesus jes = new jesus();
    Fullbright fullbright = new Fullbright();
    SpeedMine speedmine= new SpeedMine();
    NoWeather noWeather=new NoWeather();
    float prevRainGradient;
    int tw = 10;
    int th = 10;
    boolean Fullbright;
    boolean NoWeather;

    private static final KeyBinding FlyKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Fly toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "Imperials"
    ));

    private static final KeyBinding Nofallkey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Nofall toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            "Imperials"
    ));

    public static final KeyBinding Jesuskey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Jesus toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "Imperials"
    ));
    private static final KeyBinding SpeedKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Speed toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "Imperials"
    ));
    private static final KeyBinding VelocityKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Veloctiy toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "Imperials"
    ));

    MinecraftClient mc = MinecraftClient.getInstance();
    @Override
    public void onHudRender(MatrixStack matrices, float tickDelta) {
        //To avoid stupid crashes
        if (mc != null )
        {
            assert mc.player != null;
            assert MinecraftClient.getInstance().world != null;
            prevRainGradient = MinecraftClient.getInstance().world.getRainGradient(prevRainGradient);


            //To stop rendering if debug screen or F3 menu is enabled
           if (mc.options.debugEnabled) {return;}
           //To avoid null crashes
            if (Formatting.RED.getColorValue() == null || Formatting.BLUE.getColorValue() == null || Formatting.WHITE.getColorValue() == null || Formatting.GREEN.getColorValue() == null)
            {
                return;
            }
            TextRenderer font = mc.textRenderer;
            if(font==null || player==null){return;}

            int alpha = 127;
            int blue = 0;

            int color = (alpha << 24) | (0) | blue;

            Screen.fill(matrices,5,76,20,76, color);


               //To Check if Flykey is pressed
             //Useless brackets in order to seperate the different mods
                if (FlyKey.wasPressed()) {
                    fly.toggled();
                }

                //To get fly mode
                Flymodes flymode = Flymodes.values()[fly.flybindcounter % Flymodes.values().length];
                String flymodeText = flymode.name().toUpperCase();
                Text text = Text.of("Fly [" + flymodeText + "]");

                //To check if enabled or disabled
                if (fly.enabled) {
                    fly.update();
                    flymode.fly();
                    //render [on]
                    font.draw(matrices, text, tw+2, th + 12, Formatting.GREEN.getColorValue());
                } else {
                    //render [off]
                    font.draw(matrices, text, tw+2, th + 12, Formatting.RED.getColorValue());
                    //player.sendMessage(Text.of("Fly Off"));
                }

               //
            {
                if (Nofallkey.wasPressed()) {
                    noFall.toggled();
                }
                if (noFall.enabled) {
                    noFall.update();
                    //render [on]
                    font.draw(matrices, "Nofall", tw, th + 48, Formatting.GREEN.getColorValue());
                } else {
                    //render [off]
                    font.draw(matrices, "Nofall", tw, th + 48, Formatting.RED.getColorValue());
                    //player.sendMessage(Text.of("Nofall Off"));
                }
            }
              //
            {
                if(Jesuskey.wasPressed()) {
                    jesus.toggled();
                }
                if (jesus.enabled) {
                    jes.update();
                    font.draw(matrices, "Jesus", tw, th + 36, Formatting.GREEN.getColorValue());
                } else {
                    font.draw(matrices, "Jesus", tw, th + 36, Formatting.RED.getColorValue());
                }
            }

            if(SpeedKey.wasPressed())
            {
                speed.toggled();
            }
            if(speed.enabled)
            {
                speed.update();
                font.draw(matrices, "Speed", tw+1, th + 24, Formatting.GREEN.getColorValue());
            }
            else
            {
                font.draw(matrices, "Speed", tw+1, th + 24, Formatting.RED.getColorValue());
                assert mc.player != null;
                mc.player.getAbilities().setWalkSpeed(speed.walkspeed);
            }
            Fullbright= GeneralConfig.getConfig().Fullbright;
            if(Fullbright)
            {fullbright.update();}
            else
            {mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);}

            if (GeneralConfig.enabled)
            {
                    font.draw(matrices, "Freecam", tw  , th + 60, Formatting.GREEN.getColorValue());
            }
            if (!GeneralConfig.enabled)
            {
                font.draw(matrices, "Freecam", tw   , th + 60, Formatting.RED.getColorValue());
            }

            speedmine.toggled();

            NoWeather= GeneralConfig.getConfig().NoWeather;
            if (NoWeather)
            {
                noWeather.update();
            }
            else
            {
                if(MinecraftClient.getInstance().world.isRaining()) {
                    MinecraftClient.getInstance().world.setRainGradient(0);
                }
            }

        }
    }
}

