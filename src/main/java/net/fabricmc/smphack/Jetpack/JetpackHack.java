/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.fabricmc.smphack.Jetpack;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class JetpackHack {
    private static final int SPACE_KEY = GLFW.GLFW_KEY_SPACE;
    private static final double JETPACK_MAX_SPEED = 0.3;

    public static void updateJetpack() {

        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player != null) if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), SPACE_KEY)) {
            Vec3d motion = player.getVelocity();

            Vec3d vel = new Vec3d(motion.x * 1.5, 0.25, motion.z * 1.5);
            if (vel.lengthSquared() > JETPACK_MAX_SPEED * JETPACK_MAX_SPEED)
                vel = vel.normalize().multiply(JETPACK_MAX_SPEED);

            player.setVelocity(vel);
        }
    }
}