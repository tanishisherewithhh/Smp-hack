package net.fabricmc.smphack.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.smphack.GeneralConfig;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static net.fabricmc.smphack.config.ControllersConfig.*;


@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    Set<String> SpeedMineChoices = new HashSet<>(Arrays.asList("Hastemode", "NoBreakDelay"));
    Set<String> SpeedChoices = new HashSet<>(Arrays.asList("Strafe", "onGround", "Bhop"));
    Set<String> Textsidechoices = new HashSet<>(Arrays.asList("Left", "TopRight", "BottomRight"));
    Set<String> Coordsidechoices = new HashSet<>(Arrays.asList("BottomLeft", "TopRight", "BottomRight","AboveHotbar"));



    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.of("General Config Settings"));
            //AutoConfig.getConfigScreen(ControllersConfig.class, parent).get();
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            ConfigCategory settings = builder.getOrCreateCategory(Text.of("General Config Settings"));



            settings.addEntry(entryBuilder.startBooleanToggle(Text.of("SpeedMine"), GeneralConfig.getConfig().isEnableSpeedmine())
                    .setDefaultValue(false)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setEnableSpeedmine(newValue))
                    .build());

            settings.addEntry(entryBuilder.startStringDropdownMenu(Text.of("SpeedMine mode"), SpeedMineMode)
                    .setDefaultValue("Hastemode")
                    .setSelections(SpeedMineChoices)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setSpeedMineMode(newValue))
                    .build());

            settings.addEntry(entryBuilder.startIntSlider(Text.of("Haste Speed"), GeneralConfig.getConfig().getNobreakHaste(), 1, 3)
                    .setDefaultValue(1)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setNobreakHaste(newValue))
                    .build());
            settings.addEntry(entryBuilder.startIntSlider(Text.of("Break Cooldown"), GeneralConfig.getConfig().getNobreakCooldown(), 1, 3)
                    .setDefaultValue(1)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setNobreakCooldown(newValue))
                    .build());

            settings.addEntry(entryBuilder.startStringDropdownMenu(Text.of("Speed mode"), SpeedMode)
                    .setDefaultValue("NoBreakDelay")
                    .setSelections(SpeedChoices)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setSpeedMode(newValue))
                    .build());

            settings.addEntry(entryBuilder.startFloatField(Text.of("Speed Value"), GeneralConfig.getConfig().getSpeed_multiplier())
                    .setDefaultValue(1.2f)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setSpeed_multiplier(newValue))
                    .build());

            settings.addEntry(entryBuilder.startBooleanToggle(Text.of("Fullbright"), GeneralConfig.getConfig().getFullbright())
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setFullbright(newValue))
                    .build());

            settings.addEntry(entryBuilder.startBooleanToggle(Text.of("NoWeather"), GeneralConfig.getConfig().getNoWeather())
                    .setDefaultValue(false)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setNoWeather(newValue))
                    .build());
            settings.addEntry(entryBuilder.startBooleanToggle(Text.of("NoHurtCam"), GeneralConfig.getConfig().getNoHurtCam())
                    .setDefaultValue(false)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setNoHurtCam(newValue))
                    .build());

            settings.addEntry(entryBuilder.startBooleanToggle(Text.of("Antikick for creative fly"), GeneralConfig.getConfig().isEnableAntikick())
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setAntikick(newValue))
                    .build());

            settings.addEntry(entryBuilder.startStringDropdownMenu(Text.of("Text Side"), TextSide)
                    .setDefaultValue("Left")
                    .setSelections(Textsidechoices)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setTextSide(newValue))
                    .build());
            settings.addEntry(entryBuilder.startStringDropdownMenu(Text.of("Coord Side"), CoordsSide)
                    .setDefaultValue("BottomLeft")
                    .setSelections(Coordsidechoices)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setCoordsSide(newValue))
                    .build());

            settings.addEntry(entryBuilder.startFloatField(Text.of("Fire Opacity"), GeneralConfig.getConfig().getFireOpacity())
                    .setDefaultValue(1f)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setFireOpacity(newValue))
                    .build());

            settings.addEntry(entryBuilder.startIntSlider(Text.of("Jetpack Speed"), (int) GeneralConfig.getConfig().getJETPACK_MAX_SPEED(), 1, 10)
                    .setDefaultValue((int) 1f)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setJETPACK_MAX_SPEED(newValue))
                    .build());

            settings.addEntry(entryBuilder.startIntSlider(Text.of("Freecam fly Speed"), GeneralConfig.getConfig().getFlySpeed(), 1, 30)
                    .setDefaultValue(10)
                   .setSaveConsumer(newValue -> GeneralConfig.getConfig().setFlySpeed(newValue))
                   .build());

            settings.addEntry(entryBuilder.startBooleanToggle(Text.of("Freecam Action Bar"), GeneralConfig.getConfig().isActionBar())
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setActionBar(newValue))
                    .build());

            builder.setSavingRunnable(ConfigUtil::saveConfig);

            return builder.build();
        };
    }

}
