package net.fabricmc.smphack.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.smphack.GeneralConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import static net.fabricmc.smphack.HUDoverlay.ConfigScreenOpenerKey;

public class ConfigScreen {
    public Screen openconfigscreen() {

        ConfigBuilder builder = ConfigBuilder.create()
                .setTransparentBackground(true)
                .setTitle(Text.of("Smphack settings"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory settings = builder.getOrCreateCategory(Text.of("General Settings"));
        ConfigCategory hud = builder.getOrCreateCategory(Text.of("HUD"));
        ConfigCategory Speed = builder.getOrCreateCategory(Text.of("Speed"));
        ConfigCategory Freecam = builder.getOrCreateCategory(Text.of("Freecam"));
        ConfigCategory Speedmine = builder.getOrCreateCategory(Text.of("SpeedMine"));
        ConfigCategory Fly = builder.getOrCreateCategory(Text.of("Fly"));
        ConfigCategory AutoClicker = builder.getOrCreateCategory(Text.of("AutoClicker"));
        ConfigCategory Combat = builder.getOrCreateCategory(Text.of("Combat hack"));


        Combat.addEntry(entryBuilder.startIntSlider(Text.of("Crystal Break delay (0 = ~10 ms)"), GeneralConfig.getConfig().getCrystalBreakDelay_in_seconds(), 0, 1000)
                .setDefaultValue(1)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setCrystalBreakDelay_in_seconds(newValue))
                .build());
        Combat.addEntry(entryBuilder.startBooleanToggle(Text.of("AntiSuicide"), GeneralConfig.getConfig().getAntiSuicide())
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setAntiSuicide(newValue))
                .build());
        Combat.addEntry(entryBuilder.startIntSlider(Text.of("Min Damage to player for antisuicide"), GeneralConfig.getConfig().getASdamage(), 1, 32)
                .setDefaultValue(10)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setASdamage(newValue))
                .build());
        Combat.addEntry(entryBuilder.startBooleanToggle(Text.of("Only Own End crystals"), GeneralConfig.getConfig().getOnlyOwn())
                .setDefaultValue(false)
                .setTooltip(Text.of("To only break end crystals placed by the player"))
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setOnlyOwn(newValue))
                .build());

        Combat.addEntry(entryBuilder.startIntSlider(Text.of("Range"), GeneralConfig.getConfig().getRange(), 1, 5)
                .setDefaultValue(1)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setRange(newValue))
                .build());

        Combat.addEntry(entryBuilder.startIntSlider(Text.of("KillAura delay"), GeneralConfig.getConfig().getKillAuraDelay(), 3, 20)
                .setDefaultValue(8)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setKillAuraDelay(newValue))
                .setTooltip(Text.of("7 for swords and 10 for axe"))
                .build());

        Combat.addEntry(entryBuilder.startBooleanToggle(Text.of("Sword and Axe only for Killaura"), GeneralConfig.getConfig().getSwordAxeOnly())
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setSwordAxeOnly(newValue))
                .build());
        Combat.addEntry(entryBuilder.startBooleanToggle(Text.of("MultiTarget mode"), GeneralConfig.getConfig().getMultiTarget())
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setMultiTarget(newValue))
                .build());
        Combat.addEntry(entryBuilder.startBooleanToggle(Text.of("AutoDelay mode"), GeneralConfig.getConfig().getAutoDelayKA())
                .setDefaultValue(false)
                .setTooltip(Text.of("Sets delay for swords and axes automatically"))
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setAutoDelayKA(newValue))
                .build());


        hud.addEntry(entryBuilder.startBooleanToggle(Text.of("Hud Display Text Shadow"), GeneralConfig.getConfig().getTextshadow())
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setTextShadow(newValue))
                .build());


        hud.addEntry(entryBuilder.startEnumSelector(Text.of("Text info Side"), ControllersConfig.TextSide.class, ConfigUtil.config.Textside)
                .setDefaultValue(ControllersConfig.TextSide.Left)
                .setEnumNameProvider(x -> Text.of(" " + x.name().replace("_", "")))
                .setSaveConsumer(newValue -> ConfigUtil.config.Textside = newValue).build());

        hud.addEntry(entryBuilder.startEnumSelector(Text.of("Coordinates display side"), ControllersConfig.CoordsSide.class, ConfigUtil.config.Coordside)
                .setDefaultValue(ControllersConfig.CoordsSide.Top)
                .setEnumNameProvider(x -> Text.of(" " + x.name().replace("_", "")))
                .setSaveConsumer(newValue -> ConfigUtil.config.Coordside = newValue).build());

        hud.addEntry(entryBuilder.startBooleanToggle(Text.of("Show Muppet"), GeneralConfig.getConfig().getShowMuppet())
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setShowMuppet(newValue))
                .build());

        hud.addEntry(entryBuilder.startBooleanToggle(Text.of("Nametag Shadow"), GeneralConfig.getConfig().getnametagshadow())
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setNametagshadow(newValue))
                .build());
        hud.addEntry(entryBuilder.startEnumSelector(Text.of("Player Nametag colour"), ControllersConfig.nametagcolour.class, ConfigUtil.config.PlayerNametagcolour)
                .setDefaultValue(ControllersConfig.nametagcolour.WHITE)
                .setEnumNameProvider(x -> Text.of(" " + x.name().replace("_", "")))
                .setSaveConsumer(newValue -> ConfigUtil.config.PlayerNametagcolour = newValue).build());
        hud.addEntry(entryBuilder.startEnumSelector(Text.of("Entity Nametag colour"), ControllersConfig.nametagcolour.class, ConfigUtil.config.EntityNametagcolour)
                .setDefaultValue(ControllersConfig.nametagcolour.WHITE)
                .setEnumNameProvider(x -> Text.of(" " + x.name().replace("_", "")))
                .setSaveConsumer(newValue -> ConfigUtil.config.EntityNametagcolour = newValue).build());

        hud.addEntry(entryBuilder.startEnumSelector(Text.of("Armor Durability "), ControllersConfig.ArmorDurabilityDisplay.class, ConfigUtil.config.ArmorDurability)
                .setDefaultValue(ControllersConfig.ArmorDurabilityDisplay.Percent)
                .setEnumNameProvider(x -> Text.of(" " + x.name().replace("_", "")))
                .setSaveConsumer(newValue -> ConfigUtil.config.ArmorDurability = newValue).build());

        settings.addEntry(entryBuilder.startBooleanToggle(Text.of("Fullbright"), GeneralConfig.getConfig().getFullbright())
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setFullbright(newValue))
                .build());

        settings.addEntry(entryBuilder.startBooleanToggle(Text.of("AutoSprint"), GeneralConfig.getConfig().getAutoSprint())
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setAutoSprint(newValue))
                .build());
        settings.addEntry(entryBuilder.startBooleanToggle(Text.of("AutoTool"), GeneralConfig.getConfig().getAutoTool())
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setAutoTool(newValue))
                .build());

        settings.addEntry(entryBuilder.startBooleanToggle(Text.of("AntiHunger"), GeneralConfig.getConfig().getAntiHunger())
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setAntiHunger(newValue))
                .build());
        settings.addEntry(entryBuilder.startBooleanToggle(Text.of("Auto Replace"), GeneralConfig.getConfig().getAutoHotbar())
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setAutoHotbar(newValue))
                .build());
        settings.addEntry(entryBuilder.startBooleanToggle(Text.of("NoWeather"), GeneralConfig.getConfig().getNoWeather())
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setNoWeather(newValue))
                .build());
        settings.addEntry(entryBuilder.startBooleanToggle(Text.of("NoHurtCam"), GeneralConfig.getConfig().getNoHurtCam())
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setNoHurtCam(newValue))
                .build());

        Speed.addEntry(entryBuilder.startIntSlider(Text.of("Speed for Jesus"), GeneralConfig.getConfig().getSpeedforjesus(), 1, 10)
                .setDefaultValue(4)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setSpeedforjesus(newValue))
                .build());
        settings.addEntry(entryBuilder.startFloatField(Text.of("Fire Opacity"), GeneralConfig.getConfig().getFireOpacity())
                .setDefaultValue(1f)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setFireOpacity(newValue))
                .build());


        Speedmine.addEntry(entryBuilder.startBooleanToggle(Text.of("SpeedMine"), GeneralConfig.getConfig().isEnableSpeedmine())
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setEnableSpeedmine(newValue))
                .build());

        Speedmine.addEntry(entryBuilder.startEnumSelector(Text.of("SpeedMine mode"), ControllersConfig.SpeedMinemodes.class, ConfigUtil.config.SpeedmMineModes)
                .setDefaultValue(ControllersConfig.SpeedMinemodes.HasteMode)
                .setEnumNameProvider(x -> Text.of(" " + x.name().replace("_", "")))
                .setSaveConsumer(newValue -> ConfigUtil.config.SpeedmMineModes = newValue).build());

        Speedmine.addEntry(entryBuilder.startIntSlider(Text.of("Haste Speed"), GeneralConfig.getConfig().getNobreakHaste(), 1, 5)
                .setDefaultValue(1)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setNobreakHaste(newValue))
                .build());

        Speed.addEntry(entryBuilder.startEnumSelector(Text.of("Speed Mode"), ControllersConfig.SpeedMode.class, ConfigUtil.config.Speedmode)
                .setDefaultValue(ControllersConfig.SpeedMode.onGround)
                .setEnumNameProvider(x -> Text.of(" " + x.name().replace("_", "")))
                .setSaveConsumer(newValue -> ConfigUtil.config.Speedmode = newValue).build());

        Speed.addEntry(entryBuilder.startFloatField(Text.of("Speed Value"), GeneralConfig.getConfig().getSpeed_multiplier())
                .setDefaultValue(1.2f)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setSpeed_multiplier(newValue))
                .build());


        Fly.addEntry(entryBuilder.startIntSlider(Text.of("BoatFly Speed"), GeneralConfig.getConfig().getBoatflySpeed(), 1, 10)
                .setDefaultValue(1)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setBoatflySpeed(newValue))
                .build());

        Fly.addEntry(entryBuilder.startBooleanToggle(Text.of("Antikick for creative fly"), GeneralConfig.getConfig().isEnableAntikick())
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setAntikick(newValue))
                .build());

        Fly.addEntry(entryBuilder.startIntSlider(Text.of("Jetpack Speed"), (int) GeneralConfig.getConfig().getJETPACK_MAX_SPEED(), 1, 10)
                .setDefaultValue((int) 1f)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setJETPACK_MAX_SPEED(newValue))
                .build());

        AutoClicker.addEntry(entryBuilder.startBooleanToggle(Text.of("AutoClicker"), GeneralConfig.getConfig().getAutoClicker())
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setAutoClicker(newValue))
                .setTooltip(Text.of("Turn AutoClicker on/off"))
                .build());
        AutoClicker.addEntry(entryBuilder.startBooleanToggle(Text.of("No Accidental firework spam"), GeneralConfig.getConfig().getNoAccidentalFireworks())
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setNoAccidentalFireworks(newValue))
                .setTooltip(Text.of("Disables Autoclicker when you have fireworks in your main hand"))
                .build());

        AutoClicker.addEntry(entryBuilder.startBooleanToggle(Text.of("Mouse button for Autoclicker keybind"), GeneralConfig.getConfig().getMouseButton())
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setMouseButton(newValue))
                .build());

        AutoClicker.addEntry(entryBuilder.startEnumSelector(Text.of("Mouse Keybind Button for autoclicker"), ControllersConfig.MouseKeybind.class, ConfigUtil.config.MouseKeybindButton)
                .setDefaultValue(ControllersConfig.MouseKeybind.RightButton)
                .setEnumNameProvider(x -> Text.of(" " + x.name().replace("_", "")))
                .setTooltip(Text.of("This will select the keybind as a mouse button"))
                .setSaveConsumer(newValue -> ConfigUtil.config.MouseKeybindButton = newValue).build());


        AutoClicker.addEntry(entryBuilder.startEnumSelector(Text.of("Auto click button"), ControllersConfig.Buttons.class, ConfigUtil.config.ButtonAuto)
                .setDefaultValue(ControllersConfig.Buttons.RightMButton)
                .setEnumNameProvider(x -> Text.of(" " + x.name().replace("_", "")))
                .setTooltip(Text.of("Mouse button which will be auto clicked"))
                .setSaveConsumer(newValue -> ConfigUtil.config.ButtonAuto = newValue).build());

        AutoClicker.addEntry(entryBuilder.startIntSlider(Text.of("AutoClicker delay"), GeneralConfig.getConfig().getDelayAC(), 0, 100)
                .setDefaultValue(1)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setDelayAC(newValue))
                .build());


        Freecam.addEntry(entryBuilder.startIntSlider(Text.of("Freecam fly Speed"), GeneralConfig.getConfig().getFlySpeed(), 1, 30)
                .setDefaultValue(10)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setFlySpeed(newValue))
                .build());

        Freecam.addEntry(entryBuilder.startBooleanToggle(Text.of("Freecam Action Bar"), GeneralConfig.getConfig().isActionBar())
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> GeneralConfig.getConfig().setActionBar(newValue))
                .build());


        builder.setSavingRunnable(ConfigUtil::saveConfig);
        Screen screen = builder.build();
        if (ConfigScreenOpenerKey.wasPressed()) {
            MinecraftClient.getInstance().setScreen(screen);
        }
        return builder.build();
    }
}
