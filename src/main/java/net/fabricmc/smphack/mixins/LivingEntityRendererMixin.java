package net.fabricmc.smphack.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin extends EntityRenderer<LivingEntity>  {

    public LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

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

    @Inject(method = "render", at = @At("HEAD"))
    private void renderHealth(LivingEntity livingEntity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        double d = livingEntity.squaredDistanceTo(this.dispatcher.camera.getPos());
        if (d <= 1000000.0D) {
            // Get the entity's health and format the text
            if(!(livingEntity instanceof PlayerEntity) && !(livingEntity instanceof ArmorStandEntity)) {
                int health = (int) livingEntity.getHealth();
                Text text = Text.literal("❤ " + health);
                TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
                float h = textRenderer.getWidth(text);
                matrices.push();
                matrices.translate(0.0D, livingEntity.getHeight() + 0.9F, 0.0D);
                matrices.multiply(this.dispatcher.getRotation());
                matrices.scale(-0.025F, -0.025F, 0.025F);
                Matrix4f matrix4f = matrices.peek().getPositionMatrix();
                // Draw the text with no background
                textRenderer.draw(text, -h / 2.0F, 0.0F, -1, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0x80333333, 0);
                matrices.pop();
            }
        }
    }

}