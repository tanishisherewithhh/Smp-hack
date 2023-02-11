package net.fabricmc.smphack.mixin;

import net.fabricmc.smphack.GeneralConfig;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    boolean NoWeather;

    @Inject(method = "onEntitySpawn", at = @At("HEAD"), cancellable = true)
    private void noweathereffects$cancelLightningSpawn(EntitySpawnS2CPacket packet, CallbackInfo ci) {
        NoWeather= GeneralConfig.getConfig().NoWeather;
        if (NoWeather && packet.getEntityType() == EntityType.LIGHTNING_BOLT) {
            ci.cancel();
        }

    }

}
