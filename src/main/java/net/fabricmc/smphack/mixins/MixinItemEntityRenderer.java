package net.fabricmc.smphack.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public abstract class MixinItemEntityRenderer extends EntityRenderer<ItemEntity> {
    public MixinItemEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void renderName(ItemEntity itemEntity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        double d = itemEntity.squaredDistanceTo(this.dispatcher.camera.getPos());
        if (d <= 1000000.0D) {
            int itemCount = itemEntity.getStack().getCount();
            Text text = Text.literal(itemEntity.getName().getString() + " x" + itemCount);
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            float h = textRenderer.getWidth(text);
            matrices.push();
            matrices.translate(0.0D, itemEntity.getHeight() + 0.8F, 0.0D);
            matrices.multiply(this.dispatcher.getRotation());
            // Increase the scale values to make the text larger
            matrices.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            float i = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
            int j = (int)(i * 255.0F) << 24;
            textRenderer.draw(text,-h/2.0F,0.0F,553648127, false,matrix4f,vertexConsumers, TextRenderer.TextLayerType.NORMAL,0x50333333,j);
            textRenderer.draw(text,-h/2.0F,0.0F,-1, false,matrix4f,vertexConsumers, TextRenderer.TextLayerType.NORMAL,0x50333333,1);
            matrices.pop();
        }
    }
}