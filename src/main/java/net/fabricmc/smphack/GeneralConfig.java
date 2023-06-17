package net.fabricmc.smphack;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.smphack.Hacks.Freecam.CameraEntity;
import net.fabricmc.smphack.config.ConfigUtil;
import net.fabricmc.smphack.config.ControllersConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class GeneralConfig implements ModInitializer {

    public static boolean enabled = false;
    private static boolean spectator;
    public static final KeyBinding FreecamKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Freecam toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "Hacks Keybind"));


    @Override
    public void onInitialize() {
        ConfigUtil.loadConfig();


        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            while (FreecamKey.wasPressed()) {
                toggleFreecam(client);

            }
        });
    }

    public static void toggleFreecam(MinecraftClient client) {
        enabled = !enabled;
        if (enabled) {
            CameraEntity.createCamera(MinecraftClient.getInstance());
        } else {
            CameraEntity.removeCamera();
        }

    }

    public static ControllersConfig getConfig() {
        return ConfigUtil.getConfig();
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static boolean isSpectator() {
        return spectator;
    }

}