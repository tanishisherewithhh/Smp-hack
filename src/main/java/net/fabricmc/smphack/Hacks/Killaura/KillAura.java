package net.fabricmc.smphack.Hacks.Killaura;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.Hacks.Criticals.Criticals;
import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class KillAura extends MainGui {

    private int delay;
    private double range;

    @Override
    public void toggled() {
        super.toggled();
        assert MinecraftClient.getInstance().player!=null;
        if (MinecraftClient.getInstance().player.isDead()) {
            stop();
        }
        else {
            if (enabled) {
                start();
            } else {
                stop();
            }
        }
    }

    private void start() {
        Thread thread = new Thread(() -> {
            delay= GeneralConfig.getConfig().getKillAuraDelay();
            while (enabled) {
                attackEntities();
                try {
                    Thread.sleep(delay*100); // Adjust delay time in milliseconds here
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void stop() {
        enabled = false;
    }

    private void attackEntities() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;

        if (player == null) {
            return;
        }

        Box searchBox = new Box(player.getX() - 4.5, player.getY() - 4.5, player.getZ() - 4.5,
                player.getX() + 4.5, player.getY() + 4.5, player.getZ() + 4.5);

        assert mc.world != null;
        List<LivingEntity> entities = mc.world.getEntitiesByClass(LivingEntity.class, searchBox, entity -> true);

        for (Entity entity : entities) {
            if (entity instanceof PlayerEntity target) {
                if (!isValidTarget(target)) {
                    continue;
                }
                attackEntity(target);
            } else if (entity instanceof LivingEntity target) {
                if (!isValidTarget(target)) {
                    continue;
                }
                attackEntity(target);
            }
        }
    }

    private boolean isValidTarget(LivingEntity entity) {
        if (entity.isDead() || entity.isInvulnerable() || entity == MinecraftClient.getInstance().player) {
            return false;
        }
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (player.isCreative() || player.isSpectator() || player.isDead() || player.isInvulnerable()) {
                return false;
            }
        }
        return entity.getHealth() > 0 && !entity.isDead();
    }


    private void attackEntity(LivingEntity target) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;

        if (player == null) {
            return;
        }

        Vec3d pos = player.getPos();
        double distance = pos.distanceTo(target.getPos());
        range=GeneralConfig.getConfig().getRange()+0.5;
        if (distance < range) {
            // Make the hit a critical hit
            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, BlockPos.ORIGIN, Direction.DOWN));
            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, BlockPos.ORIGIN, Direction.DOWN));
            mc.player.setVelocity(0, 0.05, 0);

            //player.addCritParticles(target);
            // Attack the target
            Criticals criticals=new Criticals();
            criticals.doCritical();
            assert mc.interactionManager != null;
            mc.interactionManager.attackEntity(player, target);
        }
    }


}
