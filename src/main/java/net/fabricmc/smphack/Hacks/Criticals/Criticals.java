package net.fabricmc.smphack.Hacks.Criticals;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class Criticals {

    MinecraftClient MC = MinecraftClient.getInstance();
    public void onLeftClick()
    {
        if(MC.crosshairTarget == null
                || MC.crosshairTarget.getType() != HitResult.Type.ENTITY
                || !(((EntityHitResult)MC.crosshairTarget)
                .getEntity() instanceof LivingEntity))
            return;

        doCritical();
    }

    public void doCritical()
    {

        assert MC.player != null;
       // if(!MC.player.isOnGround())
         //   return;

        if(MC.player.isTouchingWater() || MC.player.isInLava()){
            return;}

                doPacketJump();
    }

    private void doPacketJump()
    {
        assert MC.player != null;
        double posX = MC.player.getX();
        double posY = MC.player.getY();
        double posZ = MC.player.getZ();

        sendPos(posX, posY + 0.0625D, posZ, true);
        sendPos(posX, posY, posZ, false);
        sendPos(posX, posY + 1.1E-5D, posZ, false);
        sendPos(posX, posY, posZ, false);
    }

    private void sendPos(double x, double y, double z, boolean onGround)
    {
        assert MC.player != null;
        MC.player.networkHandler.sendPacket(
                new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, onGround));
    }
}
