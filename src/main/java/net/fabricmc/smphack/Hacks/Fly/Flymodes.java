package net.fabricmc.smphack.Hacks.Fly;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.Hacks.Fly.Modes.BoatFly.BoatFlyHack;
import net.fabricmc.smphack.Hacks.Fly.Modes.Creativefly.AntiKick;
import net.fabricmc.smphack.Hacks.Fly.Modes.Elytraboost.ElytraBoost;
import net.fabricmc.smphack.Hacks.Fly.Modes.Glider.GliderHack;
import net.fabricmc.smphack.Hacks.Fly.Modes.Jetpack.JetpackHack;
import net.minecraft.client.MinecraftClient;

public enum Flymodes {


    CreativeFly {
        @Override
        public void fly() {
            assert MinecraftClient.getInstance().player != null;
            Fly flight = new Fly();
            AntiKick antiKick = new AntiKick();
            MinecraftClient.getInstance().player.getAbilities().flying = flight.enabled;
            boolean antikick = GeneralConfig.getConfig().isEnableAntikick();
            if(antikick){
                antiKick.doAntiKick();
            }

        }
    },
    BoatFly {
        @Override
        public void fly() {BoatFlyHack.updateBoatFly();
        }
    },
    GLider {
        @Override
        public void fly() {
            GliderHack.updateGlider();
        }
    },
    Jetpack {
        @Override
        public void fly() {
            JetpackHack.updateJetpack();
        }
    },

    ElytraBoost{
        @Override
        public void fly()
        {
            net.fabricmc.smphack.Hacks.Fly.Modes.Elytraboost.ElytraBoost Elytraboost = new ElytraBoost();
            Elytraboost.update();
            Elytraboost.toggled();
        }
    };

    public abstract void fly();
}

