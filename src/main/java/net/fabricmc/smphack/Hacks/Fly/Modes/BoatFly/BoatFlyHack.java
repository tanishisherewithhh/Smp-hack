package net.fabricmc.smphack.Hacks.Fly.Modes.BoatFly;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.smphack.GeneralConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
@Environment(EnvType.CLIENT)
public class BoatFlyHack {
    public static PlayerEntity player = MinecraftClient.getInstance().player;
        private static boolean setDefaultForwardSpeed=true;
        public static void updateBoatFly() {
            MinecraftClient mc = MinecraftClient.getInstance();
            ClientPlayerEntity player = mc.player;
            if (player == null || !player.hasVehicle()) {
                return;
            }
            int forwardSpeed = GeneralConfig.getConfig().getBoatflySpeed();

            Entity vehicle = player.getVehicle();
            if (vehicle == null) {
                return;
            }

            Vec3d velocity = vehicle.getVelocity();
            double directionX = velocity.x;
            double directionY = 0;
            double directionZ = velocity.z;

            if (mc.options.jumpKey.isPressed()) {
                directionY = 0.3;
            } else if (mc.options.sprintKey.isPressed()) {
                directionY = velocity.y;
            }

            if (mc.options.forwardKey.isPressed() && setDefaultForwardSpeed) {
                float yawRad = vehicle.getYaw() * MathHelper.RADIANS_PER_DEGREE;
                directionX = MathHelper.sin(-yawRad) * forwardSpeed*0.75;
                directionZ = MathHelper.cos(yawRad) * forwardSpeed*0.5;
             }

            vehicle.setVelocity(directionX, directionY, directionZ);
        }
    }
