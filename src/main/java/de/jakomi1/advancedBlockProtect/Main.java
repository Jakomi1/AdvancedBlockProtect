package de.jakomi1.advancedBlockProtect;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.InvalidConfigurationException;

public class Main extends JavaPlugin {
    private BreakManager breakBlockDbManager;
    private PlaceManager placeBlockDbManager;
    private KillManager killManager;
    private GetData dataRetriever;

    @Override
    public void onEnable() {
        getLogger().info("AdvancedBlockProtect wurde aktiviert!");
        this.breakBlockDbManager = new BreakManager(this);
        this.placeBlockDbManager = new PlaceManager(this);
        this.killManager = new KillManager(this);
        this.dataRetriever = new GetData(breakBlockDbManager, placeBlockDbManager, killManager);
        getServer().getPluginManager().registerEvents(new BreakListener(breakBlockDbManager, dataRetriever), this);
        getServer().getPluginManager().registerEvents(new PlaceListener(placeBlockDbManager), this);
        getServer().getPluginManager().registerEvents(new KillListener(killManager), this);

        // Create default kill.yml if it doesn't exist
        createDefaultConfig("kill.yml", "trackedEntities:\n  - \"zombie\"\n  - \"villager\"");
    }

    @Override
    public void onDisable() {
        getLogger().info("AdvancedBlockProtect wurde deaktiviert!");
        this.breakBlockDbManager.closeConnection();
        this.placeBlockDbManager.closeConnection();
        this.killManager.closeConnection();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("advancedblockprotect")) {
            sender.sendMessage(ChatColor.BLUE + "[AdvancedBlockProtect] " + ChatColor.WHITE + "Usage: /advancedblockprotect");
            return true;
        }
        return false;
    }

    private void createDefaultConfig(String fileName, String defaultContent) {
        File configFile = new File(getDataFolder(), fileName);
        if (!configFile.exists()) {
            getDataFolder().mkdirs();
            try {
                configFile.createNewFile();
                FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                config.loadFromString(defaultContent);
                config.save(configFile);
                getLogger().info(fileName + " created with default content.");
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }
}
