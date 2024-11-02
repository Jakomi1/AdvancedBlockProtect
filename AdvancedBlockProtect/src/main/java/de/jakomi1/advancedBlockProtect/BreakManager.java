package de.jakomi1.advancedBlockProtect;

import org.bukkit.plugin.Plugin;

public class BreakManager extends DatabaseManager {

    public BreakManager(Plugin plugin) {
        super(plugin, "breakblock.db");
    }

    public void logBreakAction(String playerName, String blockType, String location, String date, String time) {
        logBlockAction(playerName, blockType, location, date, time);
    }
}
