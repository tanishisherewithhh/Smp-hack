package net.fabricmc.smphack.Hacks.CrystalAura;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

public class RenderUtils {
    public static void drawBox(BlockPos blockPos, float r, float g, float b) {
        RenderSystem.lineWidth(5);
        WorldRenderer.drawBox(new MatrixStack(),
                MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getLines()),
                blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1,
                r, g, b, 1f
        );
    }
}
