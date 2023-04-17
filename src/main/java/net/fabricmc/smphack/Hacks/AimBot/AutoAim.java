package net.fabricmc.smphack.Hacks.AimBot;

import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.math.Vec3d;

import static net.fabricmc.smphack.Hacks.AimBot.WorldUtils.mc;

public class AutoAim extends MainGui {

    @Override
    public void toggled() {
        enabled=!enabled;
    }
    private LivingEntity getNearestLivingEntity(PlayerEntity player) {
        double range = 64.0D;
        Vec3d playerPos = player.getPos();
        LivingEntity nearestEntity = null;
        double nearestDistance = Double.MAX_VALUE;

        assert MinecraftClient.getInstance().world != null;
        for (Entity entity : MinecraftClient.getInstance().world.getEntities()) {
            if (!(entity instanceof LivingEntity) || entity == player) continue;

            double distance = entity.getPos().distanceTo(playerPos);

            if (distance < range && distance < nearestDistance) {
                // Check if there is a clear line of sight to the target
                if (player.canSee((LivingEntity) entity)) {
                    nearestDistance = distance;
                    nearestEntity = (LivingEntity) entity;
                }
            }
        }

        return nearestEntity;
    }
    @Override
    public void update() {
        MinecraftClient client = MinecraftClient.getInstance();
            assert mc.player != null;
            if (!(mc.player.getMainHandStack().getItem() instanceof RangedWeaponItem) || !mc.player.isUsingItem())
                return;
            if (client.player == null) {return;}

               PlayerEntity player = client.player;

                LivingEntity target = getNearestLivingEntity(player);
                if (target == null) {
                    return;
                }
                assert mc.player != null;
                float velocity = (72000 - mc.player.getItemUseTimeLeft()) / 20F;
                velocity = Math.min(1f, (velocity * velocity + velocity * 2) / 3);

                // set position to aim at
                Vec3d newTargetVec = target.getPos().add(target.getVelocity());
                double d = mc.player.getEyePos().distanceTo(target.getBoundingBox().offset(target.getVelocity()).getCenter());
                double x = newTargetVec.x + (newTargetVec.x - target.getX()) * d - mc.player.getX();
                double y = newTargetVec.y + (newTargetVec.y - target.getY()) * d + target.getHeight() * 0.5 - mc.player.getY() - mc.player.getEyeHeight(mc.player.getPose());
                double z = newTargetVec.z + (newTargetVec.z - target.getZ()) * d - mc.player.getZ();

                // set yaw
                mc.player.setYaw((float) Math.toDegrees(Math.atan2(z, x)) - 90);

                // calculate needed pitch
                double hDistance = Math.sqrt(x * x + z * z);
                double hDistanceSq = hDistance * hDistance;
                float g = 0.006F;
                float velocitySq = velocity * velocity;
                float velocityPow4 = velocitySq * velocitySq;
                float neededPitch = (float) -Math.toDegrees(Math.atan((velocitySq - Math
                        .sqrt(velocityPow4 - g * (g * hDistanceSq + 2 * y * velocitySq)))
                        / (g * hDistance)));

                // set pitch
                if (Float.isNaN(neededPitch)) {
                    WorldUtils.facePos(target.getX(), target.getY() + target.getHeight() / 2 -2.25f, target.getZ());
                } else {
                    mc.player.setPitch(neededPitch-2f);
                }
    }
}
