package net.fabricmc.smphack.mixins;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.Hacks.Freecam.CameraEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PickFromInventoryC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(value = GameRenderer.class, priority = 1001)
public abstract class GameRendererMixin {
    @Shadow
    @Final
    MinecraftClient client;

    private Entity cameraEntityOriginal;

    @Inject(method = "renderWorld", at = @At(
            value = "INVOKE", shift = Shift.AFTER,
            target = "Lnet/minecraft/client/render/GameRenderer;updateTargetedEntity(F)V"))
    private void overrideRenderViewEntityPre(CallbackInfo ci) {
        if (GeneralConfig.isEnabled()) {
            Entity camera = CameraEntity.getCamera();

            if (camera != null) {
                this.cameraEntityOriginal = this.client.getCameraEntity();
                this.client.setCameraEntity(camera);
            }
        }
    }

    @Inject(method = "renderWorld", at = @At("RETURN"))
    private void overrideRenderViewEntityPost(CallbackInfo ci) {
        if (GeneralConfig.isEnabled() && this.cameraEntityOriginal != null) {
            this.client.setCameraEntity(this.cameraEntityOriginal);
            this.cameraEntityOriginal = null;
        }
    }

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    private void removeHandRendering(CallbackInfo ci) {
        if (GeneralConfig.isEnabled()) {
            ci.cancel();
        }
    }
    boolean Nohurtcam;
    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/RotationAxis;rotationDegrees(F)Lorg/joml/Quaternionf;"), method = "bobView", require = 1)
    public float changeBobIntensity(float value) {
        return  (0* value);
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    public void disableHurtCam(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        Nohurtcam=GeneralConfig.getConfig().getNoHurtCam();
        if (!Nohurtcam) ci.cancel();
    }
    private ArrayList<Packet<?>> packetsToSend = new ArrayList<>();

    @Inject(at = @At("TAIL"), method = "tick")
    private void onTick(CallbackInfo ci) {
        if (packetsToSend.isEmpty()) return;

        ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
        if (networkHandler == null) return;

        networkHandler.sendPacket(packetsToSend.get(0));
        packetsToSend.remove(0);
    }

    @Inject(at = @At("TAIL"), method = "showFloatingItem")
    private void onTotemUse(ItemStack floatingItem, CallbackInfo ci) {
        if (!floatingItem.isOf(Items.TOTEM_OF_UNDYING)) return;

        GameRenderer gameRenderer = (GameRenderer) ((Object) this);
        PlayerEntity player = gameRenderer.getClient().player;
        if (player == null) return;

        if (!player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) return;
        if (!player.hasStatusEffect(StatusEffects.REGENERATION)) return;

        int spareTotemSlot = getSlotWithSpareTotem(player.getInventory());
        if (spareTotemSlot == -1) {
            return;
        }

        restockSlot(player, spareTotemSlot);
    }

    private int getSlotWithSpareTotem(PlayerInventory inventory) {
        for (int i = 0; i < inventory.main.size(); i++) {
            ItemStack stack = inventory.main.get(i);

            if (!stack.isEmpty() && stack.getItem() == Items.TOTEM_OF_UNDYING) return i;
        }

        return -1;
    }

    private void restockSlot(PlayerEntity player, int totemSlot) {
        PlayerInventory playerInventory = player.getInventory();

        if (totemSlot < 9) {
            packetsToSend = new ArrayList<>();
            packetsToSend.add(new UpdateSelectedSlotC2SPacket(totemSlot));
            packetsToSend.add(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
            packetsToSend.add(new UpdateSelectedSlotC2SPacket(playerInventory.selectedSlot));
        } else {
            packetsToSend = new ArrayList<>();
            packetsToSend.add(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
            packetsToSend.add(new PickFromInventoryC2SPacket(totemSlot));
            packetsToSend.add(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
        }
    }
}
