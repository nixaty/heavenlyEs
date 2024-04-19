package org.heavenly.heavenlyes.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File configFile = new File(FabricLoader.getInstance().getConfigDirectory(), "mod_config.json");

    public static ModOptions actualConfig;

    public static ModOptions loadConfig() {
        try (FileReader reader = new FileReader(configFile)) {
            actualConfig = GSON.fromJson(reader, ModOptions.class);
            return actualConfig;
        } catch (IOException e) {
            e.printStackTrace();
        }

        ModOptions modOptions = new ModOptions();
        ConfigManager.saveConfig(modOptions);
        return modOptions; // Возвращаем новый объект, если не удалось загрузить конфигурацию
    }

    public static void saveConfig(ModOptions config) {
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}