package de.jakomi1.advancedBlockProtect;

import org.bukkit.plugin.Plugin;

public class PlaceManager extends DatabaseManager {

    public PlaceManager(Plugin plugin) {
        super(plugin, "placeblock.db");
    }

    public void logPlaceAction(String playerName, String blockType, String location, String date, String time) {
        logBlockAction(playerName, blockType, location, date, time);
    }
}
