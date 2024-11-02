package de.jakomi1.advancedBlockProtect;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakListener implements Listener {
    private BreakActionTracker breakBlockTracker;
    private GetData dataRetriever;

    public BreakListener(DatabaseManager breakBlockDbManager, GetData dataRetriever) {
        this.breakBlockTracker = new BreakActionTracker(breakBlockDbManager);
        this.dataRetriever = dataRetriever;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.WOODEN_PICKAXE) {
            event.setCancelled(true);
            String location = event.getBlock().getLocation().getBlockX() + "/" +
                    event.getBlock().getLocation().getBlockY() + "/" +
                    event.getBlock().getLocation().getBlockZ();
            dataRetriever.getBlockData(location, event.getPlayer());
        } else {
            String playerName = event.getPlayer().getName();
            String blockType = event.getBlock().getType().toString();
            String location = event.getBlock().getLocation().getBlockX() + "/" +
                    event.getBlock().getLocation().getBlockY() + "/" +
                    event.getBlock().getLocation().getBlockZ();
            breakBlockTracker.trackBlockAction(playerName, blockType, location);
        }
    }
}
