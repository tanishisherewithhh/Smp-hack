package net.fabricmc.smphack.mixin;

import com.mojang.authlib.GameProfile;
import net.fabricmc.smphack.Hacks.Freecam.CameraEntity;
import net.fabricmc.smphack.GeneralConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//Freecam Mixin

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    ClientPlayerEntity player;

    @Shadow
    protected abstract boolean isCamera();

    @Shadow
    protected MinecraftClient client;

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Redirect(method = "tickMovement", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayerEntity;isCamera()Z"))
    private boolean preventVerticalMotion(ClientPlayerEntity player) {
        if (GeneralConfig.isEnabled()) {
            return false;
        }

        return this.isCamera();
    }

    private boolean preventFlyStateToggle(PlayerAbilities abilities) {
        if (GeneralConfig.isEnabled()) {
            return false;
        }

        return abilities.allowFlying;
    }

    @Inject(method = "tickNewAi", at = @At("RETURN"))
    private void preventJumpingInCameraMode(CallbackInfo ci) {
        if (GeneralConfig.isEnabled()) {
            this.jumping = false;
        }
    }
    @Inject(method = "sendMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"))
    private void sendMessage(Text message, CallbackInfo ci)
    {
        assert this.player!=null;
    }

    @Inject(method = "isSneaking", at = @At("HEAD"), cancellable = true)
    private void preventSneakingInCameraMode(CallbackInfoReturnable<Boolean> cir) {
        if (GeneralConfig.isEnabled()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ClientPlayerEntity player = client.player;

        assert player != null;
        CameraEntity.movementTick(player.input.sneaking, player.input.jumping);
    }

    @Override
    public boolean isSpectator() {
        return super.isSpectator() || GeneralConfig.isSpectator();
    }
}
