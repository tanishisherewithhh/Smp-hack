package net.fabricmc.smphack.Hacks.Fly.Modes.Elytraboost;

import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ElytraBoost extends MainGui {
    private int jumpTimer;


    static MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public void toggled()
    {
        enabled=!enabled;
        if (enabled)
        {
            jumpTimer = 0;
        }
    }

    @Override
    public void update()
    {
        if(jumpTimer > 0)
            jumpTimer--;

        assert mc.player != null;
        ItemStack chest = mc.player.getEquippedStack(EquipmentSlot.CHEST);
        if(chest.getItem() != Items.ELYTRA)
            return;

        if(mc.player.isFallFlying())
        {
            if(mc.player.isTouchingWater())
            {
                sendStartStopPacket();
                return;
            }

            controlSpeed();
            controlHeight();
            return;
        }

        if(ElytraItem.isUsable(chest) && mc.options.jumpKey.isPressed())
            doInstantFly();
    }

    private void sendStartStopPacket()
    {
        assert mc.player != null;
        ClientCommandC2SPacket packet = new ClientCommandC2SPacket(mc.player,
                ClientCommandC2SPacket.Mode.START_FALL_FLYING);
        mc.player.networkHandler.sendPacket(packet);
    }

    private void controlHeight()
    {
        //if(!heightCtrl.isChecked())
          //  return;

        assert mc.player != null;
        Vec3d v = mc.player.getVelocity();

        if(mc.options.jumpKey.isPressed())
            mc.player.setVelocity(v.x, v.y + 0.08, v.z);
        else if(mc.options.sneakKey.isPressed())
            mc.player.setVelocity(v.x, v.y - 0.04, v.z);
    }

    private void controlSpeed()
    {
        //if(!speedCtrl.isChecked())
          //  return;

        assert mc.player != null;
        float yaw = (float)Math.toRadians(mc.player.getYaw());
        Vec3d forward = new Vec3d(-MathHelper.sin(yaw) * 0.05, 0,
                MathHelper.cos(yaw) * 0.05);

        Vec3d v = mc.player.getVelocity();

        if(mc.options.forwardKey.isPressed())
            mc.player.setVelocity(v.add(forward));
        else if(mc.options.backKey.isPressed())
            mc.player.setVelocity(v.subtract(forward));
    }

    private void doInstantFly()
    {
        //if(!instantFly.isChecked())
         //   return;

        if(jumpTimer <= 0)
        {
            jumpTimer = 20;
            assert mc.player != null;
            mc.player.setJumping(false);
            mc.player.setSprinting(true);
            mc.player.jump();
        }

        sendStartStopPacket();
    }
}
