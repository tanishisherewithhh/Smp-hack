package net.fabricmc.smphack;

import RGBtext.CharacterMode;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class MainHack implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final String MOD_ID = "smphack";
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");
    public static KeyBinding keyBinding;
    public static boolean toggled = true;
    boolean Charactermode;
    boolean Linemode;
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        assert MinecraftClient.getInstance().player != null;
        HudRenderCallback.EVENT.register(new HUDoverlay());
            //LineMode.init();
            CharacterMode.init();


        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + MOD_ID + ".toggle",
                InputUtil.UNKNOWN_KEY.getCode(),
                "Health Indicator with " + MOD_ID
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                toggled = !toggled;
                if (client.player != null)
                    client.player.sendMessage(Text.literal((toggled ? "Enabled" : "Disabled") + " HealthIndicator"), true);
            }
        });


        LOGGER.info("client initialised");
    }

}
