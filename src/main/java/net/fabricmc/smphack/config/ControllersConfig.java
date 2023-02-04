package net.fabricmc.smphack.config;


import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "Different Controllers")
public class ControllersConfig implements ConfigData {
    public float fireOpacity = 0.9F;
    public float fireHeight = 0.7F;
    public float JETPACK_MAX_SPEED=0.3F;
    public float speed_multiplier= 1.2F;
    public boolean Antikick=false;
    public boolean Fullbright=true;

    }

