package net.fabricmc.smphack.mixins;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.Hacks.Freecam.CameraEntity;
import net.fabricmc.smphack.Hacks.RefillUtil.RefillUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//Freecam Mixin

@Mixin(ClientPlayerEntity.class)
@Environment(EnvType.CLIENT)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    ClientPlayerEntity player;
    @Inject(method = "handleStatus", at = @At("HEAD"))
    private void onEntityStatusT(byte status, CallbackInfo ci) {
        RefillUtil.parserPacket((ClientPlayerEntity) (Object) this, status);
    }
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

    // Needed for Baritone compatibility.
    @Inject(method = "isCamera", at = @At("HEAD"), cancellable = true)
    private void onIsCamera(CallbackInfoReturnable<Boolean> cir) {
        if (GeneralConfig.isEnabled() && this.equals(MinecraftClient.getInstance().player)) {
            cir.setReturnValue(true);
        }
    }
    @Override
    public boolean isSpectator() {
        return super.isSpectator() || GeneralConfig.isSpectator();
    }
}
