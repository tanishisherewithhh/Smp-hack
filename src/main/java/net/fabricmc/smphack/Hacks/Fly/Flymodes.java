package net.fabricmc.smphack.Hacks.Fly;

import net.fabricmc.smphack.ConfigController;
import net.fabricmc.smphack.Hacks.Creativefly.AntiKick;
import net.fabricmc.smphack.Hacks.Glider.GliderHack;
import net.fabricmc.smphack.Hacks.Jetpack.JetpackHack;
import net.minecraft.client.MinecraftClient;

public enum Flymodes {

    CreativeFly {
        @Override
        public void fly() {
            assert MinecraftClient.getInstance().player != null;
            Fly flight = new Fly();
            AntiKick antiKick = new AntiKick();
            MinecraftClient.getInstance().player.getAbilities().flying = flight.enabled;
            boolean antikick = ConfigController.getConfig().Antikick;
            if(antikick){
                antiKick.doAntiKick();
            }

        }
    },
    BoatFly {
        @Override
        public void fly() {
            net.fabricmc.smphack.Hacks.BoatFly.BoatFly.onUpdate();
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
    };

    public abstract void fly();
}

