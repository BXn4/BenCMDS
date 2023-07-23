package bxn4.bencmds;

import bxn4.bencmds.commands.reminder.Reminder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.PublicKey;
import java.sql.*;

public class Database {
    public String databaseType;
    public String databaseServer;
    public String databasePort;
    public String databaseName;
    public String databaseUsername;
    public String databasePassword;
    public Boolean ConnectionIsSuccessful = false;
    String databaseFileUrl = null;
    String databaseUrl = null;

    public void connectToDatbase() {
        Config config = Config.getInstance();
        databaseType = config.databaseType;
        databaseServer = config.databaseServer;
        databasePort = config.databasePort;
        databaseName = config.databaseName;
        databaseUsername = config.databaseUsername;
        databasePassword = config.databasePassword;
        databaseFileUrl = databaseServer;
        Connection conn = connect();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT 1");
                ConnectionIsSuccessful = true;
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                ConnectionIsSuccessful = false;
                e.printStackTrace();
            }
        }
    }

    public Connection connect() {
        Connection conn = null;
        Config config = Config.getInstance();
        databaseType = config.databaseType;
        databaseServer = config.databaseServer;
        databasePort = config.databasePort;
        databaseName = config.databaseName;
        databaseUsername = config.databaseUsername;
        databasePassword = config.databasePassword;
        databaseFileUrl = databaseServer;
        try {
            if (databaseType.equals("SQLite")) {
                File file = new File(databaseFileUrl);
                if (file.exists()) {
                    databaseUrl = "jdbc:sqlite:" + databaseFileUrl;
                    conn = DriverManager.getConnection(databaseUrl);
                } else {
                    ConnectionIsSuccessful = false;
                    System.out.println("[ERROR] Failed to connect database: Database file not found.");
                }
            } else {
                databaseUrl = "jdbc:" + databaseType.toLowerCase() + "://" + databaseServer + ":" + databasePort + "/" + databaseName;
                conn = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
            }
        } catch (SQLException e) {
            ConnectionIsSuccessful = false;
            e.printStackTrace();
        }
        return conn;
    }

    public void runSQLCommand(String table, String command, Double userId) {
        Integer id;
        Config config = Config.getInstance();
        databaseType = config.databaseType;
        databaseServer = config.databaseServer;
        databasePort = config.databasePort;
        databaseName = config.databaseName;
        databaseUsername = config.databaseUsername;
        databasePassword = config.databasePassword;
        Reminder reminder = new Reminder();
        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(command);
            switch (table) {
                // TODO
                case "reminders" -> {
                    if (rs.next()) {
                        id = rs.getInt("reminderId");
                        reminder.setReminderId(id);
                    }
                    else {
                        id = 0;
                        reminder.setReminderId(id);
                    }
                }
            }
            stmt.execute(command);
            stmt.close();
    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void insertToDatabase(String command) {
        Config config = Config.getInstance();
        databaseType = config.databaseType;
        databaseServer = config.databaseServer;
        databasePort = config.databasePort;
        databaseName = config.databaseName;
        databaseUsername = config.databaseUsername;
        databasePassword = config.databasePassword;
        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(command);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
