package net.fabricmc.smphack;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.smphack.config.ControllersConfig;

public class ConfigController implements ClientModInitializer {
    private static ControllersConfig config;
    @Override
    public void onInitializeClient() {
        ConfigHolder<ControllersConfig> configHolder = AutoConfig.register(ControllersConfig.class, Toml4jConfigSerializer::new);
        ConfigController.config = configHolder.getConfig();

    }

    public static ControllersConfig getConfig() {
        return ConfigController.config;
    }
}