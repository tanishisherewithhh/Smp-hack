package net.fabricmc.smphack;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

public class HealthIndicatorsMod implements ModInitializer {
    public static KeyBinding keyBinding;
    public static boolean toggled = true;

    @Override
    public void onInitialize() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Health Indicator toggle",
                InputUtil.UNKNOWN_KEY.getCode(),
                "Smphack Mod"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                toggled = !toggled;
                if (client.player != null) {
                    client.player.sendMessage(Text.literal((toggled ? "Enabled" : "Disabled") + " Health Indicators"), true);
                }
            }
        });
    }
}