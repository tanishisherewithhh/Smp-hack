package net.fabricmc.smphack.Fly;

import net.fabricmc.smphack.BoatFly.BoatFly;
import net.fabricmc.smphack.Creativefly.AntiKick;
import net.fabricmc.smphack.Glider.GliderHack;
import net.fabricmc.smphack.Jetpack.JetpackHack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import static net.fabricmc.smphack.Fly.Fly.AntikickKey;

public enum Flymodes {

    CreativeFly {
        @Override
        public void fly() {
            assert MinecraftClient.getInstance().player != null;
            Fly flight = new Fly();
            AntiKick antiKick = new AntiKick();
            if(flight.enabled) {
                MinecraftClient.getInstance().player.getAbilities().flying = true;
            }
            else
            {
                MinecraftClient.getInstance().player.getAbilities().flying = false;
            }


            if (AntikickKey.wasPressed()) {
                antiKick.toggled();
                MinecraftClient.getInstance().player.sendMessage(Text.of("Antikick ON"));
            }
            if(antiKick.enabled){
                antiKick.doAntiKick();
            }
            else {
                antiKick.CancelAntikick();//hopefully
            }

        }
    },
    BoatFly {
        @Override
        public void fly() {
            net.fabricmc.smphack.BoatFly.BoatFly.onUpdate();
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

