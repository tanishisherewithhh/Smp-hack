package net.fabricmc.smphack.Hacks.fullbright;

import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class Fullbright extends MainGui {

    @Override
    public void update()
    {
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 500, 255));
    }
}
