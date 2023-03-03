package net.fabricmc.smphack.mixins;

import net.fabricmc.smphack.GeneralConfig;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//Hurtcam mixin
@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    boolean Nohurtcam;
    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/RotationAxis;rotationDegrees(F)Lorg/joml/Quaternionf;"), method = "bobViewWhenHurt", require = 4)
    public float changeBobIntensity(float value) {
        return (float) (0* value);
    }

    @Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    public void disableHurtCam(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        Nohurtcam=GeneralConfig.getConfig().getNoHurtCam();
        if (!Nohurtcam) ci.cancel();
    }
}
