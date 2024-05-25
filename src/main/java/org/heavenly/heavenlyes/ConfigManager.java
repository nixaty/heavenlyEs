package org.heavenly.heavenlyes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static com.mojang.text2speech.Narrator.LOGGER;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "heavenly_es.json");

    public static ModOptions actualConfig;

    public static ModOptions loadConfig() {
        try (FileReader reader = new FileReader(configFile)) {
            actualConfig = GSON.fromJson(reader, ModOptions.class);
            return actualConfig;
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }

        ModOptions modOptions = new ModOptions();
        actualConfig = modOptions;
        ConfigManager.saveConfig(modOptions);
        return modOptions; // Возвращаем новый объект, если не удалось загрузить конфигурацию
    }

    public static void saveConfig(ModOptions config) {
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }
}