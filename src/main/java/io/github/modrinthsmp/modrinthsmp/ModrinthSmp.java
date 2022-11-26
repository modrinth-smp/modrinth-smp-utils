package io.github.modrinthsmp.modrinthsmp;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.quiltmc.json5.JsonReader;
import org.quiltmc.json5.JsonWriter;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public class ModrinthSmp implements ModInitializer {
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final ModrinthServerConfig CONFIG = new ModrinthServerConfig();

    @Override
    public void onInitialize() {
        readConfig();
    }

    public static void readConfig() {
        try (final JsonReader reader = JsonReader.json5(getConfigPath())) {
            CONFIG.readConfig(reader);
        } catch (IOException e) {
            LOGGER.error("Failed to load config", e);
        }
        writeConfig();
    }

    public static void writeConfig() {
        try (final JsonWriter writer = JsonWriter.json5(getConfigPath())) {
            CONFIG.writeConfig(writer);
        } catch (IOException e) {
            LOGGER.error("Failed to save config", e);
        }
    }

    public static Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("modrinth-smp.json5");
    }

    public static ModrinthServerConfig getConfig() {
        return CONFIG;
    }
}
