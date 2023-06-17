package net.fabricmc.smphack.Hacks.AimBot;

import net.fabricmc.smphack.MainGui;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;



public class AutoAim extends MainGui {

    MinecraftClient mc=MinecraftClient.getInstance();
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
            if (!(entity instanceof ArmorStandEntity)) {
                if (!(entity instanceof LivingEntity) || entity == player) continue;

                double distance = entity.getPos().distanceTo(playerPos);

                if (distance < range && distance < nearestDistance) {
                    // Check if there is a clear line of sight to the target
                    if (player.canSee(entity)) {
                        nearestDistance = distance;
                        nearestEntity = (LivingEntity) entity;
                    }
                }
            }
        }
        return nearestEntity;
    }
    public boolean willHitTarget(Vec3d startPos, Vec3d startVelocity, LivingEntity target) {
        Vec3d pos = startPos;
        Vec3d velocity = startVelocity;
        double drag = 0.99;
        double gravity = 0.05;

        for (int i = 0; i < 100; i++) {
            // Update position
            pos = pos.add(velocity);

            // Check if the projectile intersects with the target's hitbox
            if (target.getBoundingBox().contains(pos)) {
                return true;
            }

            // Update velocity
            velocity = velocity.multiply(drag);
            velocity = velocity.subtract(0, gravity, 0);

            // Check if the projectile is in water or lava
            assert mc.world != null;
            BlockState blockState = mc.world.getBlockState(BlockPos.ofFloored(pos));
            if (blockState.getBlock() == Blocks.WATER || blockState.getBlock() == Blocks.LAVA) {
                // Adjust velocity and gravity for water or lava
                drag = 1.03;
                gravity = 0.03;
            } else {
                // Reset drag and gravity for air
                drag = 0.99;
                gravity = 0.05;
            }
        }

        return false;
    }

    @Override
    public void update() {
        MinecraftClient client = MinecraftClient.getInstance();
            assert mc.player != null;
         Item mainHandItem = mc.player.getMainHandStack().getItem();
          if (!(mainHandItem instanceof RangedWeaponItem || mainHandItem instanceof TridentItem || mainHandItem == Items.EGG || mainHandItem == Items.SNOWBALL) || !mc.player.isUsingItem())
            return;

        if (client.player == null) {return;}

               PlayerEntity player = client.player;

                LivingEntity target = getNearestLivingEntity(player);
                if (target == null) {
                    return;
                }
                assert mc.player != null;
        // Calculate the initial velocity of the projectile
        Vec3d startVelocity;
        if (mainHandItem instanceof BowItem) {
            float bowPower = (72000 - mc.player.getItemUseTimeLeft()) / 20F;
            bowPower = Math.min(1f, (bowPower * bowPower + bowPower * 2) / 3);
            startVelocity = player.getRotationVec(1.0F).multiply(bowPower * 3);
        } else if (mainHandItem instanceof TridentItem) {
            float tridentPower = (mc.player.getItemUseTimeLeft()) / 10F;
            tridentPower = Math.min(1f, (tridentPower * tridentPower + tridentPower * 2) / 3);
            startVelocity = player.getRotationVec(1.0F).multiply(tridentPower * 2.5);
        } else if (mainHandItem == Items.EGG || mainHandItem == Items.SNOWBALL) {
            startVelocity = player.getRotationVec(1.0F).multiply(1.5);
        } else {
            return;
        }

        // Check if the projectile will hit the target
        if (!willHitTarget(player.getPos(), startVelocity, target)) {
            // Adjust player's yaw and pitch to aim at the target
            double dx = target.getX() - player.getX();
            double dy = target.getEyeY() - player.getEyeY();
            double dz = target.getZ() - player.getZ();
            double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
            float newPitch = (float) -Math.toDegrees(Math.atan2(dy, horizontalDistance));
            float newYaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90;

            // Adjust the yaw based on the target's movement
            Vec3d targetVelocity = target.getVelocity();
            newYaw += Math.toDegrees(Math.atan2(targetVelocity.z , targetVelocity.x ));

            player.setYaw(newYaw);
            newPitch += -0.13f * horizontalDistance- 0.5 * target.getHeight();
            player.setPitch(newPitch);
      }
    }
}
