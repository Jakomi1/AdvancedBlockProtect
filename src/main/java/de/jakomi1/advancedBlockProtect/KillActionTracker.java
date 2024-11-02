package de.jakomi1.advancedBlockProtect;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class KillActionTracker {
    private KillManager killManager;

    public KillActionTracker(KillManager killManager) {
        this.killManager = killManager;
    }

    public void trackKillAction(String playerName, String entityType, String location) {
        String date = LocalDate.now().toString();
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        killManager.logKillAction(playerName, entityType, location, date, time);
    }
}

