package net.fabricmc.smphack.Hacks.NoWeather;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class NoWeather extends MainGui {

    public static float  prevRainGradient;

    static {
        assert MinecraftClient.getInstance().world != null;
        if (MinecraftClient.getInstance().world!= null) {
            prevRainGradient = MinecraftClient.getInstance().world.getRainGradient(prevRainGradient);
            System.out.println(prevRainGradient);
        }
    }

    boolean NoWeather;


    @Override
    public void update()
    {
        assert MinecraftClient.getInstance().world != null;
        NoWeather= GeneralConfig.getConfig().NoWeather;

        if (NoWeather)
        {
            if(MinecraftClient.getInstance().world.isRaining()) {
                MinecraftClient.getInstance().world.setRainGradient(0);
            }
        }
        else
        {
            if(MinecraftClient.getInstance().world.isRaining()) {
                MinecraftClient.getInstance().world.setRainGradient(prevRainGradient);
            }
        }
    }
}
