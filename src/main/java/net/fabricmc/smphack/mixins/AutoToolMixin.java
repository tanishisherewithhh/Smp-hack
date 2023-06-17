package net.fabricmc.smphack.mixins;

import net.fabricmc.smphack.GeneralConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class AutoToolMixin {
    @Inject(at = @At("HEAD"), method = "updateBlockBreakingProgress")
    private void updateBlockBreakingProgress(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        boolean autotool= GeneralConfig.getConfig().getAutoTool();
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        World world = client.world;
        assert world != null;
        if (autotool) {
            if (player != null && player.isAlive() && player.canHit()) {
                assert client.interactionManager != null;
                if (client.interactionManager.isBreakingBlock()) {
                    // Get the player inventory and the current item stack
                    PlayerInventory inventory = player.getInventory();
                    ItemStack currentStack = inventory.getMainHandStack();

                    // Initialize a variable to store the best tool index and effectiveness
                    int bestToolIndex = -1;
                    boolean bestToolEffective = false;

                    // Loop through the inventory slots
                    for (int i = 0; i < 9; i++) {
                        // Get the item stack in the slot
                        ItemStack stack = inventory.getStack(i);

                        // Check if the item stack is not empty and is a tool item
                        if (!stack.isEmpty() && stack.getItem() instanceof ToolItem) {
                            // Get the tool item and its effectiveness on the block
                            ToolItem tool = (ToolItem) stack.getItem();
                            boolean effective = tool.isSuitableFor(world.getBlockState(pos));

                            // Check if the tool is more effective than the best tool
                            if (effective && !bestToolEffective) {
                                // Update the best tool index and effectiveness
                                bestToolIndex = i;
                                bestToolEffective = true;
                            }
                        }
                    }

                    // Check if a better tool was found
                    if (bestToolIndex != -1) {
                        // Switch to the best tool slot
                        inventory.selectedSlot = bestToolIndex;
                    }
                }
            }
        }
    }
}
