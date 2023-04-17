package net.fabricmc.smphack.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.fabricmc.smphack.Hacks.Killaura.KillAura.lowestHealthEntity;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
    private void outlineEntities(Entity entity, CallbackInfoReturnable<Boolean> ci) {
        if (lowestHealthEntity!=null) {
            if (entity == lowestHealthEntity && lowestHealthEntity.isAlive()) {
                ci.setReturnValue(true);
            }
        }
    }
}