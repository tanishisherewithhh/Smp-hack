package net.fabricmc.smphack.mixin;

import net.fabricmc.smphack.Hacks.Freecam.CameraEntity;
import net.fabricmc.smphack.GeneralConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GameRenderer.class, priority = 1001)
public abstract class GameRendererMixin {
    @Shadow
    @Final
    MinecraftClient client;

    private Entity cameraEntityOriginal;

    @Inject(method = "renderWorld", at = @At(
            value = "INVOKE", shift = Shift.AFTER,
            target = "Lnet/minecraft/client/render/GameRenderer;updateTargetedEntity(F)V"))
    private void overrideRenderViewEntityPre(CallbackInfo ci) {
        if (GeneralConfig.isEnabled()) {
            Entity camera = CameraEntity.getCamera();

            if (camera != null) {
                this.cameraEntityOriginal = this.client.getCameraEntity();
                this.client.setCameraEntity(camera);
            }
        }
    }

    @Inject(method = "renderWorld", at = @At("RETURN"))
    private void overrideRenderViewEntityPost(CallbackInfo ci) {
        if (GeneralConfig.isEnabled() && this.cameraEntityOriginal != null) {
            this.client.setCameraEntity(this.cameraEntityOriginal);
            this.cameraEntityOriginal = null;
        }
//        else if (FeatureToggle.TWEAK_ELYTRA_CAMERA.getBooleanValue() && Hotkeys.ELYTRA_CAMERA.getKeybind().isKeybindHeld()) {
//            Entity entity = this.client.getCameraEntity();
//
//            if (entity != null) {
//                MiscUtils.setEntityRotations(entity, this.realYaw, this.realPitch);
//            }
//        }
    }

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    private void removeHandRendering(CallbackInfo ci) {
        if (GeneralConfig.isEnabled()) {
            ci.cancel();
        }
    }
}
