package net.fabricmc.smphack;

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
import net.fabricmc.smphack.Hacks.Reach.Reach;
import net.fabricmc.smphack.Hacks.Speed.Speed;
import net.fabricmc.smphack.Hacks.fullbright.Fullbright;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Formatting;

import static net.fabricmc.smphack.HUDoverlay.*;
import static net.fabricmc.smphack.Hacks.Autoclicker.Autoclicker.isAutoclickerOn;

public class rendertext {
    MinecraftClient mc=MinecraftClient.getInstance();
    boolean textshadow;
    public void run()
    {
        textshadow=GeneralConfig.getConfig().getTextshadow();
    }
    public void renderFlyMode(MatrixStack matrices, int tw, int th, TextRenderer font, Fly fly) {
        if (FlyKey.wasPressed()) {
            fly.toggled();
        }

        Flymodes flymode = Flymodes.values()[fly.flybindcounter % Flymodes.values().length];
        String flymodeText = flymode.name().toUpperCase();
        String text = "Fly [" + flymodeText + "]";

        if (fly.enabled) {
            fly.update();
            flymode.fly();
            if (textshadow) {
                font.drawWithShadow(matrices, text, tw + 2, th + 12, Formatting.GREEN.getColorValue(),false);
            }
            else
            {
                font.draw(matrices, text, tw + 2, th + 12, Formatting.GREEN.getColorValue());
            }
        } else {
            if (textshadow) {
                font.drawWithShadow(matrices, String.valueOf(text), tw + 2, th + 12, Formatting.RED.getColorValue(),false);
            }
            else
            {
                font.draw(matrices, text, tw + 2, th + 12, Formatting.RED.getColorValue());
            }
        }
    }

    public void renderNofall(MatrixStack matrices, int tw, int th, TextRenderer font, Nofall noFall) {
        if (Nofallkey.wasPressed()) {
            noFall.toggled();
        }

        if (noFall.enabled) {
            noFall.update();
            if (textshadow) {
                font.drawWithShadow(matrices, "Nofall", tw, th + 48, Formatting.GREEN.getColorValue(),false);
            }
            else
            {
                font.draw(matrices, "Nofall", tw, th + 48, Formatting.GREEN.getColorValue());
            }
        } else {
            if (textshadow) {
                font.drawWithShadow(matrices, "Nofall", tw, th + 48, Formatting.RED.getColorValue(),false);
            }
            else
            {
                font.draw(matrices, "Nofall", tw, th + 48, Formatting.RED.getColorValue());

            }
        }
    }

    public void renderJesus(MatrixStack matrices, int tw, int th, TextRenderer font, jesus jesus) {
        if (Jesuskey.wasPressed()) {
            jesus.toggled();
        }

        if (jesus.enabled) {
            jes.update();
            if (textshadow) {
                font.drawWithShadow(matrices, "Jesus", tw, th + 36, Formatting.GREEN.getColorValue(),false);
            }
            else
            {
                font.draw(matrices, "Jesus", tw, th + 36, Formatting.GREEN.getColorValue());

            }
        } else {
            if (textshadow) {
                font.drawWithShadow(matrices, "Jesus", tw, th + 36, Formatting.RED.getColorValue(),false);
            }
            else
            {
                font.draw(matrices, "Jesus", tw, th + 36, Formatting.RED.getColorValue());
            }
        }
    }

    public void renderSpeed(MatrixStack matrices, int tw, int th, TextRenderer font, Speed speed) {
        if (SpeedKey.wasPressed()) {
            speed.toggled();
        }
        if (speed.enabled) {
            speed.update();
            if (textshadow) {
                font.drawWithShadow(matrices, "Speed", tw + 1, th + 24, Formatting.GREEN.getColorValue(),false);
            }
            else
            {
                font.draw(matrices, "Speed", tw + 1, th + 24, Formatting.GREEN.getColorValue());
            }
        } else {
            if (textshadow) {
                font.drawWithShadow(matrices, "Speed", tw + 1, th + 24, Formatting.RED.getColorValue(),false);
            }
            else
            {
                font.draw(matrices, "Speed", tw + 1, th + 24, Formatting.RED.getColorValue());
            }
            assert mc.player != null;
            mc.player.getAbilities().setWalkSpeed(speed.walkspeed);
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

    public void renderFreecam(MatrixStack matrices, int tw, int th, TextRenderer font) {
        boolean enabled = GeneralConfig.enabled;
        if (enabled) {
            if (textshadow) {
                font.drawWithShadow(matrices, "Freecam", tw, th + 60, Formatting.GREEN.getColorValue(),false);
            }
            else{
                font.draw(matrices, "Freecam", tw, th + 60, Formatting.GREEN.getColorValue());
            }
        } else {
            if (textshadow) {
                font.drawWithShadow(matrices, "Freecam", tw, th + 60, Formatting.RED.getColorValue(),false);
            }
            else
            {
                font.draw(matrices, "Freecam", tw, th + 60, Formatting.RED.getColorValue());
            }
        }
    }

    public void NoWeather() {
        boolean noWeather = GeneralConfig.getConfig().getNoWeather();
        if (noWeather) {
            NoWeather Noweather = new NoWeather();
            Noweather.update();
        } else {
            assert MinecraftClient.getInstance().world != null;
            if (MinecraftClient.getInstance().world.isRaining()) {
                MinecraftClient.getInstance().world.setRainGradient(0);
            }
        }
    }

    public void renderAutoCrystalBreaker(MatrixStack matrices, int tw, int th, TextRenderer font, EndCrystalBreaker endCrystalBreaker) {
        if (AutoCrystalBreakerKey.wasPressed()) {
            endCrystalBreaker.toggled();
        }
        if (endCrystalBreaker.enabled) {
            if (textshadow) {
                font.drawWithShadow(matrices, "AutoCrystal", tw, th + 72, Formatting.GREEN.getColorValue(),false);
            }
            else
            {
                font.draw(matrices, "AutoCrystal", tw, th + 72, Formatting.GREEN.getColorValue());
            }
        } else {
            if (textshadow) {
                font.drawWithShadow(matrices, "AutoCrystal", tw, th + 72, Formatting.RED.getColorValue(),false);
            }
            else
            {
                font.draw(matrices, "AutoCrystal", tw, th + 72, Formatting.RED.getColorValue());
            }
        }
    }

    public void renderKillAura(MatrixStack matrices, int tw, int th, TextRenderer font, KillAura killAura) {
        if (KillauraKey.wasPressed()) {
            killAura.toggled();
        }
        if (killAura.enabled) {
            if (textshadow) {
                font.drawWithShadow(matrices, "KillAura", tw, th + 84, Formatting.GREEN.getColorValue(),false);
            }
            else
            {
                font.draw(matrices, "KillAura", tw, th + 84, Formatting.GREEN.getColorValue());
            }
        } else {
            if (textshadow) {
                font.drawWithShadow(matrices, "KillAura", tw, th + 84, Formatting.RED.getColorValue(),false);
            }
            else
            {
                font.draw(matrices, "KillAura", tw, th + 84, Formatting.RED.getColorValue());
            }
        }
    }

    public void renderReach(MatrixStack matrices, int tw, int th, Reach reach, TextRenderer font) {
        if (ReachKey.wasPressed()) {
            reach.toggled();
        }
        if (reach.enabled) {
            if (textshadow) {
                font.drawWithShadow(matrices, "Reach", tw, th + 108, Formatting.GREEN.getColorValue(),false);
            }
            else
            {
                font.draw(matrices, "Reach", tw, th + 108, Formatting.GREEN.getColorValue());
            }
        } else {
            if (textshadow) {
                font.drawWithShadow(matrices, "Reach", tw, th + 108, Formatting.RED.getColorValue(),false);
            }
            else
            {
                font.draw(matrices, "Reach", tw, th + 108, Formatting.RED.getColorValue());
            }
        }
    }

    public void AutoSprint(AutoSprint autoSprint) {
        autoSprint.update();
    }
    public void AntiHunger(AntiHunger antiHunger) {
        antiHunger.toggled();
    }

    public void AutoClicker(Autoclicker Autoclicker) {
        Autoclicker.update();
    }
    public void renderAutoClicker(MatrixStack matrices, int tw, int th,TextRenderer font) {
        if (isAutoclickerOn) {
            if (textshadow) {
                font.drawWithShadow(matrices, "AutoClicker", tw, th + 96, Formatting.GREEN.getColorValue(),false);
            }
            else
            {
                font.draw(matrices, "AutoClicker", tw, th + 96, Formatting.GREEN.getColorValue());
            }
        } else {
            if (textshadow) {
                font.drawWithShadow(matrices, "AutoClicker", tw, th + 96, Formatting.RED.getColorValue(),false);
            }
            else
            {
                font.draw(matrices, "AutoClicker", tw, th + 96, Formatting.RED.getColorValue());
            }
        }
    }
    
    
}
