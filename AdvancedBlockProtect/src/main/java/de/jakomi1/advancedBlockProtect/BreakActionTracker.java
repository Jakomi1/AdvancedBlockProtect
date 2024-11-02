package de.jakomi1.advancedBlockProtect;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class BreakActionTracker {
    private DatabaseManager dbManager;

    public BreakActionTracker(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void trackBlockAction(String playerName, String blockType, String location) {
        String date = LocalDate.now().toString();
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        dbManager.logBlockAction(playerName, blockType, location, date, time);
    }
}
