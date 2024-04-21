package net.fabricmc.smphack.Hacks;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.HUDoverlay;
import net.fabricmc.smphack.MainGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;

public class CrystalSwitch extends MainGui {

    public CrystalSwitch(){
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            boolean CrystalSwitch = GeneralConfig.getConfig().isCrystalSwitch();
            if(CrystalSwitch && HUDoverlay.endCrystalBreaker.enabled) {
                if (player.getStackInHand(hand).getItem() == Items.OBSIDIAN) {
                    switchToEndCrystal(player);
                }
            }
            return ActionResult.PASS;
        });
    }

    private void switchToEndCrystal(PlayerEntity player) {
        for (int i = 0; i < 9; i++) { // Loop over the hotbar slots
            if (player.getInventory().getStack(i).getItem().equals(Items.END_CRYSTAL)) {
                player.getInventory().selectedSlot = i;
                break;
            }
        }
    }
}
