package net.fabricmc.smphack.Hacks.Creativefly;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class AntiKick{
    MinecraftClient mc = MinecraftClient.getInstance();
    int tickCounter = 0;
    int antiKickInterval = 40;
    double antiKickDistance = 0.0035;

    private void setMotionY(double motionY) {
        mc.options.sneakKey.setPressed(false);
        mc.options.jumpKey.setPressed(false);

        assert mc.player != null;
        Vec3d velocity = mc.player.getVelocity();
        mc.player.setVelocity(velocity.x, motionY, velocity.z);
    }

    public void doAntiKick() {

        if (tickCounter > antiKickInterval + 2)
            tickCounter = 0;
        assert mc.player != null;
        switch (tickCounter) {
            case 0 -> {
                if (MinecraftClient.getInstance().options.sneakKey.isPressed()
                        && !MinecraftClient.getInstance().options.jumpKey.isPressed())
                    tickCounter = 3;
                else
                    setMotionY(-antiKickDistance);
            }

            case 1 -> setMotionY(antiKickDistance);

            case 2 -> setMotionY(0);
        }
        tickCounter++;
    }


}
