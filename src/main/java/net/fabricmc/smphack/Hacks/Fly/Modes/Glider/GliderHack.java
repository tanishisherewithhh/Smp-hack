package net.fabricmc.smphack.Hacks.Fly.Modes.Glider;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class GliderHack {
    static final float moveSpeed = 1.2F;
    public static void updateGlider() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        assert player != null;
        Vec3d v = player.getVelocity();

        if (player.isOnGround() || player.isTouchingWater() || player.isInLava()
                || player.isClimbing() || v.y >= 0)
            return;
        final float fallSpeed = 0.13F;

        player.setVelocity(v.x, Math.max(v.y, -fallSpeed), v.z);

    }
}
