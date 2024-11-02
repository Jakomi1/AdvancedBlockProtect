package de.jakomi1.advancedBlockProtect;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class KillManager extends DatabaseManager {
    private List<String> trackedEntities;

    public KillManager(Plugin plugin) {
        super(plugin, "killentity.db");
        loadTrackedEntities(plugin);
    }

    private void loadTrackedEntities(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "kill.yml");
        if (!configFile.exists()) {
            createDefaultConfig(plugin);
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        trackedEntities = config.getStringList("trackedEntities");
    }

    private void createDefaultConfig(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "kill.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        config.set("trackedEntities", List.of("zombie", "villager"));
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isTracked(String entityType) {
        return trackedEntities.contains(entityType);
    }

    public void logKillAction(String killerName, String victimType, String location, String date, String time) {
        if (isTracked(victimType)) {
            logBlockAction(killerName, victimType, location, date, time);
        }
    }
}
