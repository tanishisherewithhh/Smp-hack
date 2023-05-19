package net.fabricmc.smphack.Hacks.CrystalAura;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.UUID;

public class EndCrystalBreaker extends MainGui {
    private boolean isBreaking = false;
    private int CrystalDelay;
    private static double distance;
    private static boolean AntiSuicide;
    private static boolean OnlyOwn;
    private static int ASdamage;
    static double x ,y,z;
    private static Vec3d vec3d = new Vec3d(0,0,0);
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
        assert mc.world != null;
        if (player == null || mc.world==null) return;

        AntiSuicide=GeneralConfig.getConfig().getAntiSuicide();
        OnlyOwn=GeneralConfig.getConfig().getOnlyOwn();

        x=player.getX();
        y=player.getY();
        z=player.getZ();


        Box searchBox = new Box(x - distance, y - distance, z - distance, x + distance, y + distance, z + distance);
        List<EndCrystalEntity> crystals = mc.world.getEntitiesByType(EntityType.END_CRYSTAL, searchBox, entity -> true);
        distance=GeneralConfig.getConfig().getRange()+0.5;
        for (EndCrystalEntity crystal : crystals) {
            if (EndCrystalDamage(player,crystal))
                {System.out.println("CHECKENDRCYTSTRL IS TRUE... RETURNING");return;}
            else if (crystal.getBoundingBox().intersects(searchBox)) {
                mc.player.swingHand(Hand.MAIN_HAND);
                assert mc.interactionManager != null;
                mc.interactionManager.attackEntity(player, crystal);
                return; // Only break one crystal per method call
            }
        }
    }
    public boolean EndCrystalDamage(PlayerEntity player,EndCrystalEntity entity) {
        double PDistance = Math.sqrt(player.distanceTo(entity));
        if (AntiSuicide) {
                if (PDistance > 15) {return false;}
                if (player.isCreative())return false;
                double damage = 6 * (1.01 - (PDistance / 5.5));
                ASdamage=GeneralConfig.getConfig().getASdamage();
                System.out.println("ASDAMAGE RETURNING: "+(damage>ASdamage));
                System.out.println("ASDAMAGE: "+ASdamage);
                System.out.println("Damage: "+damage);
                System.out.println("PDistance: "+PDistance);
                if (player.getHealth()<3){return true;}
                return damage > ASdamage;
            }
            if (OnlyOwn)
            {
                if (entity.getUuid() != null) {
                    UUID ownerUUID = entity.getUuid();
                        return player.getUuid().equals(ownerUUID);
                }
        }
        return false;
    }
}



