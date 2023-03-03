package net.fabricmc.smphack.mixin;

import net.fabricmc.smphack.Hacks.Reach.EventReach;
import net.fabricmc.smphack.Hacks.Reach.Reach;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.fabricmc.smphack.Keybinds.ReachKey;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {
Reach reach = new Reach();
    @Inject(method = "getReachDistance", at = @At("RETURN"), cancellable = true)
    private void getReachDistance(CallbackInfoReturnable<Float> callback) {
        if (ReachKey.wasPressed()) {
            reach.toggled();
        }
            EventReach event = new EventReach(callback.getReturnValueF());
            callback.setReturnValue(event.getReach());
    }

}
