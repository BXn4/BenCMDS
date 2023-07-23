package bxn4.bencmds.GUI;

import bxn4.bencmds.Config;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;

public class BotSettingsGUI {
    String botToken;
    String botStatus;
    String botActivityType;
    String botActivity;
    String botPrefix;
    String databaseType;
    String databaseServer;
    String databasePort;
    String databaseName;
    String databaseUsername;
    String databasePassword;
    String botStreamUrl;
    JButton finishBtn;
    Boolean connectionIsSuccessFul = false;
    String[] sqlCommand = {
            "CREATE TABLE IF NOT EXISTS `userData` (\n" +
                    "`userId` BIGINT,\n" +
                    "`userMoney` BIGINT,\n" +
                    "`userBankClosed` INTEGER\n" +
                    ");",
            "CREATE TABLE IF NOT EXISTS `reminders` (\n" +
                    "`reminderId` BIGINT,\n" +
                    "`reminderUserId` BIGINT,\n" +
                    "`reminderCreatorUserId` BIGINT,\n" +
                    "`reminderTime` BIGINT,\n" +
                    "`reminderAbout` TEXT\n" +
                    ");"
    };
    JTextField prefixTxtFld;

    public void makeGUI() {
        Config config = Config.getInstance();
        botToken = config.botToken;
        botStatus = config.botStatus;
        botActivityType = config.botActivityType;
        botStreamUrl = config.botStreamUrl;
        botActivity = config.botActivity;
        botPrefix = config.botPrefix;
        databaseType = config.databaseType;
        databaseServer = config.databaseServer;
        databasePort = config.databasePort;
        databaseName = config.databaseName;
        databaseUsername = config.databaseUsername;
        databasePassword = config.databasePassword;
        String[] statuses = {"online", "idle", "dnd", "invisible"};
        String[] types = {"listening", "streaming", "playing", "competing", "watching"};
        String[] databaseTypes = {"SQLite", "MySQL", "MariaDB", "PostgreSQL"};
        JFrame frame = new JFrame("Set-up");
        JPanel panel = new JPanel();

        JLabel botStatusLbl = new JLabel("Status:");
        JLabel botTypeLbl = new JLabel("Type:");
        JLabel botStreamUrlLbl = new JLabel("Stream Url:");
        JLabel botActivityLbl = new JLabel("Activity:");
        JLabel botPrefixLbl = new JLabel("Prefix (for custom commands):");
        JLabel databaseTypeLbl = new JLabel("Database:");
        JLabel databaseServerLbl = new JLabel("Server:");
        JLabel databasePortLbl = new JLabel("Port:");
        JLabel databaseNameLbl = new JLabel("Database name:");
        JLabel databaseUsernameLbl = new JLabel("Username:");
        JLabel databasePasswordLbl = new JLabel("Password:");

        botStreamUrlLbl.setVisible(false);
        JComboBox statusCmbBx = new JComboBox(statuses);
        statusCmbBx.setSelectedIndex(0);
        JComboBox typeCmbBx = new JComboBox(types);
        typeCmbBx.setSelectedIndex(0);
        JComboBox databaseTypeCmbx = new JComboBox(databaseTypes);
        databaseTypeCmbx.setSelectedIndex(0);

        JButton cancelBtn = new JButton("Cancel");
        JButton browseBtn = new JButton("...");
        JButton testConnectionBtn = new JButton("Test connection");
        JButton createDatabaseBtn = new JButton("Create");
        finishBtn = new JButton("Finish");

        JTextField streamUrlTxtFld = new JTextField("Enter Stream Url here...");
        JTextField activityTxtFld = new JTextField("Enter custom activity here...");
        prefixTxtFld = new JTextField("$");
        JTextField serverUrlTxtFld = new JTextField("");
        JTextField portTxtFld = new JTextField("");
        JTextField databaseTxtFld = new JTextField("");
        JTextField usernameTxtFld = new JTextField("");
        JTextField passwordTxtFld = new JTextField("");

        if (botStreamUrl == null) {
            streamUrlTxtFld.setVisible(false);
        } else {
            if (!botActivityType.equals("streaming")) {
                streamUrlTxtFld.setVisible(false);
            }
        }
        finishBtn.setEnabled(true);
        if (databaseType == null) {
            databaseType = "SQLite";
        }
        if (databaseType != null) {
            if (databaseType.equals("SQLite")) {
                browseBtn.setVisible(true);
                portTxtFld.setEnabled(false);
                databaseTxtFld.setEnabled(false);
                usernameTxtFld.setEnabled(false);
                passwordTxtFld.setEnabled(false);
                portTxtFld.setText("");
            } else {
                browseBtn.setVisible(false);
                portTxtFld.setEnabled(true);
                databaseTxtFld.setEnabled(true);
                usernameTxtFld.setEnabled(true);
                passwordTxtFld.setEnabled(true);
                portTxtFld.setText(databasePort);
            }
        }
        if (botStatus != null) {
            switch (botStatus) {
                case "online" -> statusCmbBx.setSelectedIndex(0);
                case "idle" -> statusCmbBx.setSelectedIndex(1);
                case "dnd" -> statusCmbBx.setSelectedIndex(2);
                case "invisible" -> statusCmbBx.setSelectedIndex(3);
                default -> statusCmbBx.setSelectedIndex(0);
            }
        }
        if (botActivityType != null) {
            switch (botActivityType) {
                case "listening" -> typeCmbBx.setSelectedIndex(0);
                case "streaming" -> typeCmbBx.setSelectedIndex(1);
                case "playing" -> typeCmbBx.setSelectedIndex(2);
                case "competing" -> typeCmbBx.setSelectedIndex(3);
                case "watching" -> typeCmbBx.setSelectedIndex(4);
                default -> typeCmbBx.setSelectedIndex(0);
            }
        } else {
            botActivityType = "online";
        }
        if (databaseType != null) {
            switch (databaseType) {
                case "SQLite" -> databaseTypeCmbx.setSelectedIndex(0);
                case "MySQL" -> databaseTypeCmbx.setSelectedIndex(1);
                case "MariaDB" -> databaseTypeCmbx.setSelectedIndex(2);
                case "PostgreSQL" -> databaseTypeCmbx.setSelectedIndex(3);
                default -> databaseTypeCmbx.setSelectedIndex(0);
            }
        } else {
            databaseType = "SQLite";
        }
        if(botStreamUrl != null) {
            streamUrlTxtFld.setText(botStreamUrl);
        }
        if(botActivity != null) {
            activityTxtFld.setText(botActivity);
        }
        if(botPrefix != null) {
            prefixTxtFld.setText(botPrefix);
        }
        if(databaseServer != null) {
            serverUrlTxtFld.setText(databaseServer);
        }
        if(databasePort != null) {
            portTxtFld.setText(databasePort);
        }
        if(databaseName != null) {
            databaseTxtFld.setText(databaseName);
        }
        if(databaseUsername != null) {
            usernameTxtFld.setText(databaseUsername);
        }
        if(databasePassword != null) {
            passwordTxtFld.setText(databasePassword);
        }

        botStatusLbl.setBounds(10, 10, 100, 20);
        botTypeLbl.setBounds(10, 50, 100, 20);
        botStreamUrlLbl.setBounds(175, 50, 100, 20);
        botActivityLbl.setBounds(10, 90, 100, 20);
        botPrefixLbl.setBounds(10, 150, 200, 20);
        databaseTypeLbl.setBounds(250, 90, 100, 20);
        databaseServerLbl.setBounds(250, 120, 100, 20);
        databasePortLbl.setBounds(250, 170, 100, 20);
        databaseNameLbl.setBounds(250, 220, 100, 20);
        databaseUsernameLbl.setBounds(400, 170, 100, 20);
        databasePasswordLbl.setBounds(400, 220, 100, 20);

        statusCmbBx.setBounds(60, 10, 100, 20);
        typeCmbBx.setBounds(60, 50, 100, 20);
        databaseTypeCmbx.setBounds(320, 90, 100, 20);

        finishBtn.setBounds(530, 280, 100, 20);
        cancelBtn.setBounds(10, 280, 100, 20);
        browseBtn.setBounds(450, 140, 20, 20);
        testConnectionBtn.setBounds(480, 140, 150, 20);
        createDatabaseBtn.setBounds(550, 170, 80, 20);
        finishBtn.setEnabled(false);

        streamUrlTxtFld.setBounds(250, 50, 200, 20);
        activityTxtFld.setBounds(10, 110, 200, 20);
        prefixTxtFld.setBounds(10, 170, 50, 20);
        serverUrlTxtFld.setBounds(250, 140, 200, 20);
        portTxtFld.setBounds(250, 190, 60, 20);
        databaseTxtFld.setBounds(250, 240, 100, 20);
        usernameTxtFld.setBounds(400, 190, 100, 20);
        passwordTxtFld.setBounds(400, 240, 100, 20);

        frame.add(botStatusLbl);
        frame.add(botTypeLbl);
        frame.add(botStreamUrlLbl);
        frame.add(botActivityLbl);
        frame.add(botPrefixLbl);
        frame.add(databaseTypeLbl);
        frame.add(databaseServerLbl);
        frame.add(databasePortLbl);
        frame.add(databaseNameLbl);
        frame.add(databaseUsernameLbl);
        frame.add(databasePasswordLbl);

        frame.add(statusCmbBx);
        frame.add(typeCmbBx);
        frame.add(databaseTypeCmbx);

        frame.add(finishBtn);
        frame.add(cancelBtn);
        frame.add(browseBtn);
        frame.add(testConnectionBtn);
        frame.add(createDatabaseBtn);

        frame.add(streamUrlTxtFld);
        frame.add(activityTxtFld);
        frame.add(prefixTxtFld);
        frame.add(serverUrlTxtFld);
        frame.add(portTxtFld);
        frame.add(databaseTxtFld);
        frame.add(usernameTxtFld);
        frame.add(passwordTxtFld);

        frame.setSize(660, 350);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(panel);
        frame.setVisible(true);

        statusCmbBx.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer statusCmmBxValue = statusCmbBx.getSelectedIndex();
                switch (statusCmmBxValue) {
                    case 0 -> botStatus = "online";
                    case 1 -> botStatus = "idle";
                    case 2 -> botStatus = "dnd";
                    case 3 -> botStatus = "invisible";
                }
                System.out.println("Bot status set to: " + botStatus);
            }
        });

        typeCmbBx.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer typeCmBxValue = typeCmbBx.getSelectedIndex();
                switch (typeCmBxValue) {
                    case 0 -> {
                        botActivityType = "listening";
                        streamUrlTxtFld.setVisible(false);
                    }
                    case 1 -> {
                        botActivityType = "streaming";
                        streamUrlTxtFld.setVisible(true);
                    }
                    case 2 -> {
                        botActivityType = "playing";
                        streamUrlTxtFld.setVisible(false);
                    }
                    case 3 -> {
                        botActivityType = "competing";
                        streamUrlTxtFld.setVisible(false);
                    }
                    case 4 -> {
                        botActivityType = "watching";
                        streamUrlTxtFld.setVisible(false);
                    }
                }
                System.out.println("Bot activity type set to: " + botActivityType);
            }
        });

        databaseTypeCmbx.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer databaseTypeCmbxValue = databaseTypeCmbx.getSelectedIndex();
                connectionIsSuccessFul = false;
                finishBtn.setEnabled(false);
                switch (databaseTypeCmbxValue) {
                    case 0 -> {
                        browseBtn.setVisible(true);
                        portTxtFld.setEnabled(false);
                        databaseTxtFld.setEnabled(false);
                        usernameTxtFld.setEnabled(false);
                        passwordTxtFld.setEnabled(false);
                        portTxtFld.setText("");
                        databaseType = "SQLite";
                    }
                    case 1 -> {
                        browseBtn.setVisible(false);
                        portTxtFld.setEnabled(true);
                        databaseTxtFld.setEnabled(true);
                        usernameTxtFld.setEnabled(true);
                        passwordTxtFld.setEnabled(true);
                        portTxtFld.setText("3306");
                        databaseType = "MySQL";
                    }
                    case 2 -> {
                        browseBtn.setVisible(false);
                        portTxtFld.setEnabled(true);
                        databaseTxtFld.setEnabled(true);
                        usernameTxtFld.setEnabled(true);
                        passwordTxtFld.setEnabled(true);
                        portTxtFld.setText("3306");
                        databaseType = "MariaDB";
                    }
                    case 3 -> {
                        browseBtn.setVisible(false);
                        portTxtFld.setEnabled(true);
                        databaseTxtFld.setEnabled(true);
                        usernameTxtFld.setEnabled(true);
                        passwordTxtFld.setEnabled(true);
                        portTxtFld.setText("5432");
                        databaseType = "PostgreSQL";
                    }
                }
                System.out.println("Database set to: " + databaseType);
            }
        });


        activityTxtFld.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                check();
            }

            public void changedUpdate(DocumentEvent e) {
                check();
            }

            public void check() {
                if (activityTxtFld.getText().length() != 0 && !activityTxtFld.getText().equals("Enter custom activity here...")) {
                    botActivity = activityTxtFld.getText();
                }
            }
        });

        activityTxtFld.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (activityTxtFld.getText().equals("Enter custom activity here...")) {
                    activityTxtFld.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (activityTxtFld.getText().equals("")) {
                    activityTxtFld.setText("Enter custom activity here...");
                } else {
                    System.out.println("Bot activity is set to: " + botActivity);
                }
            }
        });

        streamUrlTxtFld.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                check();
            }

            public void changedUpdate(DocumentEvent e) {
                check();
            }

            public void check() {
                if (streamUrlTxtFld.getText().length() != 0 && !streamUrlTxtFld.getText().equals("Enter Stream Url here...")) {
                    botStreamUrl = streamUrlTxtFld.getText();
                }
            }
        });

        streamUrlTxtFld.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (streamUrlTxtFld.getText().equals("Enter Stream Url here...")) {
                    streamUrlTxtFld.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (streamUrlTxtFld.getText().equals("")) {
                    streamUrlTxtFld.setText("Enter Stream Url here...");
                } else {
                    System.out.println("Bot stream url is set to: " + botStreamUrl);
                }
            }
        });

        serverUrlTxtFld.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                check();
            }

            public void changedUpdate(DocumentEvent e) {
                check();
            }

            public void check() {
                if (serverUrlTxtFld.getText().length() != 0) {
                    databaseServer = serverUrlTxtFld.getText();
                }
            }
        });

        serverUrlTxtFld.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!serverUrlTxtFld.getText().equals("")) {
                    System.out.println("Database server url is set to: " + databaseServer);
                }
            }
        });

        usernameTxtFld.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                check();
            }

            public void changedUpdate(DocumentEvent e) {
                check();
            }

            public void check() {
                if (usernameTxtFld.getText().length() != 0) {
                    databaseUsername = usernameTxtFld.getText();
                }
            }
        });

        usernameTxtFld.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!usernameTxtFld.getText().equals("")) {
                    System.out.println("Database username is set to: " + databaseUsername);
                }
            }
        });

        passwordTxtFld.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                check();
            }

            public void changedUpdate(DocumentEvent e) {
                check();
            }

            public void check() {
                databasePassword = passwordTxtFld.getText();
            }
        });

        passwordTxtFld.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("Database password is set to: " + databasePassword);
            }
        });

        databaseTxtFld.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                check();
            }

            public void changedUpdate(DocumentEvent e) {
                check();
            }

            public void check() {
                if (databaseTxtFld.getText().length() != 0) {
                    databaseName = databaseTxtFld.getText();
                }
            }
        });

        databaseTxtFld.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!databaseTxtFld.getText().equals("")) {
                    System.out.println("Database name is set to: " + databaseName);
                }
            }
        });

        prefixTxtFld.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                check();
            }

            public void changedUpdate(DocumentEvent e) {
                check();
            }

            public void check() {
                if (prefixTxtFld.getText().length() != 0) {
                    botPrefix = prefixTxtFld.getText();
                }
            }
        });

        prefixTxtFld.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!prefixTxtFld.getText().equals("")) {
                    botPrefix = prefixTxtFld.getText();
                    System.out.println("Prefix is set to: " + botPrefix);
                }
            }
        });

        portTxtFld.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                check();
            }

            public void changedUpdate(DocumentEvent e) {
                check();
            }

            public void check() {
                if (portTxtFld.getText().length() != 0) {
                    databasePort = portTxtFld.getText();
                }
            }
        });

        portTxtFld.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!portTxtFld.getText().equals("")) {
                    databasePort = portTxtFld.getText();
                    System.out.println("Port is set to: " + databasePort);
                }
            }
        });



        testConnectionBtn.addActionListener(new ActionListener() {
            String databaseFileUrl = null;
            String databaseUrl = null;

            @Override
            public void actionPerformed(ActionEvent e) {
                databaseFileUrl = databaseServer;
                Connection conn = connect();
                if (conn != null) {
                    try {
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT 1");
                        JOptionPane.showMessageDialog(null, "Connection successful!", "Test connection", JOptionPane.INFORMATION_MESSAGE);
                        connectionIsSuccessFul = true;
                        finishBtn.setEnabled(true);
                        rs.close();
                        stmt.close();
                        conn.close();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Test connection", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }

            public Connection connect() {
                Connection conn = null;
                try {
                    if (databaseType.equals("SQLite")) {
                        File file = new File(databaseFileUrl);
                        if (file.exists()) {
                            databaseUrl = "jdbc:sqlite:" + databaseFileUrl;
                            conn = DriverManager.getConnection(databaseUrl);
                        } else {
                            Toolkit.getDefaultToolkit().beep();
                            JOptionPane.showMessageDialog(null, "File not found!", "Test connection", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        databaseUrl = "jdbc:" + databaseType.toLowerCase() + "://" + databaseServer + ":" + databasePort + "/" + databaseName;
                        conn = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Test connection", JOptionPane.WARNING_MESSAGE);
                }
                return conn;
            }
        });

        createDatabaseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (databaseType.equals("SQLite")) {
                    File tempDatabase = null;
                    try {
                        tempDatabase = File.createTempFile("database", ".db");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, ex, "Create Database", JOptionPane.WARNING_MESSAGE);
                    }
                    String tempDatabaseUrl = tempDatabase.getAbsolutePath();
                    try {
                        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + tempDatabaseUrl);
                        if (conn != null) {
                            Statement stmt = conn.createStatement();
                            for (int i = 0; i < sqlCommand.length; i++) {
                                stmt.execute(sqlCommand[i]);
                            }
                            stmt.close();
                            conn.close();
                        }
                    } catch (SQLException ex) {
                    }
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setSelectedFile(new File("database.db"));
                    fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                    int result = fileChooser.showSaveDialog(frame);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        Path path = file.toPath();
                        try {
                            if (!file.exists()) {
                                Files.copy(Path.of(tempDatabaseUrl), path);
                                JOptionPane.showMessageDialog(null, "Database created.", "Create Database", JOptionPane.INFORMATION_MESSAGE);
                                serverUrlTxtFld.setText(path.toString());
                                databaseServer = serverUrlTxtFld.getText();
                                System.out.println("Database server url is set to: " + databaseServer);
                            } else {
                                int dialogResult = JOptionPane.showConfirmDialog(null, "The file exists. Do you want to overwrite it?", "Create Database", JOptionPane.YES_NO_OPTION);
                                if (dialogResult == JOptionPane.YES_OPTION) {
                                    Files.copy(Path.of(tempDatabaseUrl), path, StandardCopyOption.REPLACE_EXISTING);
                                    JOptionPane.showMessageDialog(null, "Database created.", "Create Database", JOptionPane.INFORMATION_MESSAGE);
                                    serverUrlTxtFld.setText(path.toString());
                                    databaseServer = serverUrlTxtFld.getText();
                                    System.out.println("Database server url is set to: " + databaseServer);

                                } else {
                                    tempDatabase.delete();
                                }
                            }
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, ex, "Create Database", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        tempDatabase.delete();
                    }
                } else {
                    String createDatabaseStr = "CREATE DATABASE IF NOT EXISTS " + databaseName + ";";
                    try {
                        Connection conn = DriverManager.getConnection("jdbc:" + databaseType.toLowerCase() + "://" + databaseServer + ":" + databasePort + "/", databaseUsername, databasePassword);
                        if (conn != null) {
                            Statement stmt = conn.createStatement();
                            stmt.execute(createDatabaseStr);
                            stmt.execute("USE " + databaseName);
                            for (int i = 0; i < sqlCommand.length; i++) {
                                stmt.execute(sqlCommand[i]);
                            }
                            stmt.close();
                            conn.close();
                            JOptionPane.showMessageDialog(null, "Database created (" + databaseName + ").", "Create Database", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex);
                        JOptionPane.showMessageDialog(null, ex, "Create Database", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        browseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Database File", "db");
                fileChooser.addChoosableFileFilter(filter);
                fileChooser.setFileFilter(filter);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    serverUrlTxtFld.setText(fileChooser.getSelectedFile().toString());
                    databaseServer = serverUrlTxtFld.getText();
                    System.out.println("Database server url is set to: " + databaseServer);
                }
            }
        });

        finishBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(connectionIsSuccessFul) {
                    botStatus = statusCmbBx.getSelectedItem().toString();
                    botActivityType = typeCmbBx.getSelectedItem().toString();
                    botStreamUrl = streamUrlTxtFld.getText();
                    botActivity = activityTxtFld.getText();
                    botPrefix = prefixTxtFld.getText();
                    databaseType = databaseTypeCmbx.getSelectedItem().toString();
                    databaseServer = serverUrlTxtFld.getText();
                    databasePort = portTxtFld.getText();
                    databaseName = databaseTxtFld.getText();
                    databaseUsername = usernameTxtFld.getText();
                    databasePassword = passwordTxtFld.getText();
                    Config config = Config.getInstance();
                    config.botStatus = botStatus;
                    config.botActivityType = botActivityType;
                    config.botStreamUrl = botStreamUrl;
                    config.botActivity = botActivity;
                    config.botPrefix = botPrefix;
                    config.databaseType = databaseType;
                    config.databaseServer = databaseServer;
                    config.databasePort = databasePort;
                    config.databaseName = databaseName;
                    config.databaseUsername = databaseUsername;
                    config.databasePassword = databasePassword;
                    config.saveConfig();
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    System.gc();
                }
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                System.gc();
            }
        });
    }
}