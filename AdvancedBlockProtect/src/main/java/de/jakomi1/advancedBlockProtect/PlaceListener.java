package de.jakomi1.advancedBlockProtect;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceListener implements Listener {
    private PlaceActionTracker placeBlockTracker;

    public PlaceListener(DatabaseManager placeBlockDbManager) {
        this.placeBlockTracker = new PlaceActionTracker(placeBlockDbManager);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        String playerName = event.getPlayer().getName();
        String blockType = event.getBlock().getType().toString();
        String location = event.getBlock().getLocation().getBlockX() + "/" +
                event.getBlock().getLocation().getBlockY() + "/" +
                event.getBlock().getLocation().getBlockZ();
        placeBlockTracker.trackBlockAction(playerName, blockType, location);
    }
}
