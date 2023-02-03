package net.fabricmc.smphack.mixin;

import net.fabricmc.smphack.MainHack;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class ExampleMixin {
    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {

        MainHack.LOGGER.info("Mixin Working.");
        MainHack.LOGGER.info("tanishisherewith: UNTIL WE MEET AGAIN");

    }
}
