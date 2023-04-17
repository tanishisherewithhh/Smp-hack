package net.fabricmc.smphack.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;


@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    ConfigScreen screen = new ConfigScreen();

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
            return parent ->{
                return screen.openconfigscreen();
            };
    }
}
