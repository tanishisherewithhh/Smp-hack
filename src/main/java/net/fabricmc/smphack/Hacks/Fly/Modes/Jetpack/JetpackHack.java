package net.fabricmc.smphack.Hacks.Fly.Modes.Jetpack;

import net.fabricmc.smphack.GeneralConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class JetpackHack {
    private static final int SPACE_KEY = GLFW.GLFW_KEY_SPACE;
    static float JETPACK_MAX_SPEED;

    public static void updateJetpack() {

        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        JETPACK_MAX_SPEED = GeneralConfig.getConfig().getJETPACK_MAX_SPEED();
        if (player != null) if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), SPACE_KEY)) {
            Vec3d motion = player.getVelocity();
            Vec3d vel = new Vec3d(motion.x*(JETPACK_MAX_SPEED/2), 0.25, motion.z*(JETPACK_MAX_SPEED/2));
            if (vel.lengthSquared() > JETPACK_MAX_SPEED * JETPACK_MAX_SPEED)
                vel = vel.normalize().multiply(JETPACK_MAX_SPEED);

            player.setVelocity(vel);
        }
    }
}