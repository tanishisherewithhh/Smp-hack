package net.fabricmc.smphack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.smphack.BoatFly.BoatFly;
import net.fabricmc.smphack.Creativefly.AntiKick;
import net.fabricmc.smphack.Glider.GliderHack;
import net.fabricmc.smphack.Jetpack.JetpackHack;
import net.fabricmc.smphack.Nofall.NoFallHack;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import static net.fabricmc.smphack.BoatFly.BoatFly.player;

@Environment(EnvType.CLIENT)
public class Overlay implements HudRenderCallback {

    //ke
    private static final KeyBinding Nofallkey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Nofall toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            "Imperials"
    ));
    public static boolean flytoggle = false;
    public static boolean Glidetoggle = false;
    public static boolean Nofalltoggle = false;
    private static int flyMode = 0,Nofallval = 0;
    //counter variable (prob remove sometime later)
    int tw = 10;
    int th = 10;




    //global mc variable
    MinecraftClient mc = MinecraftClient.getInstance();

    //Nofall
    public void Nofall() {
        NoFallHack noFallHack = new NoFallHack();
        noFallHack.cancelFallDamage((ClientPlayerEntity) player);
    }
    //antikick for creative fly


    @Override
    public void onHudRender(MatrixStack matrices, float delta) {
        //null checks
        if (mc != null) {
            if (Formatting.RED.getColorValue() == null || Formatting.BLUE.getColorValue() == null || Formatting.WHITE.getColorValue() == null || Formatting.GREEN.getColorValue() == null)
                return;
            if (mc.options.debugEnabled) return;
            if (mc.isRunning()) {
                //font
                TextRenderer font = mc.textRenderer;
                if (font == null || mc.player == null || player == null) return;
                //end of null checks
                // boolean death = player.isDead();
                //misc / check
                if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.GLFW_KEY_X))
                    font.draw(matrices, "[X] Xray", tw, th + 24, Formatting.GREEN.getColorValue());
                else font.draw(matrices, "[X] Xray", tw, th + 24, Formatting.RED.getColorValue());
                if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.GLFW_KEY_C))
                    font.draw(matrices, "[C] Freecam", tw, th + 36, Formatting.GREEN.getColorValue());
                else{ font.draw(matrices, "[C] Freecam", tw, th + 36, Formatting.RED.getColorValue());}
                    //Nofall
                    if (Nofallkey.wasPressed()) {
                        if (Nofalltoggle) {
                            Nofalltoggle = false;
                            font.draw(matrices, "[N] Nofall", tw, th + 48, Formatting.RED.getColorValue());
                        } else {
                            Nofalltoggle = true;
                            player.sendMessage(Text.of("Nofall On"));
                            if (Nofallval > 0) {
                                Nofallval = 0;
                            }
                            Nofallval = (Nofallval + 1) % 4;
                        }
                    }
                    else
                        font.draw(matrices, "[N] Nofall", tw, th + 48, Formatting.RED.getColorValue());

                    if (Nofallval == 1 && Nofalltoggle) {
                        Nofall();
                        font.draw(matrices, "[N] Nofall", tw, th + 48, Formatting.GREEN.getColorValue());
                    }
                }

        } else System.out.println("Null printed");
    }
}















