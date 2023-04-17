package net.fabricmc.smphack.Hacks.CrystalAura;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;

import java.util.List;

public class EndCrystalBreaker extends MainGui {
    private boolean isBreaking = false;
    private int CrystalDelay;
    private double distance;



    @Override
    public void toggled()
    {
        enabled=!enabled;
        assert MinecraftClient.getInstance().player!=null;
        if (MinecraftClient.getInstance().player.isDead()) {
            stopBreaking();
        }
        else {
            if (enabled) {
                startBreaking();
            } else {
                stopBreaking();
            }
        }
    }

    private void startBreaking() {
        isBreaking = true;
        CrystalDelay= GeneralConfig.getConfig().getCrystalBreakDelay_in_seconds();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isBreaking) {
                    breakNextCrystal();
                    try {
                        Thread.sleep((CrystalDelay* 4L)+1); // Adjust delay time in milliseconds here
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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

        if (player == null) return;

        distance=GeneralConfig.getConfig().getRange()+0.5;
        Box searchBox = new Box(player.getX() - distance, player.getY() - distance, player.getZ() - distance,
                player.getX() + distance, player.getY() + distance, player.getZ() + distance);

        assert mc.world != null;
        List<EndCrystalEntity> crystals = mc.world.getEntitiesByType(EntityType.END_CRYSTAL, searchBox, entity -> true);
if (mc.world==null){return;}
        for (EndCrystalEntity crystal : crystals) {
            if (crystal.getBoundingBox().intersects(searchBox)) {
                mc.player.swingHand(Hand.MAIN_HAND);
                assert mc.interactionManager != null;
                mc.interactionManager.attackEntity(player, crystal);
                return; // Only break one crystal per method call
            }
        }
    }
}



