package net.fabricmc.smphack.mixin;

import net.fabricmc.smphack.GeneralConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
//Freecam Mixin

@Mixin(value = WorldRenderer.class, priority = 1001)
public class WorldRendererMixin {

    @Redirect(method = "render", require = 0, at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/render/Camera;getFocusedEntity()Lnet/minecraft/entity/Entity;", ordinal = 3))
    private Entity allowRenderingClientPlayerInFreeCameraMode(Camera camera) {
        if (GeneralConfig.isEnabled()) {
            return MinecraftClient.getInstance().player;
        }

        return camera.getFocusedEntity();
    }

    @Redirect(method = "setupTerrain", require = 0, at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/render/BuiltChunkStorage;updateCameraPosition(DD)V"))
    private void preventRenderChunkPositionUpdates(net.minecraft.client.render.BuiltChunkStorage storage, double viewEntityX, double viewEntityZ) {
        if (!GeneralConfig.isEnabled()) {
            storage.updateCameraPosition(viewEntityX, viewEntityZ);
        }
    }
}
