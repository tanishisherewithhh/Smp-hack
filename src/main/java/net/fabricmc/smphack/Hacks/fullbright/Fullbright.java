package net.fabricmc.smphack.Hacks.fullbright;

import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class Fullbright extends MainGui {

    MinecraftClient mc = MinecraftClient.getInstance();
    @Override
    public void update()
    {
        assert mc.player != null;
        mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION,500,254,true,false,false));
    }

}
