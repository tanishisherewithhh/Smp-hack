package net.fabricmc.smphack;

import net.fabricmc.smphack.Hacks.AimBot.AutoAim;
import net.fabricmc.smphack.Hacks.AntiHunger.AntiHunger;
import net.fabricmc.smphack.Hacks.AutoSprint.AutoSprint;
import net.fabricmc.smphack.Hacks.Autoclicker.Autoclicker;
import net.fabricmc.smphack.Hacks.CrystalAura.EndCrystalBreaker;
import net.fabricmc.smphack.Hacks.Fly.Fly;
import net.fabricmc.smphack.Hacks.Fly.Flymodes;
import net.fabricmc.smphack.Hacks.Jesus.jesus;
import net.fabricmc.smphack.Hacks.Killaura.KillAura;
import net.fabricmc.smphack.Hacks.NoWeather.NoWeather;
import net.fabricmc.smphack.Hacks.Nofall.Nofall;
import net.fabricmc.smphack.Hacks.Scaffold.Scaffold;
import net.fabricmc.smphack.Hacks.Speed.Speed;
import net.fabricmc.smphack.Hacks.fullbright.Fullbright;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import static net.fabricmc.smphack.HUDoverlay.*;
import static net.fabricmc.smphack.Hacks.Autoclicker.Autoclicker.isAutoclickerOn;

public class rendertext {
    MinecraftClient mc = MinecraftClient.getInstance();
    String dText = "";
    boolean textshadow;

    public void displayText(DrawContext drawContext, int tw, int th, TextRenderer font, String displayText, boolean enabled,boolean shadow) {
        if (enabled) {
            drawContext.drawText(font,displayText,tw,th,Formatting.GREEN.getColorValue(),shadow);
        } else {
            drawContext.drawText(font,displayText,tw,th,Formatting.RED.getColorValue(),shadow);
        }
    }

    public void TextRender(DrawContext drawContext, int tw, int th, TextRenderer font, String displayText, boolean enabled, boolean Shadow) {
        displayText(drawContext,tw,th,font,displayText,enabled,Shadow);
    }

    public void run() {
        textshadow = GeneralConfig.getConfig().getTextshadow();
    }

    public void renderFlyMode(DrawContext drawcontext, int tw, int th, TextRenderer font, Fly fly) {
        if (FlyKey.wasPressed()) {
            fly.toggled();
        }

        Flymodes flymode = Flymodes.values()[fly.flybindcounter % Flymodes.values().length];
        String flymodeText = flymode.name().toUpperCase();
        String text = "Fly [" + flymodeText + "]";

        if (fly.enabled) {
            fly.update();
            flymode.fly();
            TextRender(drawcontext, tw + 2, th + 12, font, text, true, textshadow);
        } else {
            TextRender(drawcontext, tw + 2, th + 12, font, text, false, textshadow);
        }
    }

    public void renderNofall(DrawContext drawcontext, int tw, int th, TextRenderer font, Nofall noFall) {
        if (Nofallkey.wasPressed()) {
            noFall.toggled();
        }
        dText = "Nofall";

        if (noFall.enabled) {
            noFall.update();
            TextRender(drawcontext, tw, th + 48, font, dText, true, textshadow);
        } else {
            TextRender(drawcontext, tw, th + 48, font, dText, false, textshadow);
        }
    }
    public void renderScaffold(DrawContext drawcontext, int tw, int th, TextRenderer font, Scaffold scaffold) {
        if (ScaffoldKey.wasPressed()) {
            scaffold.toggled();
        }
        dText = "Scaffold";

        if (scaffold.enabled) {
            scaffold.update();
            TextRender(drawcontext, tw, th + 120, font, dText, true, textshadow);
        } else {
            TextRender(drawcontext, tw, th + 120, font, dText, false, textshadow);
        }
    }
    public void renderJesus(DrawContext drawcontext, int tw, int th, TextRenderer font, jesus jesus) {
        if (Jesuskey.wasPressed()) {
            jesus.toggled();
        }
        dText = "Jesus";
        if (jesus.enabled) {
            jes.update();
            TextRender(drawcontext, tw, th + 36, font, dText, true, textshadow);
        } else {
            TextRender(drawcontext, tw, th + 36, font, dText, false, textshadow);
        }
    }
    public void renderAimBot(DrawContext drawcontext, int tw, int th, TextRenderer font, AutoAim Aimbot) {
        if (AimBotKey.wasPressed()) {
            Aimbot.toggled();
        }
        dText = "AimBot";
        if (Aimbot.enabled) {
            Aimbot.update();
            TextRender(drawcontext, tw, th + 108, font, dText, true, textshadow);
        } else {
            TextRender(drawcontext, tw, th + 108, font, dText, false, textshadow);
        }
    }
    public void renderSpeed(DrawContext drawcontext, int tw, int th, TextRenderer font, Speed speed) {
        if (SpeedKey.wasPressed()) {
            speed.toggled();
        }
        dText = "Speed";
        if (speed.enabled) {
            speed.update();
            TextRender(drawcontext, tw, th + 24, font, dText, true, textshadow);
            assert mc.player != null;
            mc.player.getAbilities().setWalkSpeed(speed.walkspeed);
        } else {
            TextRender(drawcontext, tw, th + 24, font, dText, false, textshadow);

        }
    }

    public void Fullbright() {
        boolean fullbright = GeneralConfig.getConfig().getFullbright();
        if (fullbright) {
            Fullbright fullbright1 = new Fullbright();
            fullbright1.update();
        } else {
            assert mc.player != null;
            mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        }
    }

    public void renderFreecam(DrawContext drawcontext, int tw, int th, TextRenderer font) {
        boolean enabled = GeneralConfig.enabled;
        dText = "Freecam";
        TextRender(drawcontext, tw, th + 60, font, dText, enabled, textshadow);
    }

    public void NoWeather() {
        boolean noWeather = GeneralConfig.getConfig().getNoWeather();
        if (noWeather) {
            NoWeather Noweather = new NoWeather();
            Noweather.update();
        } else {
            assert MinecraftClient.getInstance().world != null;
            if (MinecraftClient.getInstance().world.isRaining()) {
                MinecraftClient.getInstance().world.setRainGradient(6);
            }
        }
    }
    public void renderAutoCrystalBreaker(DrawContext drawcontext, int tw, int th, TextRenderer font, EndCrystalBreaker endCrystalBreaker) {
        if (AutoCrystalBreakerKey.wasPressed()) {
            endCrystalBreaker.toggled();
        }
        dText = "AutoCrystal";
        TextRender(drawcontext, tw, th + 72, font, dText, endCrystalBreaker.enabled, textshadow);
    }

    public void renderKillAura(DrawContext drawcontext, int tw, int th, TextRenderer font, KillAura killAura) {
        if (KillauraKey.wasPressed()) {
            killAura.toggled();
        }
        dText = "KillAura";
        TextRender(drawcontext, tw, th + 84, font, dText, killAura.enabled, textshadow);
    }

    public void AutoSprint(@NotNull AutoSprint autoSprint) {
        autoSprint.update();
    }

    public void AntiHunger(@NotNull AntiHunger antiHunger) {
        antiHunger.toggled();
    }

    public void AutoClicker(@NotNull Autoclicker Autoclicker) {
        Autoclicker.update();
    }



    public void renderAutoClicker(DrawContext drawcontext, int tw, int th, TextRenderer font) {
        dText = "AutoClicker";
        TextRender(drawcontext, tw, th + 96, font, dText, isAutoclickerOn, textshadow);
    }

}
