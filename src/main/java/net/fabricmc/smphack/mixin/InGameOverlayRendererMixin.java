package net.fabricmc.smphack.mixin;

import net.fabricmc.smphack.ConfigController;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(InGameOverlayRenderer.class)
public abstract class InGameOverlayRendererMixin {
    @ModifyArg(
            method = "renderFireOverlay(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/util/math/MatrixStack;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/VertexConsumer;color(FFFF)Lnet/minecraft/client/render/VertexConsumer;"
            ),
            index = 3
    )
    private static float renderFireOverlay_opacity(float alpha) {
        return ConfigController.getConfig().fireOpacity;
    }

    private static double renderFireOverlay_translate(double y) {
        return -1.0D + ConfigController.getConfig().fireHeight;
    }
}