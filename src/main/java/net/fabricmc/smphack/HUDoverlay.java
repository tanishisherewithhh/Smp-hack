package net.fabricmc.smphack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.smphack.Fly.Flymodes;
import net.fabricmc.smphack.Jesus.Jesusm;
import net.fabricmc.smphack.Jesus.jesus;
import net.fabricmc.smphack.Nofall.Nofall;
import net.fabricmc.smphack.Speed.Speed;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import net.fabricmc.smphack.Fly.Fly;

import static net.fabricmc.smphack.BoatFly.BoatFly.player;

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

    int tw = 10;
    int th = 10;
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
    MinecraftClient mc = MinecraftClient.getInstance();
    @Override
    public void onHudRender(MatrixStack matrices, float tickDelta) {
        //To avoid stupid crashes
        if (mc != null )
        {
        //To stop rendering if debug screen or F3 menu is enabled
           if (mc.options.debugEnabled) {return;}
           //To avoid null crashes
            if (Formatting.RED.getColorValue() == null || Formatting.BLUE.getColorValue() == null || Formatting.WHITE.getColorValue() == null || Formatting.GREEN.getColorValue() == null)
            {
                return;
            }
            TextRenderer font = mc.textRenderer;
            if(font==null || player==null){return;}



               //To Check if Flykey is pressed
            { //Useless brackets in order to seperate the different mods
                if (FlyKey.wasPressed()) {
                    fly.toggled();
                    player.sendMessage(Text.of("Fly On"));
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
            }
               //
            {
                if (Nofallkey.wasPressed()) {
                    noFall.toggled();
                    player.sendMessage(Text.of("Nofall On"));
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
                player.sendMessage(Text.of("Speed On"));
            }
            if(speed.enabled)
            {
                speed.update();
                font.draw(matrices, "Speed", tw, th + 24, Formatting.GREEN.getColorValue());
            }
            else
            {
                font.draw(matrices, "Speed", tw, th + 24, Formatting.RED.getColorValue());
                assert mc.player != null;
                mc.player.getAbilities().setWalkSpeed(speed.walkspeed);
            }



        }
    }
}

