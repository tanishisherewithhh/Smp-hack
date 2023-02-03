package net.fabricmc.smphack.Glider;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class GliderHack {
    public static void updateGlider() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;

        final float fallSpeed = 0.1F;
        final float moveSpeed = 1.2F;

        if (player != null) {
            Vec3d v = player.getVelocity();
            player.setVelocity(v.x, Math.max(v.y, -fallSpeed), v.z);
            player.airStrafingSpeed *= moveSpeed;
        }
    }
}
