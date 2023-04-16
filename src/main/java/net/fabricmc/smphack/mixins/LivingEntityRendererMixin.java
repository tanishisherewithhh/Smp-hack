package net.fabricmc.smphack.mixins;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    @Redirect(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;getSquaredDistanceToCamera(Lnet/minecraft/entity/Entity;)D",
            ordinal = 0), method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z")
    private double adjustDistance(EntityRenderDispatcher render, Entity entity)
    {
        // pretend the distance is 1 so the check always passes

        return 1;
    }


    @Inject(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/MinecraftClient;getInstance()Lnet/minecraft/client/MinecraftClient;",
            ordinal = 0),
            method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z",
            cancellable = true)
    private void shouldForceLabel(LivingEntity e,
                                  CallbackInfoReturnable<Boolean> cir)
    {
        // return true immediately after the distance check
         cir.setReturnValue(true);
    }
}