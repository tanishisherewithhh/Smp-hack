package net.fabricmc.smphack.Hacks.Freecam;

import net.fabricmc.smphack.GeneralConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.stat.StatHandler;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class CameraEntity extends ClientPlayerEntity {
    private static CameraEntity camera;
    private static float forwardRamped;
    private static float strafeRamped;
    private static float verticalRamped;

    public CameraEntity(MinecraftClient mc, ClientWorld world, ClientPlayNetworkHandler nethandler, StatHandler stats, ClientRecipeBook recipeBook) {
        super(mc, world, nethandler, stats, recipeBook, false, false);
    }

    @Override
    public boolean isSpectator() {
        return true;
    }

    public static void movementTick(boolean sneak, boolean jump) {
        CameraEntity camera = getCamera();

        if (camera != null) {
            MinecraftClient mc = MinecraftClient.getInstance();
            ClientPlayerEntity player = mc.player;

            float forward = 0;
            float vertical = 0;
            float strafe = 0;

            GameOptions options = mc.options;
            if (options.forwardKey.isPressed()) {
                forward++;
            }
            if (options.backKey.isPressed()) {
                forward--;
            }
            if (options.leftKey.isPressed()) {
                strafe++;
            }
            if (options.rightKey.isPressed()) {
                strafe--;
            }
            if (options.jumpKey.isPressed()) {
                vertical++;
            }
            if (options.sneakKey.isPressed()) {
                vertical--;
            }

            float rampAmount = 0.15f;
            float speed = strafe * strafe + forward * forward;

            if (forward != 0 && strafe != 0) {
                speed = (float) Math.sqrt(speed * 0.6);
            } else {
                speed = 1;
            }

            forwardRamped = getRampedMotion(forwardRamped, forward, rampAmount) / speed;
            verticalRamped = getRampedMotion(verticalRamped, vertical, rampAmount);
            strafeRamped = getRampedMotion(strafeRamped, strafe, rampAmount) / speed;

            assert player != null;
            forward = player.isSprinting() ? forwardRamped * 2 : forwardRamped;

            camera.updateLastTickPosition();
            camera.handleMotion(forward, verticalRamped, strafeRamped);
        }
    }

    private static float getRampedMotion(float current, float input, float rampAmount) {
        if (input != 0) {
            if (input < 0) {
                rampAmount *= -1f;
            }

            if ((input < 0) != (current < 0)) {
                current = 0;
            }

            current = MathHelper.clamp(current + rampAmount, -1f, 1f);
        } else {
            current *= 0.5f;
        }

        return current;
    }

    private static double getMoveSpeed() {
        return GeneralConfig.getConfig().getFlySpeed() / 10.0;
    }

    private void handleMotion(float forward, float up, float strafe) {
        double xFactor = Math.sin(this.getYaw() * Math.PI / 180D);
        double zFactor = Math.cos(this.getYaw() * Math.PI / 180D);
        double scale = getMoveSpeed();

        double x = (double) (strafe * zFactor - forward * xFactor) * scale;
        double y = (double) up * scale;
        double z = (double) (forward * zFactor + strafe * xFactor) * scale;
        this.setVelocity(new Vec3d(x, y, z));

        this.move(MovementType.SELF, this.getVelocity());
    }

    private void updateLastTickPosition() {
        this.lastRenderX = this.getX();
        this.lastRenderY = this.getY();
        this.lastRenderZ = this.getZ();

        this.prevX = this.getX();
        this.prevY = this.getY();
        this.prevZ = this.getZ();

        this.prevYaw = this.getYaw();
        this.prevPitch = this.getPitch();

        this.prevHeadYaw = this.headYaw;
    }

    public void setRotations(float yaw, float pitch) {
        this.setYaw(yaw);
        this.setPitch(pitch);

        this.headYaw = this.getYaw();

        //this.prevRotationYaw = this.rotationYaw;
        //this.prevRotationPitch = this.rotationPitch;

        //this.prevRotationYawHead = this.rotationYaw;
        //this.setRenderYawOffset(this.rotationYaw);
    }

    private static CameraEntity create(MinecraftClient mc) {
        CameraEntity camera = new CameraEntity(mc, mc.world, mc.player.networkHandler, mc.player.getStatHandler(), mc.player.getRecipeBook());
        camera.noClip = true;

        ClientPlayerEntity player = mc.player;

        if (player != null) {
            camera.refreshPositionAndAngles(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
            camera.setRotations(player.getYaw(), player.getPitch());
        }

        return camera;
    }

    public static void createCamera(MinecraftClient mc) {
        camera = create(mc);
    }

    public static CameraEntity getCamera() {
        return camera;
    }

    public static void removeCamera() {
        camera = null;
    }
}
