package net.fabricmc.smphack;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.smphack.Hacks.CrystalSwitch;
import net.fabricmc.smphack.Hacks.Fakeplayer.FakePlayerCommand;
import net.fabricmc.smphack.RGBtext.CharacterMode;
import net.fabricmc.smphack.config.ConfigScreen;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainHack  implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Smp-hack");
    CrystalSwitch crystalSwitch = new CrystalSwitch();

    @Override
    public void onInitialize() {
        assert MinecraftClient.getInstance().player != null;
        FakePlayerCommand.register();
        HudRenderCallback.EVENT.register(new HUDoverlay());
        CharacterMode.init();
        LOGGER.info("Smphack Initialised");
        ConfigScreen Screen = new ConfigScreen();
        ClientTickEvents.END_CLIENT_TICK.register(client -> Screen.openconfigscreen());
        crystalSwitch = new CrystalSwitch();
    }
}



