package net.fabricmc.smphack.Hacks.Fly;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class Fly extends MainGui {

    public int flybindcounter = 0;
    private static final KeyBinding modeKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Change Fly mode ",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_M,
            "Smphack Mod"
    ));


    @Override
    public void toggled()
    {
        enabled=!enabled;
    }
    @Override
    public void update() {
            int flymodeIndex = 1;
        if (modeKey.wasPressed()) {
            if (flybindcounter > 5) {
                flybindcounter = 0;
            }
            flybindcounter++;
            flymodeIndex = flybindcounter % Flymodes.values().length;
        }
        Flymodes flymode = Flymodes.values()[flymodeIndex];
        assert MinecraftClient.getInstance().player != null;
    }
}

