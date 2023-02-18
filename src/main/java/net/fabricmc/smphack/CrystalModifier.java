package net.fabricmc.smphack;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;

import java.util.HashMap;

@Environment(EnvType.CLIENT)
public class CrystalModifier implements ClientModInitializer {
    public static final int delay = 0;
    public static final HashMap<Entity, Integer> toKill = new HashMap<>();

    public CrystalModifier() {
    }

    public void onInitializeClient() {
        toKill.clear();
    }
}