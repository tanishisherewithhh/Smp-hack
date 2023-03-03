package net.fabricmc.smphack.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.smphack.GeneralConfig;
import net.minecraft.text.Text;

import static net.fabricmc.smphack.config.ControllersConfig.SpeedMinemodes;
import static net.fabricmc.smphack.config.ControllersConfig.SpeedMode;


@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.of("General Config Settings"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();



            ConfigCategory settings = builder.getOrCreateCategory(Text.of("General Settings"));
            ConfigCategory Speed = builder.getOrCreateCategory(Text.of("Speed"));
            ConfigCategory Freecam = builder.getOrCreateCategory(Text.of("Freecam"));
            ConfigCategory Speedmine = builder.getOrCreateCategory(Text.of("SpeedMine"));
            ConfigCategory Fly = builder.getOrCreateCategory(Text.of("Fly"));
            ConfigCategory Combat = builder.getOrCreateCategory(Text.of("Combat hack"));

            Combat.addEntry(entryBuilder.startIntSlider(Text.of("Crystal Break delay (0 = ~10 ms)"), GeneralConfig.getConfig().getCrystalBreakDelay_in_seconds(), 0, 1000)
                    .setDefaultValue(1)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setCrystalBreakDelay_in_seconds(newValue))
                    .build());

            Combat.addEntry(entryBuilder.startIntSlider(Text.of("KillAura delay (delay for swords = 8)"), GeneralConfig.getConfig().getKillAuraDelay(), 3, 20)
                    .setDefaultValue(8)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setKillAuraDelay(newValue))
                    .build());

            Combat.addEntry(entryBuilder.startIntSlider(Text.of("Range"), GeneralConfig.getConfig().getRange(), 1, 10)
                    .setDefaultValue(1)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setRange(newValue))
                    .build());
            SubCategoryBuilder Display = entryBuilder.startSubCategory(Text.of("Display HUD text"));

            Display.add(entryBuilder.startEnumSelector(Text.of("Text Side"), ControllersConfig.TextSide.class, ConfigUtil.config.Textside)
                    .setDefaultValue(ControllersConfig.TextSide.Left)
                    .setEnumNameProvider(x -> Text.of(" " + x.name().replace("_", "")))
                    .setSaveConsumer(newValue -> ConfigUtil.config.Textside = newValue).build());

            Display.add(entryBuilder.startEnumSelector(Text.of("Coordinates Side"), ControllersConfig.CoordsSide.class, ConfigUtil.config.Coordside)
                    .setDefaultValue(ControllersConfig.CoordsSide.Top)
                    .setEnumNameProvider(x -> Text.of(" " + x.name().replace("_", "")))
                    .setSaveConsumer(newValue -> ConfigUtil.config.Coordside = newValue).build());

            settings.addEntry(Display.build());


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
            settings.addEntry(entryBuilder.startIntSlider(Text.of("Player Reach"), GeneralConfig.getConfig().getReach(), 1, 10)
                    .setDefaultValue(4)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setReach(newValue))
                    .build());
            settings.addEntry(entryBuilder.startIntSlider(Text.of("Speed for Jesus"), GeneralConfig.getConfig().getSpeedforjesus(), 1, 10)
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
                    .setDefaultValue(SpeedMinemodes.HasteMode)
                    .setEnumNameProvider(x -> Text.of(" " + x.name().replace("_", "")))
                    .setSaveConsumer(newValue -> ConfigUtil.config.SpeedmMineModes = newValue).build());

            Speedmine.addEntry(entryBuilder.startIntSlider(Text.of("Haste Speed"), GeneralConfig.getConfig().getNobreakHaste(), 1, 5)
                    .setDefaultValue(1)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setNobreakHaste(newValue))
                    .build());

           /*
            Create subCategory using this (just for reference)

            SubCategoryBuilder SpeedModeSubCategory = entryBuilder.startSubCategory(Text.of("Speed"));

            SpeedModeSubCategory.add(entryBuilder.startStringDropdownMenu(Text.of("Speed mode"), SpeedMode)
                    .setDefaultValue("NoBreakDelay")
                    .setSelections(SpeedChoices)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setSpeedMode(newValue))
                    .build());

            SpeedModeSubCategory.add(entryBuilder.startFloatField(Text.of("Speed Value"), GeneralConfig.getConfig().getSpeed_multiplier())
                    .setDefaultValue(1.2f)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setSpeed_multiplier(newValue))
                    .build());

            Speed.addEntry(SpeedModeSubCategory.build());*/



            /*Speed.addEntry(entryBuilder.startStringDropdownMenu(Text.of("Speed mode"), SpeedMode)
                    .setDefaultValue("NoBreakDelay")
                    .setSelections(SpeedChoices)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setSpeedMode(newValue))
                    .build());*/

            Speed.addEntry(entryBuilder.startEnumSelector(Text.of("Speed Mode"), ControllersConfig.SpeedMode.class, ConfigUtil.config.Speedmode)
                    .setDefaultValue(SpeedMode.onGround)
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


            Freecam.addEntry(entryBuilder.startIntSlider(Text.of("Freecam fly Speed"), GeneralConfig.getConfig().getFlySpeed(), 1, 30)
                    .setDefaultValue(10)
                   .setSaveConsumer(newValue -> GeneralConfig.getConfig().setFlySpeed(newValue))
                   .build());

            Freecam.addEntry(entryBuilder.startBooleanToggle(Text.of("Freecam Action Bar"), GeneralConfig.getConfig().isActionBar())
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> GeneralConfig.getConfig().setActionBar(newValue))
                    .build());


            builder.setSavingRunnable(ConfigUtil::saveConfig);

            return builder.build();
        };
    }

}
