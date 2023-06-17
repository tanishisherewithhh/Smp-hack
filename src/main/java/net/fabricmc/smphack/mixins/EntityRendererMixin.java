package net.fabricmc.smphack.mixins;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.config.ControllersConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {

    MinecraftClient MC = MinecraftClient.getInstance();
    int ping;
    Text newText;
    boolean nametagshadow=false;
    private final Matrix4f emptyMatrix = new Matrix4f();
    @Shadow
    @Final
    protected EntityRenderDispatcher dispatcher;
    @Inject(at = @At("HEAD"),
            method = "renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            cancellable = true)
    private void onRenderLabelIfPresent(T entity, Text text,
                                        MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider,
                                        int i, CallbackInfo ci)
    {
        ci.cancel();
        if (MinecraftClient.getInstance().currentScreen instanceof InventoryScreen) return;
        // do NameTags adjustments
        assert GeneralConfig.getConfig()!=null;
        ControllersConfig.nametagcolour PlayernametagColor = GeneralConfig.getConfig().PlayerNametagcolour;
        ControllersConfig.nametagcolour EntitynametagColor = GeneralConfig.getConfig().EntityNametagcolour;
        int PlayercolourIndex = PlayernametagColor.ordinal();
        int EntitycolourIndex = EntitynametagColor.ordinal();
        ClientPlayerEntity player = MinecraftClient.getInstance().player;


        if(entity instanceof PlayerEntity) {
            Formatting Playernametagformatting = Formatting.byColorIndex(PlayercolourIndex);
            newText = Text.literal(entity.getName().getString()).formatted(Playernametagformatting);
        }
        else if (entity instanceof MobEntity || !entity.isPlayer() && !(entity instanceof ServerPlayerEntity))
        {
            Formatting Entitynametagformatting = Formatting.byColorIndex(EntitycolourIndex);
            newText = Text.literal(entity.getName().getString()).formatted(Entitynametagformatting);
        }
        else
        {
            newText=text;
        }
        if (entity instanceof PlayerEntity && entity instanceof MobEntity)
        {
            Formatting Entitynametagformatting = Formatting.byColorIndex(EntitycolourIndex);
            newText = Text.literal(entity.getName().getString()).formatted(Entitynametagformatting);
        }

            smphackxWurstRenderLabelIfPresent(entity, newText, matrixStack, vertexConsumerProvider, i);
            ci.cancel();

    }

    protected void smphackxWurstRenderLabelIfPresent(T entity, Text text,
                                             MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light)
    {
        nametagshadow=GeneralConfig.getConfig().getnametagshadow();
        // disable distance limit if configured in NameTags
        double distanceSq = dispatcher.getSquaredDistanceToCamera(entity);
        if(distanceSq > 4096)
            return;

        // disable sneaking changes if NameTags is enabled
        boolean notSneaky = !entity.isSneaky();

        float matrixY = entity.getHeight() + 0.5F;
        int labelY = "deadmau5".equals(text.getString()) ? -10 : 0;

        matrices.push();
        matrices.translate(0, matrixY, 0);
        matrices.multiply(dispatcher.getRotation());

        // adjust scale if NameTags is enabled
        float scale = 0.025F;
        assert MC.player != null;
        double distance = MC.player.distanceTo(entity);
            if(distance >5) {
                scale *= distance /10;
            }
           if (scale<=0.025F)
           {
               scale=0.025F;
           }
        matrices.scale(-scale, -scale, scale);

        Matrix4f matrix = emptyMatrix;
        float bgOpacity = MC.options.getTextBackgroundOpacity(0.25F);
        int bgColor = (int)(bgOpacity * 255F) << 24;
        TextRenderer tr = getTextRenderer();
        float labelX = -tr.getWidth(text) / 2;
        matrix=matrices.peek().getPositionMatrix();
        tr.draw(text, labelX, labelY, 0x20FFFFFF, false, matrix, vertexConsumers, TextLayerType.NORMAL, bgColor, LightmapTextureManager.MAX_LIGHT_COORDINATE);

        // use the see-through layer for text if configured in NameTags
        TextLayerType textLayer = TextLayerType.NORMAL;

        // draw text
        if(notSneaky) {
            tr.draw(text, labelX, labelY, 0xFFFFFFFF, false, matrix, vertexConsumers, textLayer, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE);
        }

        assert MinecraftClient.getInstance().player != null;
        PlayerListEntry entry = MinecraftClient.getInstance().player.networkHandler.getPlayerListEntry(entity.getUuid());
        if (entry != null) {
            ping = entry.getLatency();
        }
        String pingText= "["+ping+"]";
        float h = (float)(tr.getWidth(pingText)/2);
        int j = (int)(bgOpacity * 255.0F) << 24;

        if(entity instanceof PlayerEntity && entity.isPlayer()) {
            tr.draw(pingText, h + 8, (float) labelY + 9, -1, nametagshadow, matrix, vertexConsumers, TextLayerType.NORMAL, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE);
            tr.draw(pingText, h + 8, (float) labelY + 9, 0xFFFFFFFF, false, matrix, vertexConsumers, TextLayerType.NORMAL, j, LightmapTextureManager.MAX_LIGHT_COORDINATE);
        }


        tr.draw(text, labelX, labelY, 0xFFFFFFFF, nametagshadow, matrix, vertexConsumers, TextLayerType.NORMAL, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE);
        matrices.pop();

    }

    @Shadow
    public TextRenderer getTextRenderer()
    {
        return null;
    }



}