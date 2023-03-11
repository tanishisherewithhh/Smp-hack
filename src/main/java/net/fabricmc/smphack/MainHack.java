package net.fabricmc.smphack;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.smphack.RGBtext.CharacterMode;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainHack implements ModInitializer {
    MinecraftClient mc=MinecraftClient.getInstance();
    public static final Logger LOGGER = LoggerFactory.getLogger("Smp-hack");
    public static MainHack instance = null;

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        assert MinecraftClient.getInstance().player != null;
        HudRenderCallback.EVENT.register(new HUDoverlay());
        instance = this;
        CharacterMode.init();
            LOGGER.info("Client Initialised");
    }

}



