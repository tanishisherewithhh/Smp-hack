package net.fabricmc.smphack.Hacks.CrystalAura;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EndCrystalBreaker extends MainGui {
    static double x, y, z;
    private static double distance;
    private static boolean AntiSuicide;
    private static boolean OnlyOwn;
    private static int ASdamage;
    private boolean isBreaking = false;
    private int CrystalDelay;
    private boolean SmartCrystal;
    private final Object lock = new Object();
    // Added variables for SmartCrystal mode
    private double smartCrystalRange;
    private double smartCrystalDamageThreshold = 5.0;

    @Override
    public void toggled() {
        enabled = !enabled;
        assert MinecraftClient.getInstance().player != null;
        if (MinecraftClient.getInstance().player.isDead()) {
            stopBreaking();
        } else if (enabled) {
            startBreaking();
        } else {
            stopBreaking();
        }
    }
    private void startBreaking() {
        isBreaking = true;
        Thread t = new Thread(() -> {
            while (isBreaking) {
                CrystalDelay = GeneralConfig.getConfig().getCrystalBreakDelay_in_seconds();
                long startTime = System.currentTimeMillis();
                breakNextCrystal();
                long endTime = System.currentTimeMillis();
                long ping = endTime - startTime;
                try {
                    // Adjust delay time based on ping
                    Thread.sleep(ping+(CrystalDelay * 4L)+2);
                } catch (InterruptedException e) {
                    // Handle the InterruptedException
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }


    private void stopBreaking() {
        isBreaking = false;
    }

    private void breakNextCrystal() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        assert mc.world != null;
        if (player == null || mc.world == null) return;

        AntiSuicide = GeneralConfig.getConfig().getAntiSuicide();
        OnlyOwn = GeneralConfig.getConfig().getOnlyOwn();
        SmartCrystal=GeneralConfig.getConfig().getSmartCrystal();
        smartCrystalDamageThreshold=GeneralConfig.getConfig().getSmartCrystalDamageThreshold();
        distance = GeneralConfig.getConfig().getRange() + 0.5;

        x = player.getX();
        y = player.getY();
        z = player.getZ();

        Box searchBox = new Box(x - distance, y - distance, z - distance, x + distance, y + distance, z + distance);
        List<EndCrystalEntity> crystals;
        synchronized (lock) {
            crystals = mc.world.getEntitiesByType(EntityType.END_CRYSTAL, searchBox, entity -> true);
        }

        // Group multiple crystals together
        Queue<EndCrystalEntity> crystalsToBreak = new ConcurrentLinkedQueue<>();
        if (SmartCrystal) {
            for (EndCrystalEntity crystal : crystals) {
                try {
                    if (shouldBreakSmartCrystal(player, crystal)) {
                        crystalsToBreak.add(crystal);
                    }
                } catch (ConcurrentModificationException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (EndCrystalEntity crystal : crystals) {
                try {
                    if (EndCrystalDamage(player, crystal)) {
                        return;
                    } else {
                        crystalsToBreak.add(crystal);
                    }
                } catch (ConcurrentModificationException e) {
                    e.printStackTrace();
                }
            }
        }
        // Break all crystals in a single batch
        for (EndCrystalEntity crystal : crystalsToBreak) {
            player.swingHand(Hand.MAIN_HAND);
            if (mc.interactionManager != null) {
                mc.interactionManager.attackEntity(player, crystal);
            }
        }
    }

    // Added method for SmartCrystal mode
    private boolean shouldBreakSmartCrystal(PlayerEntity player, EndCrystalEntity crystal) {
        smartCrystalRange=10f;
        Box searchBox = new Box(crystal.getX() - smartCrystalRange, crystal.getY() - smartCrystalRange, crystal.getZ() - smartCrystalRange, crystal.getX() + smartCrystalRange, crystal.getY() + smartCrystalRange, crystal.getZ() + smartCrystalRange);
        assert MinecraftClient.getInstance().world != null;
        List<Entity> entities = MinecraftClient.getInstance().world.getOtherEntities(player, searchBox);
        for (Entity entity : entities) {
            if (!(entity instanceof EndCrystalEntity) && !(entity instanceof ItemEntity)&& !(entity instanceof ExperienceOrbEntity)) {
                double distance = Math.sqrt(entity.distanceTo(crystal));
                double damage = 6 * (1.01 - (distance / 5.5));
                System.out.println("Damage: " + damage);
                if (damage > smartCrystalDamageThreshold) {
                    return true;
                }
            }
        }
        return false;
    }



    public boolean EndCrystalDamage(PlayerEntity player, EndCrystalEntity entity) {
        double PDistance = Math.sqrt(player.distanceTo(entity));
        ASdamage = GeneralConfig.getConfig().getASdamage();
        if (AntiSuicide) {
            if (PDistance > 15) {
                return false;
            }
            double damage = 6 * (1.01 - (PDistance / 5.5));
            if (player.getHealth() < ASdamage) {
                return true;
            }
            return damage > ASdamage;
        }
        if (OnlyOwn) {
                UUID ownerUUID = entity.getUuid();
                return player.getUuid().equals(ownerUUID);
        }
        return false;
    }
}



