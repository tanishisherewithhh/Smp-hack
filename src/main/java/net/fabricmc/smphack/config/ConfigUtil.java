package net.fabricmc.smphack.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class ConfigUtil {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "Smphack_Config.json");
    public static ControllersConfig config;

    public static void loadConfig() {
        try {
            if (configFile.exists()) {
                Reader reader = new FileReader(configFile);
                config = GSON.fromJson(reader, ControllersConfig.class);
            } else {
                config = new ControllersConfig();
                saveConfig();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        try {
            configFile.createNewFile();
            Writer writer = new FileWriter(configFile, false);
            GSON.toJson(config, writer);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ControllersConfig getConfig() {
        return config;
    }

}
