package net.fabricmc.smphack.Hacks.Speed;

import net.fabricmc.smphack.ConfigController;
import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;

public class Speed extends MainGui
{
    MinecraftClient mc = MinecraftClient.getInstance();
    public float walkspeed;
    float SpeedMultiplier=1.2F;



    @Override
   public void toggled()
   {
       enabled=!enabled;
   }

   @Override
    public void update()
   {
       assert mc.player != null;
       if(ConfigController.getConfig()!=null)

       {
          SpeedMultiplier = ConfigController.getConfig().speed_multiplier;
       }
       if(!mc.player.isSneaking() && mc.player.isOnGround() /**&& mc.player.isSprinting()**/)
       {
           //mc.player.jump();
           PlayerEntity player = MinecraftClient.getInstance().player;
           assert player != null;
           double speed = player.getVelocity().length();
           speed *= SpeedMultiplier;
           player.setVelocity(player.getVelocity().normalize().multiply(speed));
           HungerManager hungerManager = player.getHungerManager();
           hungerManager.add(0, -3);




       }



   }

}
