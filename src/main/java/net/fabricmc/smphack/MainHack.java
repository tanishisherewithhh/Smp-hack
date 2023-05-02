package net.fabricmc.smphack;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.smphack.RGBtext.CharacterMode;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainHack implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Smp-hack");

    @Override
    public void onInitialize() {
        assert MinecraftClient.getInstance().player != null;
        HudRenderCallback.EVENT.register(new HUDoverlay());
        CharacterMode.init();
            LOGGER.info("Smphack Initialised");
    }

}



