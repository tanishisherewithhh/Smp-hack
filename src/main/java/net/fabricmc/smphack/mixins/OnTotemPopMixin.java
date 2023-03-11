package net.fabricmc.smphack.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PickFromInventoryC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

//Autototem Mixin
@Mixin(GameRenderer.class)
public class OnTotemPopMixin {
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
            System.out.println("No Spare Totem Found");
            return;
        }

        System.out.println("Restocking Totem");
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
