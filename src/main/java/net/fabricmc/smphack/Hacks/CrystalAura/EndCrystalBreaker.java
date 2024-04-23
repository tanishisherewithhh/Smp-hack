package net.fabricmc.smphack.Hacks.CrystalAura;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EndCrystalBreaker extends MainGui {
    double x, y, z;
    private double distance;
    private boolean AntiSuicide;
    private boolean OnlyOwn;
    private int ASdamage;
    private boolean isBreaking = false;
    private int CrystalDelay;
    private boolean SmartCrystal;
    // Added variables for SmartCrystal mode
    private double smartCrystalRange;
    private double smartCrystalDamageThreshold = 5.0;
    private final Map<UUID, Integer> crystalAttackCount = new HashMap<>();
    private ScheduledExecutorService executor;

    public EndCrystalBreaker() {
        executor = Executors.newSingleThreadScheduledExecutor();
    }

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
        if (executor.isShutdown()) {
            executor = Executors.newSingleThreadScheduledExecutor();
        }
        CrystalDelay = GeneralConfig.getConfig().getCrystalBreakDelay_in_seconds();
        if (CrystalDelay > 0) {
            executor.scheduleAtFixedRate(this::breakNextCrystal, 0, CrystalDelay*4L + 2, TimeUnit.MILLISECONDS);
        } else {
            System.err.println("CrystalDelay must be greater than 0");
        }
    }



    private void stopBreaking() {
        isBreaking = false;
        executor.shutdownNow();
    }

    private void breakNextCrystal() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        crystalAttackCount.clear();

        if (player == null || mc.world == null) return;

        AntiSuicide = GeneralConfig.getConfig().getAntiSuicide();
        OnlyOwn = GeneralConfig.getConfig().getOnlyOwn();
        SmartCrystal=GeneralConfig.getConfig().getSmartCrystal();
        smartCrystalDamageThreshold=GeneralConfig.getConfig().getSmartCrystalDamageThreshold();
        distance = GeneralConfig.getConfig().getRange() + 0.5;

        x = player.getX();
        y = player.getY();
        z = player.getZ();
        if(player.getMainHandStack().getItem() == Items.END_CRYSTAL || player.getMainHandStack().getItem() == Items.OBSIDIAN) {
            Box searchBox = new Box(x - distance, y - distance, z - distance, x + distance, y + distance, z + distance);
            List<EndCrystalEntity> crystals = mc.world.getEntitiesByType(EntityType.END_CRYSTAL, searchBox, entity -> true);

            // Group multiple crystals together
            Queue<EndCrystalEntity> crystalsToBreak = new ConcurrentLinkedQueue<>();
            if (SmartCrystal) {
                for (EndCrystalEntity crystal : crystals) {
                    if (shouldBreakSmartCrystal(player, crystal)) {
                        crystalsToBreak.add(crystal);
                    }
                }
            } else {
                for (EndCrystalEntity crystal : crystals) {
                    if (EndCrystalDamage(player, crystal)) {
                        continue;
                    } else {
                        crystalsToBreak.add(crystal);
                    }
                }
            }
            // Break all crystals in a single batch
            for (EndCrystalEntity crystal : crystalsToBreak) {
                UUID crystalId = crystal.getUuid();
                int attackCount = crystalAttackCount.getOrDefault(crystalId, 0);
                if (attackCount < 2 && crystal.isAlive()) {  // Only attack a crystal up to 2 times
                    player.swingHand(Hand.MAIN_HAND);
                    Vec3d playerEyePos = player.getEyePos();
                    Vec3d crystalPos = crystal.getPos();
                    HitResult hitResult = mc.world.raycast(new RaycastContext(playerEyePos, crystalPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player));

                    if (hitResult.getType() == HitResult.Type.MISS) {
                        // There's a clear line of sight to the crystal
// Calculate the yaw and pitch to look at the End Crystal
                        PlayerMoveC2SPacket.LookAndOnGround packet = getLookAndOnGroundPacket(crystal, player);

// Send the packet to the server
                        Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendPacket(packet);
                        crystal.getBoundingBox().expand(0.5f);
                        if (mc.interactionManager != null) {
                            mc.interactionManager.attackEntity(player, crystal);
                        }
                        crystalAttackCount.put(crystalId, attackCount + 1);
                    }
                }
            }
        }
    }

    private static PlayerMoveC2SPacket.@NotNull LookAndOnGround getLookAndOnGroundPacket(EndCrystalEntity crystal, ClientPlayerEntity player) {
        double dx = crystal.getX() - player.getX();
        double dy = crystal.getY() - player.getY();
        double dz = crystal.getZ() - player.getZ();
        double distance = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float)(Math.atan2(dz, dx) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float)-(Math.atan2(dy, distance) * 180.0D / Math.PI);

// Create a new PlayerMoveC2SPacket to update the player's rotation on the server
        PlayerMoveC2SPacket.LookAndOnGround packet = new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, player.isOnGround());
        return packet;
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
        return false;
    }
}



