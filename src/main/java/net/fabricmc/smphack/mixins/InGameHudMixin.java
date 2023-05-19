package net.fabricmc.smphack.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.smphack.config.ConfigUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    private ClientPlayerEntity player;
    private final double x = -30;
    private final double y = 5;

    @ModifyArg(method = "renderStatusBars", index = 10, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHealthBar(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIIZ)V"))
    public boolean renderHearts(boolean blinking) {
        return blinking;
    }

    @Shadow
    @Final
    private MinecraftClient client;
    private String ArmorDurabilityDisplay;
    private int color;


    @Inject(method = "renderStatusBars", at = @At("TAIL"))
    private void renderArmor(MatrixStack matrices, CallbackInfo ci) {
        PlayerEntity player = this.client.player;
        if (player != null) {
            int scaledWidth = this.client.getWindow().getScaledWidth();
            int scaledHeight = this.client.getWindow().getScaledHeight();
            int x = scaledWidth / 2 + 115;
            int y = scaledHeight;
            int variable=20;
            for (int i = 0; i < 4; i++) {
                ItemStack stack = player.getInventory().armor.get(3 - i);
                if (!stack.isEmpty()) {
                    RenderSystem.setShaderTexture(0, DrawableHelper.GUI_ICONS_TEXTURE);
                    ArmorDurabilityDisplay = String.valueOf(ConfigUtil.config.ArmorDurability);
                    double durabilityPercentage = ((double) stack.getMaxDamage() - (double) stack.getDamage()) / (double) stack.getMaxDamage();
                    String durabilityText = (int) (durabilityPercentage * 100.0D) + "%";
                    if (durabilityPercentage > 0.65D) color = 0x00FF00; // green
                    else if (durabilityPercentage > 0.25D) color = 0xFFFF00; // yellow
                    else color = 0xFF0000; // red
                    matrices.push();
                    switch (ArmorDurabilityDisplay)
                    {
                        case "Percent":
                            variable=20;
                            //Percent Display
                            matrices.scale(0.5f, 0.5f, 1.0f);
                            int textWidth = this.client.textRenderer.getWidth(durabilityText);
                            this.client.textRenderer.draw(matrices, durabilityText, (x + i * 20 + 12 - textWidth / 2) * 2, (y-26) * 2f, color);
                            break;
                        case "Bar":
                            variable=20;
                            // Bar display
                            int barWidth = (int)(durabilityPercentage * 16);
                            DrawableHelper.drawBorder(matrices, x + i * 20 -1 , (y-30)*2 , x + i * 20  + barWidth , (y-26)*2 , color);
                            break;
                        case "Number":
                            variable=23;
                            //Number display
                            matrices.scale(0.5f, 0.5f, 1.0f);
                            String currentDurabilityText = stack.getMaxDamage() - stack.getDamage() + "/" + stack.getMaxDamage();
                            textWidth = this.client.textRenderer.getWidth(currentDurabilityText);
                            int xCords = (x + i * variable + 12 - textWidth / 2) * 2;
                            if (stack.getMaxDamage() < 100) {
                                // Adjust x and y coordinates for armor with max durability of 100
                                this.client.textRenderer.draw(matrices, currentDurabilityText, xCords + 5, (y-26) * 2f, color);
                            } else if (stack.getMaxDamage() >= 100) {
                                // Adjust x and y coordinates for armor with max durability of 200
                                this.client.textRenderer.draw(matrices, currentDurabilityText, xCords +6.2f, (y-26) * 2f, color);
                            } else {
                                // Default x and y coordinates
                                this.client.textRenderer.draw(matrices, currentDurabilityText, xCords, (y-26) * 2f, color);
                            }
                            break;
                    }
                    matrices.pop();
                    this.client.getItemRenderer().renderInGui(matrices, stack, (x + i * variable - 1), y-20);
                }
            }
        }
    }
    @Inject(method = "renderStatusBars", at = @At("TAIL"))
    private void onRenderStatusBars(MatrixStack matrices, CallbackInfo ci) {
        PlayerEntity player = this.client.player;
        if (player != null) {
            int totemCount = player.getInventory().count(Items.TOTEM_OF_UNDYING);
            if (totemCount > 0) {
                int scaledWidth = this.client.getWindow().getScaledWidth();
                int scaledHeight = this.client.getWindow().getScaledHeight();
                int x = scaledWidth / 2 + 100;
                int y = scaledHeight - 20;
                this.client.getItemRenderer().renderInGui(matrices,new ItemStack(Items.TOTEM_OF_UNDYING), x-5, y);

                String countString = String.valueOf(totemCount);
                int countStringWidth = this.client.textRenderer.getWidth(countString);
                matrices.push();
                matrices.scale(0.5f, 0.5f, 1.0f);
                this.client.textRenderer.drawWithShadow(matrices, countString, (x+12-countStringWidth/2f)*2-12, (y-6)*2f, 0xFFFFFF);
                matrices.pop();
            }
        }
    }
}