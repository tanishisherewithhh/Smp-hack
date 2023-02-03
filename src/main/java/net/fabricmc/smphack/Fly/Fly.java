package net.fabricmc.smphack.Fly;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class Fly extends MainGui {

    public int flybindcounter = 0;
    private static final KeyBinding modeKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Fly mode ",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_M,
            "Imperials"
    ));
    public static final KeyBinding AntikickKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Antikick",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            "Antikick for CreativeFly mode"
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
            if (flybindcounter > 4) {
                flybindcounter = 0;
            }
            flybindcounter++;
            flymodeIndex = flybindcounter % Flymodes.values().length;

        }
        Flymodes flymode = Flymodes.values()[flymodeIndex];
        assert MinecraftClient.getInstance().player != null;
    }
}

