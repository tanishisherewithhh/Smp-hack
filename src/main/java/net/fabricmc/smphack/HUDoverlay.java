package net.fabricmc.smphack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.smphack.Hacks.AntiHunger.AntiHunger;
import net.fabricmc.smphack.Hacks.AutoSprint.AutoSprint;
import net.fabricmc.smphack.Hacks.Autoclicker.Autoclicker;
import net.fabricmc.smphack.Hacks.CrystalAura.EndCrystalBreaker;
import net.fabricmc.smphack.Hacks.Fly.Fly;
import net.fabricmc.smphack.Hacks.Jesus.jesus;
import net.fabricmc.smphack.Hacks.Killaura.KillAura;
import net.fabricmc.smphack.Hacks.Nofall.Nofall;
import net.fabricmc.smphack.Hacks.Reach.Reach;
import net.fabricmc.smphack.Hacks.Speed.Speed;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;


// I know no one is gonna check the source code but for the few people who do and might need help, I am glad for you;
// This shit took me much time. I didnt know How the fuck do you even use keybinds and what onHudRender was. Heck I didnt even know what is the main class of fabric is. Bruh.
// This is not supposed to be a full on hack client
// Use this for fun with your friends and on shitty servers with shitty AntiCheat.

@Environment(EnvType.CLIENT)
public class HUDoverlay implements HudRenderCallback {
    Fly fly = new Fly();
    Nofall noFall = new Nofall();
    Speed speed = new Speed();
    public static jesus jes = new jesus();
    Autoclicker autoclicker=new Autoclicker();
    EndCrystalBreaker endCrystalBreaker = new EndCrystalBreaker();
    KillAura killAura = new KillAura();
    Reach reach= new Reach();
    AutoSprint autoSprint = new AutoSprint();
    AntiHunger antiHunger=new AntiHunger();
    rendertext text = new rendertext();


    float prevRainGradient;
    int tw = 10;
    int th = 10;
    public static final KeyBinding FlyKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Fly toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "Imperials"
    ));

    public static final KeyBinding Nofallkey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
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
    public static final KeyBinding SpeedKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Speed toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "Imperials"
    ));

    public static final KeyBinding AutoCrystalBreakerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "AutoCrystal Breaker toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "Imperials"
    ));

    public static final KeyBinding KillauraKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Killaura toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "Imperials"
    ));

    public static final KeyBinding ReachKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Reach toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Y,
            "Imperials"
    ));

    public static final KeyBinding AutoClickerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "AutoClicker for autocrystal",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
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
            ClientPlayerEntity player= MinecraftClient.getInstance().player;
            if(font==null || player==null){return;}
            text.run();

            text.renderFreecam(matrices,tw,th,font);
            text.renderFlyMode(matrices,tw,th,font,fly);
            text.renderJesus(matrices,tw,th,font,jes);
            text.renderNofall(matrices,tw,th,font,noFall);
            text.renderReach(matrices,tw,th,reach,font);
            text.renderAutoCrystalBreaker(matrices,tw,th,font,endCrystalBreaker);
            text.renderKillAura(matrices,tw,th,font,killAura);
            text.renderSpeed(matrices,tw,th,font,speed);
            text.renderAutoClicker(matrices,tw,th,font);


            text.AutoSprint(autoSprint);
            text.AntiHunger(antiHunger);
            text.AutoClicker(autoclicker);
            text.NoWeather();
            text.Fullbright();
        }
    }


}

