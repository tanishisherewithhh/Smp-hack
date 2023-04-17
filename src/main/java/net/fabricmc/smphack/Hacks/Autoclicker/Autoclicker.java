package net.fabricmc.smphack.Hacks.Autoclicker;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.MainGui;
import net.fabricmc.smphack.config.ConfigUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
public static boolean ToggleAuto;
public static boolean isToggleAuto=false;
public static boolean FireworksDisable;
private static int i = 0;

    @Override
    public void toggled() {
        super.toggled();
    }

    public boolean isPlayerHoldingFireworks(PlayerEntity player)
    {
        ItemStack item = player.getMainHandStack();
        Item type = item.getItem();
        return type == Items.FIREWORK_ROCKET;
    }


    public void Autoclick()
    {
        if (i < delay) i++;
        else {
            if (Objects.equals(Button, "LeftMButton")) {
                    if (ToggleAuto)
                    {
                        while(isToggleAuto) {
                            KeyBinding.onKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_1));
                        }
                    }
                else {
                    KeyBinding.onKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_1));
                }
            }
            if (Objects.equals(Button, "RightMButton")) {
                    if (ToggleAuto)
                    {
                        if (isToggleAuto) {
                            KeyBinding.onKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_2));
                        }
                    }
                else {
                    KeyBinding.onKeyPressed(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_2));
                }
            }
            i=0;
        }
    }



    @Override
    public void update() {
        MinecraftClient mc = MinecraftClient.getInstance();
        Button = String.valueOf(ConfigUtil.config.ButtonAuto);
        MouseButton = String.valueOf(ConfigUtil.config.MouseKeybindButton);
        mouse = GeneralConfig.getConfig().getMouseButton();
        ToggleAuto=GeneralConfig.getConfig().getAutoClickerToggle();
        assert mc.player != null;
        isAutoclickerOn=false;
        FireworksDisable=GeneralConfig.getConfig().getNoAccidentalFireworks();
        if (FireworksDisable)
        {
            if(isPlayerHoldingFireworks(mc.player))
            {
                Autoclicker=false;
            }
            else
            {
                Autoclicker=GeneralConfig.getConfig().getAutoClicker();
            }
        }
        else
        {
            Autoclicker=GeneralConfig.getConfig().getAutoClicker();
        }
        assert GeneralConfig.getConfig() != null;
        delay = GeneralConfig.getConfig().getDelayAC();
        if (Autoclicker) {
            if (mouse) {
                if (Objects.equals(MouseButton, "RightButton")) {
                    if (mc.options.useKey.isPressed()) {
                        isAutoclickerOn = true;
                        isToggleAuto=!isToggleAuto;
                       Autoclick();
                    }
                }
                if (Objects.equals(MouseButton, "LeftButton")) {
                    if (mc.options.attackKey.isPressed()) {
                        isAutoclickerOn = true;
                        isToggleAuto=!isToggleAuto;
                            Autoclick();
                    }
                }

            } else if (AutoClickerKey.isPressed()) {
                isAutoclickerOn = true;
                isToggleAuto=!isToggleAuto;
                    Autoclick();
            }

        }
    }
}
