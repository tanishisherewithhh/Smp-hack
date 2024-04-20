package net.fabricmc.smphack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.smphack.Hacks.AimBot.AutoAim;
import net.fabricmc.smphack.Hacks.AntiHunger.AntiHunger;
import net.fabricmc.smphack.Hacks.AutoSprint.AutoSprint;
import net.fabricmc.smphack.Hacks.Autoclicker.Autoclicker;
import net.fabricmc.smphack.Hacks.CrystalAura.EndCrystalBreaker;
import net.fabricmc.smphack.Hacks.Fly.Fly;
import net.fabricmc.smphack.Hacks.Jesus.jesus;
import net.fabricmc.smphack.Hacks.Killaura.KillAura;
import net.fabricmc.smphack.Hacks.Nofall.Nofall;
import net.fabricmc.smphack.Hacks.Scaffold.Scaffold;
import net.fabricmc.smphack.Hacks.Speed.Speed;
import net.fabricmc.smphack.Hacks.SpeedMine.SpeedMine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;


// I know no one is going to check the source code but for the few people who do and might need help, I am glad for you;
// This is not supposed to be a full on hack client (atleast I think so)
// Use this for fun with your friends and on shitty servers with shitty AntiCheat.

@Environment(EnvType.CLIENT)
public class HUDoverlay implements HudRenderCallback {
    Fly fly = new Fly();
    Scaffold scaffold = new Scaffold();
    Nofall noFall = new Nofall();
    Speed speed = new Speed();
    public static jesus jes = new jesus();
    Autoclicker autoclicker=new Autoclicker();
    EndCrystalBreaker endCrystalBreaker = new EndCrystalBreaker();
    KillAura killAura = new KillAura();
    AutoAim Aimbot = new AutoAim();
    AutoSprint autoSprint = new AutoSprint();
    AntiHunger antiHunger=new AntiHunger();
    rendertext text = new rendertext();

    int tw = 10;
    int th = 10;
    public static final KeyBinding FlyKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Fly toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "Hacks Keybind"
    ));

    public static final KeyBinding Nofallkey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Nofall toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            "Hacks Keybind"
    ));

    public static final KeyBinding Jesuskey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Jesus toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "Hacks Keybind"
    ));
    public static final KeyBinding SpeedKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Speed toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "Hacks Keybind"
    ));

    public static final KeyBinding AutoCrystalBreakerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "AutoCrystal Breaker toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "Hacks Keybind"
    ));

    public static final KeyBinding KillauraKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Killaura toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "Hacks Keybind"
    ));

    public static final KeyBinding AimBotKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "AimBot toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "Hacks Keybind"
    ));
    public static final KeyBinding ScaffoldKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Scaffold toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Y,
            "Hacks Keybind"
    ));
    public static final KeyBinding AutoClickerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "AutoClicker for autocrystal",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "Smphack Mod"
    ));
    public static final KeyBinding ConfigScreenOpenerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Smphack Config Opener",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "Smphack Mod"
    ));


    MinecraftClient mc = MinecraftClient.getInstance();


    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        //To avoid stupid crashes
        if (mc != null )
        {
            assert mc.player != null;
            assert MinecraftClient.getInstance().world != null;


            //To stop rendering if debug screen or F3 menu is enabled
           if (mc.getDebugHud().shouldShowDebugHud()) {return;}
           //To avoid null crashes
            if (Formatting.RED.getColorValue() == null || Formatting.BLUE.getColorValue() == null || Formatting.WHITE.getColorValue() == null || Formatting.GREEN.getColorValue() == null)
            {
                return;
            }
            TextRenderer font = mc.textRenderer;
            ClientPlayerEntity player= MinecraftClient.getInstance().player;
            if(font==null || player==null){return;}
            SpeedMine Speedmine = new SpeedMine();
            Speedmine.toggled();


            text.run();

            text.renderFreecam(drawContext,tw,th,font);
            text.renderFlyMode(drawContext,tw,th,font,fly);
            text.renderJesus(drawContext,tw,th,font,jes);
            text.renderNofall(drawContext,tw,th,font,noFall);
            text.renderAutoCrystalBreaker(drawContext,tw,th,font,endCrystalBreaker);
            text.renderKillAura(drawContext,tw,th,font,killAura);
            text.renderSpeed(drawContext,tw,th,font,speed);
            text.renderAutoClicker(drawContext,tw,th,font);
            text.renderAimBot(drawContext,tw,th,font,Aimbot);
            text.renderScaffold(drawContext,th,tw,font, scaffold);

            text.AutoSprint(autoSprint);
            text.AntiHunger(antiHunger);
            text.AutoClicker(autoclicker);
            text.NoWeather();
            text.Fullbright();


        }
    }
}

