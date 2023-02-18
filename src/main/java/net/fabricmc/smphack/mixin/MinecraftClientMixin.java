package net.fabricmc.smphack.mixin;

import net.fabricmc.smphack.CrystalModifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Map;

//Crystal modifier mixin
@Mixin({MinecraftClient.class})
public class MinecraftClientMixin {
    public MinecraftClientMixin() {
    }

    @Inject(
            method = {"tick"},
            at = {@At("HEAD")}
    )
    private void onPreTick(CallbackInfo info) {
        Iterator<Map.Entry<Entity, Integer>> iterator = CrystalModifier.toKill.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry<Entity, Integer> entry = iterator.next();
            Entity entity = (Entity)entry.getKey();
            int delay = (Integer)entry.getValue() - 1;
            delay=0;
            if (delay == 0) {
                iterator.remove();
                if (!entity.isAlive()) {
                    entity.kill();
                    entity.setRemoved(RemovalReason.KILLED);
                    entity.onRemoved();
                }
            } else {
                entry.setValue(delay);
            }
        }

    }
}
