package net.fabricmc.smphack.Hacks.Jesus;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import static net.fabricmc.smphack.Hacks.Fly.Modes.BoatFly.BoatFlyHack.player;

public class jesus extends MainGui {
    MinecraftClient mc = MinecraftClient.getInstance();
    private int speed;
    @Override
    public void update() {
        // check if sneaking
        if (mc.options.sneakKey.isPressed())
            return;
        onTick();

    }


    public void onTick() {
        assert mc.player != null;
        Entity e = mc.player.getRootVehicle();

        if (e.isSneaking() || e.fallDistance > 3f)
            return;

        speed= GeneralConfig.getConfig().getSpeedforjesus();

        if (isSubmerged(e.getPos().add(0, 0.3, 0))) {
            e.setVelocity(e.getVelocity().x, 0.08, e.getVelocity().z);
        } else if (isSubmerged(e.getPos().add(0, 0.1, 0))) {
            e.setVelocity(e.getVelocity().x, 0.05, e.getVelocity().z);
        } else if (isSubmerged(e.getPos().add(0, 0.05, 0))) {
            e.setVelocity(e.getVelocity().x, 0.01, e.getVelocity().z);
        } else if (isSubmerged(e.getPos())) {
            e.setVelocity(e.getVelocity().x, -0.005, e.getVelocity().z);
            e.setOnGround(true);
            if (mc.options.forwardKey.wasPressed()) {
                movePlayerForward(player, (speed/3F));
            }
        }
    }

    private boolean isSubmerged(Vec3d pos) {
        BlockPos bp = BlockPos.ofFloored(pos);
        assert mc.world != null;
        FluidState state = mc.world.getFluidState(bp);

        return !state.isEmpty() && pos.getY() - bp.getY() <= state.getHeight();
    }
    public static void movePlayerForward(PlayerEntity player, float speed) {
        float yaw = player.getYaw();
        double radians = Math.toRadians(yaw);
        double deltaX = (-speed/5) * Math.sin(radians);
        double deltaZ = (speed/5) * Math.cos(radians);
        player.addVelocity(deltaX, 0, deltaZ);
    }


}
