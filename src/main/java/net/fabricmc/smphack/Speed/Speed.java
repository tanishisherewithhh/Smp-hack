package net.fabricmc.smphack.Speed;

import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class Speed extends MainGui
{
    MinecraftClient mc = MinecraftClient.getInstance();
    public float walkspeed;

    @Override
   public void toggled()
   {
       enabled=!enabled;
   }

   @Override
    public void update()
   {
       assert mc.player != null;
       if(!mc.player.isSneaking() && mc.player.isOnGround() /**&& mc.player.isSprinting()**/)
       {
           //mc.player.jump();
           PlayerEntity player = MinecraftClient.getInstance().player;
           assert player != null;
           double speed = player.getVelocity().length();
            speed *= 1.2;
           player.setVelocity(player.getVelocity().normalize().multiply(speed));
           //Vec3d motion = player.getPos().add(0, player.getHeight(), 0).subtract(player.prevX, player.prevY, player.prevZ).multiply(1.2);
           //motion = motion.multiply(1.2);
           //player.setPos(player.getX() + motion.x, player.getY() + motion.y, player.getZ() + motion.z);
           HungerManager hungerManager = player.getHungerManager();
           hungerManager.add(0, -3);




       }



   }

}
