package de.jakomi1.advancedBlockProtect;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class GetData {
    private DatabaseManager breakBlockDbManager;
    private DatabaseManager placeBlockDbManager;
    private KillManager killManager;

    public GetData(DatabaseManager breakBlockDbManager, DatabaseManager placeBlockDbManager, KillManager killManager) {
        this.breakBlockDbManager = breakBlockDbManager;
        this.placeBlockDbManager = placeBlockDbManager;
        this.killManager = killManager;
    }

    public void getBlockData(String location, CommandSender sender) {
        String sql = "SELECT playerName, blockType, date, time FROM log WHERE location = ? ORDER BY date DESC, time DESC";
        LocalDateTime now = LocalDateTime.now();
        boolean dataFound = false;

        try (PreparedStatement breakStmt = breakBlockDbManager.getConnection().prepareStatement(sql);
             PreparedStatement placeStmt = placeBlockDbManager.getConnection().prepareStatement(sql);
             PreparedStatement killStmt = killManager.getConnection().prepareStatement(sql)) {
            breakStmt.setString(1, location);
            placeStmt.setString(1, location);
            killStmt.setString(1, location);

            var breakRs = breakStmt.executeQuery();
            var placeRs = placeStmt.executeQuery();
            var killRs = killStmt.executeQuery();

            sender.sendMessage(ChatColor.BLUE + "[AdvancedBlockProtect] - " + ChatColor.WHITE + location);

            while (breakRs.next() || placeRs.next() || killRs.next()) {
                String playerName;
                String blockType;
                String action;
                String dateStr;
                LocalDateTime dateTime;
                if (breakRs.next()) {
                    playerName = breakRs.getString("playerName");
                    blockType = breakRs.getString("blockType");
                    dateStr = breakRs.getString("date") + " " + breakRs.getString("time");
                    dateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    action = "broke";
                } else if (placeRs.next()) {
                    playerName = placeRs.getString("playerName");
                    blockType = placeRs.getString("blockType");
                    dateStr = placeRs.getString("date") + " " + placeRs.getString("time");
                    dateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    action = "placed";
                } else {
                    playerName = killRs.getString("playerName");
                    blockType = killRs.getString("blockType");
                    dateStr = killRs.getString("date") + " " + killRs.getString("time");
                    dateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    action = "killed";
                }

                Duration duration = Duration.between(dateTime, now);
                String timeAgo;

                long days = duration.toDays();
                long hours = duration.toHours();
                long minutes = duration.toMinutes();
                long seconds = duration.getSeconds();
                if (days > 0) {
                    timeAgo = days + "d";
                } else if (hours > 0) {
                    timeAgo = hours + "h";
                } else if (minutes > 0) {
                    timeAgo = minutes + "m";
                } else {
                    timeAgo = seconds + "s";
                }

                sender.sendMessage(ChatColor.BLUE + timeAgo + ChatColor.WHITE + " - " + ChatColor.GREEN + playerName + ChatColor.WHITE + " " + ChatColor.BOLD + action + " " + blockType + ChatColor.RESET);

                dataFound = true;
            }

            if (!dataFound) {
                sender.sendMessage(ChatColor.BLUE + "[AdvancedBlockProtect] " + ChatColor.WHITE + "Keine Daten hier gefunden.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
