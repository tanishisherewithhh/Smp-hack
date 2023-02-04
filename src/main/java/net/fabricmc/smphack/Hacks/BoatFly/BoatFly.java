package net.fabricmc.smphack.Hacks.BoatFly;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BoatFly {
    public static PlayerEntity player = MinecraftClient.getInstance().player;
    private static boolean setdefaultforwardSpeed;
    private static double defaultupwardSpeed = 0.3;
    private static double defaultforwardSpeed;

    public static void onUpdate() {
        if (!player.hasVehicle()) return;

        Entity vehicle = player.getVehicle();
        assert vehicle != null;
        Vec3d velocity = vehicle.getVelocity();


        double directionX = velocity.x;
        double directionY = 0;
        double directionZ = velocity.z;


        if (MinecraftClient.getInstance().options.jumpKey.isPressed()) directionY = defaultupwardSpeed;
        else if (MinecraftClient.getInstance().options.sprintKey.isPressed()) directionY = velocity.y;
        //change so that when arrow keys are pressed the velocity of the boat decreases or increases;

        if (MinecraftClient.getInstance().options.forwardKey.isPressed() && setdefaultforwardSpeed) {
            float yawRad = vehicle.getYaw() * MathHelper.RADIANS_PER_DEGREE;
            directionX = MathHelper.sin(-yawRad) * defaultforwardSpeed;
            directionZ = MathHelper.cos(yawRad) * defaultforwardSpeed;
        }

        vehicle.setVelocity(directionX, directionY, directionZ);
    }

    public void setdefaultupwardSpeed(double defaultupwardSpeed) {
        BoatFly.defaultupwardSpeed = defaultupwardSpeed;
    }

    public void setdefaultforwardSpeed(double defaultforwardSpeed) {
        BoatFly.defaultforwardSpeed = defaultforwardSpeed;
    }

    public void setsetdefaultforwardSpeed(boolean setdefaultforwardSpeed) {
        BoatFly.setdefaultforwardSpeed = setdefaultforwardSpeed;
    }
}