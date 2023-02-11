package net.fabricmc.smphack.Hacks.Speed;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class Speed extends MainGui {
    MinecraftClient mc = MinecraftClient.getInstance();
    public float walkspeed;
    float Speed = 1.2F;
    public String Speedmode;
    private boolean jumping;


    @Override
    public void toggled() {
        enabled = !enabled;
    }

    @Override
    public void update() {
        assert mc.player != null;
        if (mc.options.sneakKey.isPressed())
            return;

        if (GeneralConfig.getConfig() != null) {
            Speed = GeneralConfig.getConfig().getSpeed_multiplier();
            Speedmode = GeneralConfig.getConfig().getSpeedMode();
        }
        assert MinecraftClient.getInstance().player != null;
        HungerManager hungerManager = MinecraftClient.getInstance().player.getHungerManager();
        hungerManager.add(2, 5);

        /* Strafe */
        if (Objects.equals(Speedmode, "Strafe")) {
            if ((mc.player.forwardSpeed != 0 || mc.player.sidewaysSpeed != 0) /*&& mc.player.isOnGround()*/) {
                if (!mc.player.isSprinting()) {
                    mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
                }

                mc.player.setVelocity(new Vec3d(0, mc.player.getVelocity().y, 0));
                mc.player.updateVelocity(Speed,
                        new Vec3d(mc.player.sidewaysSpeed, 0, mc.player.forwardSpeed));

                double vel = Math.abs(mc.player.getVelocity().getX()) + Math.abs(mc.player.getVelocity().getZ());

                if (Objects.equals(Speedmode, "Strafe") && vel >= 0.12 && mc.player.isOnGround()) {
                    mc.player.updateVelocity(vel >= 0.3 ? 0.0f : 0.15f, new Vec3d(mc.player.sidewaysSpeed, 0, mc.player.forwardSpeed));
                    mc.player.jump();
                }
            }
        }
        /* OnGround */
        else if (Objects.equals(Speedmode, "onGround")) {
            if (mc.options.jumpKey.isPressed() || mc.player.fallDistance > 0.25)
                return;

            double speeds = 0.85 + Speed / 30;

            if (jumping && mc.player.getY() >= mc.player.prevY + 0.399994D) {
                mc.player.setVelocity(mc.player.getVelocity().x, -0.9, mc.player.getVelocity().z);
                mc.player.setPos(mc.player.getX(), mc.player.prevY, mc.player.getZ());
                jumping = false;
            }

            if (mc.player.forwardSpeed != 0.0F && !mc.player.horizontalCollision) {
                if (mc.player.verticalCollision) {
                    mc.player.setVelocity(mc.player.getVelocity().x * speeds, mc.player.getVelocity().y, mc.player.getVelocity().z * speeds);
                    jumping = true;
                    //mc.player.jump();
                    // 1.0379
                }

                if (jumping && mc.player.getY() >= mc.player.prevY + 0.399994D) {
                    mc.player.setVelocity(mc.player.getVelocity().x, -100, mc.player.getVelocity().z);
                    jumping = false;
                }

            }
        }
        /* Bhop */
     else if (Objects.equals(Speedmode, "Bhop")) {
        if (mc.player.forwardSpeed > 0 && mc.player.isOnGround()) {
            double speeds = 0.65 + Speed / 30;

            mc.player.jump();
            mc.player.setVelocity(mc.player.getVelocity().x * speeds, 0.255556, mc.player.getVelocity().z * speeds);
            mc.player.sidewaysSpeed += 3.0F;
            mc.player.jump();
            mc.player.setSprinting(true);
        }
    }

    }
}
