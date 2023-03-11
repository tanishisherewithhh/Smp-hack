package net.fabricmc.smphack.Hacks.Autoclicker;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.MainGui;
import net.fabricmc.smphack.config.ConfigUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

import static net.fabricmc.smphack.HUDoverlay.AutoClickerKey;

public class Autoclicker extends MainGui {


String Button;
String MouseButton;
boolean mouse;
public static int delay;
public static boolean isAutoclickerOn=false;
public static boolean Autoclicker;

private static int i = 0;

    @Override
    public void toggled() {
        super.toggled();
    }

    @Override
    public void update() {
        Button = String.valueOf(ConfigUtil.config.ButtonAuto);
        MouseButton = String.valueOf(ConfigUtil.config.MouseKeybindButton);
        mouse = GeneralConfig.getConfig().getMouseButton();
        Autoclicker=GeneralConfig.getConfig().getAutoClicker();
        isAutoclickerOn=false;
        assert GeneralConfig.getConfig() != null;
        delay = GeneralConfig.getConfig().getDelayAC();
        if (Autoclicker) {
            if (mouse) {
                if (Objects.equals(MouseButton, "RightButton")) {
                    if (MinecraftClient.getInstance().options.useKey.isPressed()) {
                        assert MinecraftClient.getInstance().player != null;
                        isAutoclickerOn = true;
                        {
                            if (i < delay) i++;
                            else {
                                if (Objects.equals(Button, "LeftMButton")) {
                                    KeyBinding.onKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_1));
                                    //KeyBinding.setKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_1),false);
                                    //KeyBinding.setKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_1),true);

                                }
                                if (Objects.equals(Button, "RightMButton")) {
                                    KeyBinding.onKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_2));
                                    // KeyBinding.setKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_2),false);
                                    //KeyBinding.setKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_2),true);

                                }
                                i = 0;
                            }
                        }
                    }
                }
                if (Objects.equals(MouseButton, "LeftButton")) {
                    if (MinecraftClient.getInstance().options.attackKey.isPressed()) {
                        assert MinecraftClient.getInstance().player != null;
                        isAutoclickerOn = true;
                        {
                            if (i < delay) i++;
                            else {
                                if (Objects.equals(Button, "LeftMButton")) {
                                    KeyBinding.onKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_1));
                                    //KeyBinding.setKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_1),false);
                                    //KeyBinding.setKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_1),true);

                                }
                                if (Objects.equals(Button, "RightMButton")) {
                                    KeyBinding.onKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_2));
                                    // KeyBinding.setKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_2),false);
                                    //KeyBinding.setKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_2),true);

                                }
                                i = 0;
                            }
                        }
                    }
                }

            } else {
                if (AutoClickerKey.isPressed()) {
                    assert MinecraftClient.getInstance().player != null;
                    {
                        isAutoclickerOn = true;
                        if (i < delay) i++;
                        else {
                            if (Objects.equals(Button, "LeftMButton")) {
                                KeyBinding.onKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_1));
                                //KeyBinding.setKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_1),false);
                                //KeyBinding.setKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_1),true);

                            }
                            if (Objects.equals(Button, "RightMButton")) {
                                KeyBinding.onKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_2));
                                // KeyBinding.setKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_2),false);
                                //KeyBinding.setKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_2),true);

                            }
                            i = 0;
                        }
                    }
                }
            }

        }
    }
}
