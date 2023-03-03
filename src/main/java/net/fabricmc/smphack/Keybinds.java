package net.fabricmc.smphack;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Keybinds {

    public static final KeyBinding FlyKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Fly toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "Imperials"
    ));

    public static final KeyBinding Nofallkey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Nofall toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            "Imperials"
    ));

    public static final KeyBinding Jesuskey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Jesus toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "Imperials"
    ));
    public static final KeyBinding SpeedKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Speed toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "Imperials"
    ));

    public static final KeyBinding AutoCrystalBreakerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "AutoCrystal Breaker toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "Imperials"
    ));

    public static final KeyBinding KillauraKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Killaura toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "Imperials"
    ));

    public static final KeyBinding ReachKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Reach toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Y,
            "Imperials"
    ));



}
