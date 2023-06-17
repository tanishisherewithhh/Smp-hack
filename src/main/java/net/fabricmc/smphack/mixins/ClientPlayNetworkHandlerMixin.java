package net.fabricmc.smphack.mixins;

import net.fabricmc.smphack.GeneralConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//NoWeather mixin
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

    // Disables freecam when the player respawns/switches dimensions.
    @Inject(method = "onPlayerRespawn", at = @At("HEAD"))
    private void onPlayerRespawn(CallbackInfo ci) {
        if (GeneralConfig.isEnabled()) {
            GeneralConfig.enabled=!GeneralConfig.enabled;
        }
    }

    private static String currentBiome = "";

    @Inject(method = "onGameJoin", at = @At("RETURN"))
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        assert MinecraftClient.getInstance().world != null;
        assert MinecraftClient.getInstance().player != null;
        currentBiome = MinecraftClient.getInstance().world.getBiome(MinecraftClient.getInstance().player.getBlockPos()).toString();
    }
}


