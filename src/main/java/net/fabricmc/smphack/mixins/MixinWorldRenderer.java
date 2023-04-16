package net.fabricmc.smphack.mixins;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.Hacks.Killaura.KillAura;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.fabricmc.smphack.Hacks.Killaura.KillAura.lowestHealthEntity;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    KillAura killAura=new KillAura();
    @Inject(method = "renderEntity", at = @At("HEAD"))
    private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        if (entity != null) {
            if (!matrices.isEmpty()) {
                matrices.pop();
            }
            try {
                matrices.push();
                if (killAura.enabled && GeneralConfig.getConfig().getMultiTarget()) {
                    if (lowestHealthEntity != null) {
                        if (entity == lowestHealthEntity && lowestHealthEntity.isAlive()) {
                            if (vertexConsumers instanceof OutlineVertexConsumerProvider outlineVertexConsumers) {
                                int color = GeneralConfig.getConfig().getPlayernametagcolour();
                                int red = (color >> 16) & 0xFF;
                                int green = (color >> 8) & 0xFF;
                                int blue = color & 0xFF;
                                outlineVertexConsumers.setColor(red, green, blue, 255);
                            }
                        }
                    }
                }
            } finally {
                matrices.pop();
            }
        }
    }
    }
