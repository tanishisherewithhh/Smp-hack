package net.fabricmc.smphack.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tanishisherewith.dynamichud.widget.Widget;
import com.tanishisherewith.dynamichud.widget.WidgetBox;
import com.tanishisherewith.dynamichud.widget.WidgetData;
import net.fabricmc.smphack.GeneralConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class MuppetWidget extends Widget {

    public static WidgetData<MuppetWidget> DATA = new WidgetData<>("Muppet","Shows a muppet on screen",MuppetWidget::new);

    public MuppetWidget(){
        this("Empty");
    }

    public MuppetWidget(String modID) {
        super(DATA,modID);
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY) {
        boolean showMuppet= GeneralConfig.getConfig().getShowMuppet();
        //SHOW MUPPET
        ClientPlayerEntity player=MinecraftClient.getInstance().player;
        if (showMuppet && player != null && player.getWorld() != null) {
            drawEntity(context,x + 25/2,y + 25/2, (int) (25*scale),player,mc.getTickDelta());
            widgetBox.setSizeAndPosition(x,y,25*scale,30*scale);
        }
    }
    public static void drawEntity(DrawContext context, int x, int y, int size, Entity entity, float delta) {
        float yaw = MathHelper.wrapDegrees(entity.prevYaw + (entity.getYaw() - entity.prevYaw) * delta);
        float pitch = entity.getPitch();

        Quaternionf quaternionf = (new Quaternionf()).rotateZ(3.1415927F);
        Quaternionf quaternionf2 = (new Quaternionf()).rotateX(-pitch * 0.017453292F); // Invert the pitch rotation
        quaternionf.mul(quaternionf2);
        float h = entity.getBodyYaw();
        float i = entity.getYaw();
        float j = entity.getPitch();
        float k = entity.prevYaw;
        float l = entity.getHeadYaw();
        entity.setBodyYaw(yaw);
        entity.setPitch(pitch);
        entity.setHeadYaw(entity.getYaw());
        entity.prevYaw = entity.getYaw();

        context.getMatrices().push();
        context.getMatrices().translate(x, y, 70.0);
        context.getMatrices().multiplyPositionMatrix((new Matrix4f()).scaling((float) size, (float) size, (float) (-size)));
        context.getMatrices().multiply(quaternionf);
        DiffuseLighting.method_34742();
        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        if (quaternionf2 != null) {
            quaternionf2.conjugate();
            entityRenderDispatcher.setRotation(quaternionf2);
        }

        entityRenderDispatcher.setRenderShadows(false);
        RenderSystem.runAsFancy(() -> entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, delta, context.getMatrices(), context.getVertexConsumers(), 15728880));
        context.draw();
        entityRenderDispatcher.setRenderShadows(true);
        context.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
        entity.setBodyYaw(h);
        entity.setYaw(i);
        entity.setPitch(j);
        entity.prevYaw = k;
        entity.setHeadYaw(l);
    }
}
