package de.jakomi1.advancedBlockProtect;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class KillListener implements Listener {
    private KillActionTracker killActionTracker;

    public KillListener(KillManager killManager) {
        this.killActionTracker = new KillActionTracker(killManager);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            String killerName = event.getEntity().getKiller().getName();
            String victimType = event.getEntity().getType().toString();
            String location = event.getEntity().getLocation().getBlockX() + "/" +
                    event.getEntity().getLocation().getBlockY() + "/" +
                    event.getEntity().getLocation().getBlockZ();
            killActionTracker.trackKillAction(killerName, victimType, location);
        }
    }
}
