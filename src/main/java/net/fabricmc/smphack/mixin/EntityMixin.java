package net.fabricmc.smphack.mixin;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.Hacks.Freecam.CameraEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//Freecam mixin

@Mixin(net.minecraft.entity.Entity.class)
public abstract class EntityMixin {

    @Shadow
    private float yaw;
    @Shadow
    private float pitch;
    @Shadow
    public float prevYaw;
    @Shadow
    public float prevPitch;

    private double forcedPitch;
    private double forcedYaw;

    @Shadow
    public abstract Vec3d getVelocity();

    @Shadow
    public abstract void setVelocity(Vec3d velocity);

    @Inject(method = "updateVelocity", at = @At("HEAD"), cancellable = true)
    private void moreAccurateMoveRelative(float float_1, Vec3d motion, CallbackInfo ci) {
        if (GeneralConfig.isEnabled()) {
            CameraEntity camera = CameraEntity.getCamera();

            if (camera != null) {
                this.setVelocity(this.getVelocity().multiply(1D, 0D, 1D));
                ci.cancel();
            }
        }
    }

    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    private void overrideYaw(double yawChange, double pitchChange, CallbackInfo ci) {
        if (GeneralConfig.isEnabled()) {
            this.yaw = this.prevYaw;
            this.pitch = this.prevPitch;

            this.updateCustomRotations(yawChange, pitchChange);

            CameraEntity camera = CameraEntity.getCamera();

            if (camera != null) {
                camera.setRotations((float) this.forcedYaw, (float) this.forcedPitch);
            }

            ci.cancel();

            return;
        }

        this.forcedYaw = this.yaw;
        this.forcedPitch = this.pitch;
    }

    private void updateCustomRotations(double yawChange, double pitchChange) {
        this.forcedYaw += yawChange * 0.15D;

        this.forcedPitch = net.minecraft.util.math.MathHelper.clamp(this.forcedPitch + pitchChange * 0.15D, -(float) 90, (float) 90);
    }
}

