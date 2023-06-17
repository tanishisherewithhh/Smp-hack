package net.fabricmc.smphack.Hacks.Killaura;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.Hacks.Criticals.Criticals;
import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KillAura extends MainGui {

    private int delay;
    private double range;
    private boolean SwordAxeOnly;
    private boolean MultiTarget;
    private boolean hasSwordAxe;
    private boolean AutodelayKA;
   public static LivingEntity lowestHealthEntity = null;
    MinecraftClient mc = MinecraftClient.getInstance();


    @Override
    public void toggled() {
        super.toggled();
        assert MinecraftClient.getInstance().player!=null;
        SwordAxeOnly=GeneralConfig.getConfig().getSwordAxeOnly();
        if (MinecraftClient.getInstance().player.isDead()) {
            stop();
        }
        else if (enabled) {
            start();
        } else {
            stop();
        }
    }

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private void start() {
        executor.submit(() -> {
            while (enabled) {
                delay = GeneralConfig.getConfig().getKillAuraDelay();
                attackEntities();
                try {
                    Thread.sleep(delay * 100L); // Adjust delay time in milliseconds here
                } catch (InterruptedException e) {
                    // Handle the InterruptedException
                    // For example, log the error and stop the thread
                    e.printStackTrace();
                    enabled = false;
                }
            }
        });
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
        AutodelayKA=GeneralConfig.getConfig().getAutoDelayKA();
        MultiTarget=GeneralConfig.getConfig().getMultiTarget();

        Box searchBox = new Box(player.getX() - 4.5, player.getY() - 4.5, player.getZ() - 4.5,
                player.getX() + 4.5, player.getY() + 4.5, player.getZ() + 4.5);
        if (AutodelayKA) {
            setDelay(mc.player);
        }
        if(mc.world==null)return;
        List<LivingEntity> entities = mc.world.getEntitiesByClass(LivingEntity.class, searchBox, entity -> true);

        float lowestHealth = Float.MAX_VALUE;
        for (Entity entity : entities) {
            if (entity instanceof PlayerEntity target) {
                if (!isValidTarget(target)) {
                    continue;
                }
                if (MultiTarget) {
                    attackEntity(target);
                } else if (target.getHealth() < lowestHealth) {
                    lowestHealth = target.getHealth();
                    lowestHealthEntity = target;
                }
            }
            else if (entity instanceof LivingEntity target) {
                if (!isValidTarget(target)) {
                    continue;
                }
                if (MultiTarget) {
                    attackEntity(target);
                } else if (target.getHealth() < lowestHealth) {
                    lowestHealth = target.getHealth();
                    lowestHealthEntity = target;
                }
            }
        }
       if (!MultiTarget) {
           if (lowestHealthEntity != null) {
               attackEntity(lowestHealthEntity);
           }
       }
    }

    private boolean isValidTarget(LivingEntity entity) {
        if (entity.isDead() || entity.isInvulnerable() || entity == MinecraftClient.getInstance().player || entity.isInvisible() || !entity.isAttackable()) {
            return false;
        }
        if (entity instanceof PlayerEntity player) {
            if (player.isCreative() || player.isSpectator() || player.isDead() || player.isInvulnerable() || !player.isAttackable()) {
                return false;
            }
        }
        if (entity instanceof ArmorStandEntity) {
            return false;
        }
        return entity.getHealth() > 0 && !entity.isDead();
    }


    private void attackEntity(LivingEntity target) {
        ClientPlayerEntity player = mc.player;
        range=GeneralConfig.getConfig().getRange()+0.5;

        if (player == null) {
            return;
        }
        Vec3d pos = player.getPos();
        double distance = pos.distanceTo(target.getPos());
        if (SwordAxeOnly)
        {
            hasSwordAxe=hasSwordOrAxe(player);
            if (!hasSwordAxe)
            {
                return;
            }
        }
        if (distance < range) {
            // Make the hit a critical hit
            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, BlockPos.ORIGIN, Direction.DOWN));
            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, BlockPos.ORIGIN, Direction.DOWN));
            mc.player.setVelocity(0, 0.001, 0);

            player.addCritParticles(target);
            // Attack the target
            Criticals criticals=new Criticals();
            criticals.doCritical();
            mc.player.swingHand(Hand.MAIN_HAND,true);
            if (mc.interactionManager!=null) {
                mc.interactionManager.attackEntity(player, target);
            }
        }
    }

    public boolean hasSwordOrAxe(PlayerEntity player) {
        ItemStack item = player.getMainHandStack();
        Item type = item.getItem();
        return type == Items.WOODEN_SWORD || type == Items.STONE_SWORD || type == Items.IRON_SWORD || type == Items.GOLDEN_SWORD || type == Items.DIAMOND_SWORD || type == Items.NETHERITE_SWORD || type == Items.WOODEN_AXE || type == Items.STONE_AXE || type == Items.IRON_AXE || type == Items.GOLDEN_AXE || type == Items.DIAMOND_AXE || type == Items.NETHERITE_AXE;
    }
    public void setDelay(PlayerEntity player) {
        ItemStack item = player.getMainHandStack();
        Item type = item.getItem();
        if (type == Items.WOODEN_AXE || type == Items.STONE_AXE || type == Items.IRON_AXE || type == Items.GOLDEN_AXE || type == Items.DIAMOND_AXE || type == Items.NETHERITE_AXE) {
            delay=10;
            GeneralConfig.getConfig().setKillAuraDelay(10);
        } else if (type == Items.WOODEN_SWORD || type == Items.STONE_SWORD || type == Items.IRON_SWORD || type == Items.GOLDEN_SWORD || type == Items.DIAMOND_SWORD || type == Items.NETHERITE_SWORD) {
            delay = 6;
            GeneralConfig.getConfig().setKillAuraDelay(7);
        }
        else
        {
            delay=5;
            GeneralConfig.getConfig().setKillAuraDelay(3);
        }
    }

}
