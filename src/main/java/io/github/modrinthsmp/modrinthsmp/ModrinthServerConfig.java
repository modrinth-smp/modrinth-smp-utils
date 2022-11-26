package io.github.modrinthsmp.modrinthsmp;

import org.quiltmc.json5.JsonReader;
import org.quiltmc.json5.JsonWriter;

import java.io.IOException;

public final class ModrinthServerConfig {
    private int explosionSpawnRadius = 512;

    public int getExplosionSpawnRadius() {
        return explosionSpawnRadius;
    }

    public void writeConfig(JsonWriter writer) throws IOException {
        writer.beginObject(); {
            writer.comment("Spawn radius to disable explosions within. Same value as server.properties");
            writer.name("explosionSpawnRadius").value(explosionSpawnRadius);
        } writer.endObject();
    }

    public void readConfig(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            final String key;
            //noinspection SwitchStatementWithTooFewBranches
            switch (key = reader.nextName()) {
                case "explosionSpawnRadius" -> explosionSpawnRadius = reader.nextInt();
                default -> ModrinthSmp.LOGGER.warn("Unknown config key: {}", key);
            }
        }
        reader.endObject();
    }
}
