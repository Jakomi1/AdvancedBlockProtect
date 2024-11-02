package de.jakomi1.advancedBlockProtect;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.plugin.Plugin;

public class DatabaseManager {
    private Connection connection;
    private Plugin plugin;

    public DatabaseManager(Plugin plugin, String dbName) {
        this.plugin = plugin;
        openConnection(dbName);
        createTable();
    }

    public void openConnection(String dbName) {
        try {
            File dataFolder = new File(plugin.getDataFolder(), "database");
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder + "/" + dbName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS log (" +
                "playerName TEXT, " +
                "blockType TEXT, " +
                "location TEXT, " +
                "date TEXT, " +
                "time TEXT)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void logBlockAction(String playerName, String blockType, String location, String date, String time) {
        String sql = "INSERT INTO log(playerName, blockType, location, date, time) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, playerName);
            stmt.setString(2, blockType);
            stmt.setString(3, location);
            stmt.setString(4, date);
            stmt.setString(5, time);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearDatabase() {
        String sql = "DELETE FROM log";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
