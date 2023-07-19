package bxn4.bencmds;

import bxn4.bencmds.commands.weather.WeatherData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtattoo.plaf.graphite.GraphiteLookAndFeel;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
public class GUI {
    public JLabel serversLbl = new JLabel("");
    public JTextArea logArea = new JTextArea("Nothing to do...");
    public String token = null;
    public String status = null;
    public String type = null;
    public String streamUrl = null;
    public String activity = null;
    public String prefix = null;
    String databaseFileUrl = null;
    String databaseUrl = null;
    String database = null;
    String serverUrl = null;
    String port = null;
    String databaseName = null;
    String username =  null;
    String password = null;
    boolean streamTxtFldIsEmpty = true;
    boolean activityTxtFldIsEmpty = true;
    boolean prefixTxtFldIsEmpty = false;
    boolean serverTxtFldIsEmpty = true;
    boolean portTxtFldIsEmpty = true;
    boolean databaseNameTxtFldIsEmpty = true;
    boolean usernameTxtFldIsEmpty = true;
    boolean passwordTxtFldIsEmpty = false;
    boolean sqliteSelected = true;
    boolean conectionIsSuccessFul = false;
    boolean streamType = false;
    private Map<String, Object> map = new LinkedHashMap<>();
    Connection conn = null;
    String sqlCommand =
            "CREATE TABLE IF NOT EXISTS `userData` (\n" +
                    "`userId` INTEGER,\n" +
                    "`userMoney` INTEGER,\n" +
                    "`userBankClosed` INTEGER\n" +
                    ");";
    JButton finishBtn = new JButton("Finish");
    Map<String, Object> data = null;
    public void MakeGui() {
        try {
            UIManager.setLookAndFeel(GraphiteLookAndFeel.class.getName());
        }
        catch (Exception e) {
        }
        logArea.setEditable(false);
        JScrollPane sp = new JScrollPane(logArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        BenCMDS benCMDS = new BenCMDS(this);
        JFrame frame = new JFrame("BenCMDS");
        JPanel panel = new JPanel();
        JButton startStopBtn = new JButton("Start");
        JButton setUpBtn = new JButton("Set-up");
        JButton configBtn = new JButton("Config");
        JButton logBtn = new JButton("View Log");
        startStopBtn.setBounds(575,330,100,20);
        setUpBtn.setBounds(575,300,100,20);
        configBtn.setBounds(575,270,100,20);
        logBtn.setBounds(575,240,100,20);
        serversLbl.setBounds(5,340,100,20);
        logArea.setBounds(5,240,560,100);
        frame.add(startStopBtn);
        frame.add(setUpBtn);
        frame.add(configBtn);
        frame.add(logBtn);
        frame.add(serversLbl);
        frame.add(logArea);
        frame.getContentPane().add(sp);
        frame.setSize(700,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(panel);
        frame.setVisible(true);
        startStopBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (startStopBtn.getText()) {
                    case "Start":
                        benCMDS.StartBot();
                        startStopBtn.setText("Stop");
                        break;
                    case "Stop":
                        benCMDS.StopBot();
                        startStopBtn.setText("Start");
                }
            }
        });
        setUpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MakeSetUpGUI();
            }
        });
        configBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MakeConfigGui();
            }
        });
    }

    public void MakeSetUpGUI() {
        JFrame frame = new JFrame("Set-up");
        JPanel panel = new JPanel();
        JTextField tokenTextField = new JTextField("");
        JButton nextBtn = new JButton("Next");
        nextBtn.setEnabled(false);
        JButton cancelBtn = new JButton("Cancel");
        JButton getTokenBtn = new JButton("Get Token");
        tokenTextField.setBounds(40,10,300,25);
        nextBtn.setBounds(270,80,100,20);
        cancelBtn.setBounds(10,80,100,20);
        getTokenBtn.setBounds(270,50,100,20);
        frame.add(tokenTextField);
        frame.add(nextBtn);
        frame.add(cancelBtn);
        frame.add(getTokenBtn);
        frame.setSize(400,150);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(panel);
        frame.setVisible(true);
        tokenTextField.setText(token);
        if(tokenTextField.getText().length() > 0) {
            nextBtn.setEnabled(true);
        }
        tokenTextField.getDocument().addDocumentListener(new DocumentListener() {
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
                if(tokenTextField.getText().length() > 0 || !tokenTextField.getText().equals("")) {
                    nextBtn.setEnabled(true);
                }
                else {
                    nextBtn.setEnabled(false);
                }
            }
        });
        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                token = tokenTextField.getText();
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                MakeSetUpGUI2();
            }
        });
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                System.gc();
            }
        });
        getTokenBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                URI uri = null;
                try {
                    uri= new URI("https://discord.com/developers/applications");
                } catch (URISyntaxException ex) {
                }
                try {
                    java.awt.Desktop.getDesktop().browse(uri);
                    uri = null;
                } catch (IOException ex) {
                }
            }
        });
    }
    private void MakeSetUpGUI2() {
        String[] statuses = { "online", "idle", "dnd", "invisible" };
        String[] types = { "listening", "streaming", "playing", "competing", "watching" };
        String[] databaseTypes = { "SQLite", "MySQL", "MariaDB", "PostgreSQL" };
        JFrame frame = new JFrame("Set-up");
        JPanel panel = new JPanel();
        JLabel statusLbl = new JLabel("Status:");
        JLabel typeLbl = new JLabel("Type:");
        JLabel streamUrlLbl = new JLabel("Stream Url:");
        JLabel activityLbl = new JLabel("Activity:");
        JLabel prefixLbl = new JLabel("Prefix (for custom commands):");
        JLabel databaseTypeLbl = new JLabel("Database:");
        JLabel serverUrlLbl = new JLabel("Server:");
        JLabel portLbl = new JLabel("Port:");
        JLabel databaseLbl = new JLabel("Database name:");
        JLabel usernameLbl = new JLabel("Username:");
        JLabel passwordLbl = new JLabel("Password:");
        streamUrlLbl.setVisible(false);
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

        JTextField streamUrlTxtFld = new JTextField("Enter Stream Url here...");
        JTextField activityTxtFld = new JTextField("Enter custom activity here...");
        JTextField prefixTxtFld = new JTextField("$");
        JTextField serverUrlTxtFld = new JTextField("");
        JTextField portTxtFld = new JTextField("");
        JTextField databaseTxtFld = new JTextField("");
        JTextField usernameTxtFld = new JTextField("");
        JTextField passwordTxtFld = new JTextField("");
        streamUrlTxtFld.setVisible(false);
        finishBtn.setEnabled(false);
        portTxtFld.setEnabled(false);
        usernameTxtFld.setEnabled(false);
        passwordTxtFld.setEnabled(false);
        databaseTxtFld.setEnabled(false);
        File file = new File("config.yaml");
        if (file.exists()) {
            if (database.equals("")) {
                database = "sqlite";
            }
            switch (status) {
                case "online":
                    statusCmbBx.setSelectedIndex(0);
                    break;
                case "idle":
                    statusCmbBx.setSelectedIndex(1);
                    break;
                case "dnd":
                    statusCmbBx.setSelectedIndex(2);
                    break;
                case "invisible":
                    statusCmbBx.setSelectedIndex(3);
                    break;
            }
            switch (type) {
                case "listening":
                    typeCmbBx.setSelectedIndex(0);
                    break;
                case "streaming":
                    typeCmbBx.setSelectedIndex(1);
                    break;
                case "playing":
                    typeCmbBx.setSelectedIndex(2);
                    break;
                case "competing":
                    typeCmbBx.setSelectedIndex(3);
                    break;
                case "watching":
                    typeCmbBx.setSelectedIndex(4);
                    break;
            }
            activityTxtFld.setText(activity);
            prefixTxtFld.setText(prefix);
            switch (database) {
                case "sqlite":
                    databaseTypeCmbx.setSelectedIndex(0);
                    break;
                case "mysql":
                    databaseTypeCmbx.setSelectedIndex(1);
                    break;
                case "mariadb":
                    databaseTypeCmbx.setSelectedIndex(2);
                    break;
                case "postgresql":
                    databaseTypeCmbx.setSelectedIndex(3);
                    break;
            }
            serverUrlTxtFld.setText(databaseUrl);
            portTxtFld.setText(port);
            databaseTxtFld.setText(databaseName);
            usernameTxtFld.setText(username);
            passwordTxtFld.setText(password);
            if(!serverUrlTxtFld.equals(""))
            {
                if(!database.equals("sqlite")) {
                    portTxtFld.setEnabled(true);
                    databaseTxtFld.setEnabled(true);
                    usernameTxtFld.setEnabled(true);
                    passwordTxtFld.setEnabled(true);
                }
                testConnectionBtn.setEnabled(true);
            }
            checkSetup();
        }
        else {
            database = "sqlite";
        }
        statusLbl.setBounds(10,10,100,20);
        typeLbl.setBounds(10,50,100,20);
        streamUrlLbl.setBounds(175,50,100,20);
        activityLbl.setBounds(10,90,100,20);
        prefixLbl.setBounds(10,150,200,20);
        databaseTypeLbl.setBounds(250,90,100,20);
        serverUrlLbl.setBounds(250,120,100,20);
        portLbl.setBounds(250,170,100,20);
        databaseLbl.setBounds(250,220,100,20);
        usernameLbl.setBounds(400,170,100,20);
        passwordLbl.setBounds(400,220,100,20);

        statusCmbBx.setBounds(60,10,100,20);
        typeCmbBx.setBounds(60,50,100,20);
        databaseTypeCmbx.setBounds(320,90,100,20);

        finishBtn.setBounds(530,280,100,20);
        cancelBtn.setBounds(10,280,100,20);
        browseBtn.setBounds(450,140,20,20);
        testConnectionBtn.setBounds(480,140,150,20);
        createDatabaseBtn.setBounds(550,170,80,20);

        streamUrlTxtFld.setBounds(250,50,200,20);
        activityTxtFld.setBounds(10,110,200,20);
        prefixTxtFld.setBounds(10,170,50,20);
        serverUrlTxtFld.setBounds(250,140,200,20);
        portTxtFld.setBounds(250,190,50,20);
        databaseTxtFld.setBounds(250,240,100,20);
        usernameTxtFld.setBounds(400,190,100,20);
        passwordTxtFld.setBounds(400,240,100,20);

        frame.add(statusLbl);
        frame.add(typeLbl);
        frame.add(streamUrlLbl);
        frame.add(activityLbl);
        frame.add(prefixLbl);
        frame.add(databaseTypeLbl);
        frame.add(serverUrlLbl);
        frame.add(portLbl);
        frame.add(databaseLbl);
        frame.add(usernameLbl);
        frame.add(passwordLbl);

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

        frame.setSize(660,350);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(panel);
        frame.setVisible(true);

        testConnectionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                databaseFileUrl = serverUrlTxtFld.getText();
                Connection conn = connect();
                if (conn != null) {
                    try {
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT 1");
                        JOptionPane.showMessageDialog(null, "Connection successful!", "Test connection",JOptionPane.INFORMATION_MESSAGE);
                        conectionIsSuccessFul = true;
                        checkBooleans();
                        rs.close();
                        stmt.close();
                        conn.close();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Test connection",JOptionPane.WARNING_MESSAGE);
                        conectionIsSuccessFul = false;
                        checkBooleans();
                    }
                }
            }
            public Connection connect() {
                Connection conn = null;
                try {
                    if(databaseTypeCmbx.getSelectedIndex() == 0) {
                        File file = new File(databaseFileUrl);
                        if(file.exists()) {
                            databaseUrl = "jdbc:sqlite:" + databaseFileUrl;
                            conn = DriverManager.getConnection(databaseUrl);
                        }
                        else {
                            Toolkit.getDefaultToolkit().beep();
                            JOptionPane.showMessageDialog(null, "File not found!", "Test connection",JOptionPane.WARNING_MESSAGE);
                            conectionIsSuccessFul = false;
                            checkBooleans();
                        }
                    }
                    else {
                        serverUrl = serverUrlTxtFld.getText();
                        port = portTxtFld.getText();
                        databaseName = databaseTxtFld.getText();
                        username = usernameTxtFld.getText();
                        password = passwordTxtFld.getText();
                        databaseUrl = "jdbc:" + database + "://" + serverUrl + ":" + port + "/" + databaseName;
                        conn = DriverManager.getConnection(databaseUrl, username, password);
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Test connection",JOptionPane.WARNING_MESSAGE);
                    conectionIsSuccessFul = false;
                    checkBooleans();
                }
                return conn;
            }
        });
        createDatabaseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (database.equals("sqlite")) {
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
                            stmt.execute(sqlCommand);
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
                            } else {
                                int dialogResult = JOptionPane.showConfirmDialog(null, "The file exists. Do you want to overwrite it?", "Create Database", JOptionPane.YES_NO_OPTION);
                                if (dialogResult == JOptionPane.YES_OPTION) {
                                    Files.copy(Path.of(tempDatabaseUrl), path, StandardCopyOption.REPLACE_EXISTING);
                                    JOptionPane.showMessageDialog(null, "Database created.", "Create Database", JOptionPane.INFORMATION_MESSAGE);
                                    serverUrlTxtFld.setText(path.toString());
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
                }
                else {
                    String createDatabaseStr = "CREATE DATABASE IF NOT EXISTS " + databaseName + ";";
                    try {
                        System.out.println("sdad");
                        Connection conn = DriverManager.getConnection("jdbc:" + database + "://" + serverUrl + ":" + port + "/", username, password);
                        if (conn != null) {
                            Statement stmt = conn.createStatement();
                            stmt.execute(createDatabaseStr);
                            stmt.execute("USE " + databaseName);
                            stmt.execute(sqlCommand);
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
                if (serverUrlTxtFld.getText().length() > 0 || !serverUrlTxtFld.getText().equals("")) {
                    testConnectionBtn.setEnabled(true);
                    serverUrl = serverUrlTxtFld.getText();
                } else {
                    testConnectionBtn.setEnabled(false);
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
                }
            }
        });
        databaseTypeCmbx.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (databaseTypeCmbx.getSelectedIndex()) {
                    case 0:
                        browseBtn.setVisible(true);
                        portTxtFld.setEnabled(false);
                        databaseTxtFld.setEnabled(false);
                        usernameTxtFld.setEnabled(false);
                        passwordTxtFld.setEnabled(false);
                        portTxtFld.setText("");
                        database = "sqlite";
                        sqliteSelected = true;
                        conectionIsSuccessFul = false;
                        checkBooleans();
                        break;
                    case 1:
                        browseBtn.setVisible(false);
                        portTxtFld.setEnabled(true);
                        databaseTxtFld.setEnabled(true);
                        usernameTxtFld.setEnabled(true);
                        passwordTxtFld.setEnabled(true);
                        portTxtFld.setText("3306");
                        database = "mysql";
                        sqliteSelected = false;
                        conectionIsSuccessFul = false;
                        checkBooleans();
                        break;
                    case 2:
                        browseBtn.setVisible(false);
                        portTxtFld.setEnabled(true);
                        databaseTxtFld.setEnabled(true);
                        usernameTxtFld.setEnabled(true);
                        passwordTxtFld.setEnabled(true);
                        portTxtFld.setText("3306");
                        database = "mariadb";
                        sqliteSelected = false;
                        conectionIsSuccessFul = false;
                        checkBooleans();
                        break;
                    case 3:
                        browseBtn.setVisible(false);
                        portTxtFld.setEnabled(true);
                        databaseTxtFld.setEnabled(true);
                        usernameTxtFld.setEnabled(true);
                        passwordTxtFld.setEnabled(true);
                        portTxtFld.setText("5432");
                        database = "postgresql";
                        sqliteSelected = false;
                        conectionIsSuccessFul = false;
                        checkBooleans();
                        break;
                    case 4:
                        browseBtn.setVisible(false);
                        portTxtFld.setEnabled(true);
                        usernameTxtFld.setEnabled(true);
                        passwordTxtFld.setEnabled(true);
                        portTxtFld.setText("");
                        sqliteSelected = false;
                        conectionIsSuccessFul = false;
                        checkBooleans();
                        break;
                }
            }
        });
        typeCmbBx.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(typeCmbBx.getSelectedIndex() == 1) {
                    streamUrlLbl.setVisible(true);
                    streamUrlTxtFld.setVisible(true);
                    streamType = true;
                }
                else {
                    streamUrlLbl.setVisible(false);
                    streamUrlTxtFld.setVisible(false);
                    streamType = false;
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
                if(streamUrlTxtFld.getText().length() > 0 || !streamUrlTxtFld.getText().equals("")) {
                    streamTxtFldIsEmpty = false;
                    checkBooleans();
                }
                else {
                    streamTxtFldIsEmpty = true;
                    finishBtn.setEnabled(false);
                }
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
                if(activityTxtFld.getText().length() > 0 || !activityTxtFld.getText().equals("")) {
                    activityTxtFldIsEmpty = false;
                    checkBooleans();
                }
                else {
                    activityTxtFldIsEmpty = true;
                    finishBtn.setEnabled(false);
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
                if(prefixTxtFld.getText().length() > 0 || !prefixTxtFld.getText().equals("")) {
                    prefixTxtFldIsEmpty = false;
                    checkBooleans();
                }
                else {
                    prefixTxtFldIsEmpty = true;
                    finishBtn.setEnabled(false);
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
                if(serverUrlTxtFld.getText().length() > 0 || !serverUrlTxtFld.getText().equals("")) {
                    serverTxtFldIsEmpty = false;
                    checkBooleans();
                }
                else {
                    serverTxtFldIsEmpty = true;
                    finishBtn.setEnabled(false);
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
                if(portTxtFld.getText().length() > 0 || !portTxtFld.getText().equals("")) {
                    portTxtFldIsEmpty = false;
                    port = portTxtFld.getText();
                    checkBooleans();
                }
                else {
                    portTxtFldIsEmpty = true;
                    finishBtn.setEnabled(false);
                }
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
                if(databaseTxtFld.getText().length() > 0 || !databaseTxtFld.getText().equals("")) {
                    databaseNameTxtFldIsEmpty = false;
                    databaseName = databaseTxtFld.getText();
                    checkBooleans();
                }
                else {
                    databaseNameTxtFldIsEmpty = true;
                    finishBtn.setEnabled(false);
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
                if(usernameTxtFld.getText().length() > 0 || !usernameTxtFld.getText().equals("")) {
                    usernameTxtFldIsEmpty = false;
                    username = usernameTxtFld.getText();
                    checkBooleans();
                }
                else {
                    usernameTxtFldIsEmpty = true;
                    finishBtn.setEnabled(false);
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
                password = passwordTxtFld.getText();
            }
        });
        /* SOON
        finishBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DumperOptions dumper = new DumperOptions();
                Config config = new Config();
                map.put("token", token);
                map.put("status", statusCmbBx.getSelectedItem());
                map.put("type", typeCmbBx.getSelectedItem());
                map.put("streamUrl", streamUrlTxtFld.getText());
                map.put("activity", activityTxtFld.getText());
                map.put("prefix", prefixTxtFld.getText());
                map.put("database-type", database);
                map.put("server", serverUrlTxtFld.getText());
                map.put("port", portTxtFld.getText());
                map.put("database", databaseTxtFld.getText());
                map.put("username", usernameTxtFld.getText());
                map.put("password", passwordTxtFld.getText());

                // IF YOU FOUND A MISTAKE: https://github.com/BXn4/BenCMDS/issues
                config.token = token;
                config.status = statusCmbBx.getSelectedItem().toString();
                config.type = typeCmbBx.getSelectedItem().toString();
                config.streamUrl = streamUrlTxtFld.getText();
                config.activity = activityTxtFld.getText();
                config.prefix = prefixTxtFld.getText();
                config.databaseType = database;
                config.server = serverUrlTxtFld.getText();
                config.port = portTxtFld.getText();
                config.database = databaseTxtFld.getText();
                config.username = usernameTxtFld.getText();
                config.password = passwordTxtFld.getText();
                dumper.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                Yaml yaml = new Yaml(dumper);
                try {
                    InputStream inputStream = BenCMDS.class.getResourceAsStream("/config.yaml");
                    FileWriter writer = new FileWriter("config.yaml");
                yaml.dump(map, writer);
                try {
                    writer.close();
                } catch (IOException ex) {
                }
                }
                catch (IOException ex) {
                }
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                System.gc();
            }
        });
         */
        finishBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DumperOptions dumper = new DumperOptions();
                map.put("token", token);
                map.put("status", statusCmbBx.getSelectedItem());
                map.put("type", typeCmbBx.getSelectedItem());
                map.put("streamUrl", streamUrlTxtFld.getText());
                map.put("activity", activityTxtFld.getText());
                map.put("prefix", prefixTxtFld.getText());
                map.put("database-type", database);
                map.put("server", serverUrlTxtFld.getText());
                map.put("port", portTxtFld.getText());
                map.put("database", databaseTxtFld.getText());
                map.put("username", usernameTxtFld.getText());
                map.put("password", passwordTxtFld.getText());
                dumper.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                Yaml yaml = new Yaml(dumper);
                try {
                    InputStream inputStream = BenCMDS.class.getResourceAsStream("/config.yaml");
                    FileWriter writer = new FileWriter("config.yaml");
                    yaml.dump(map, writer);
                    try {
                        writer.close();
                    } catch (IOException ex) {
                    }
                }
                catch (IOException ex) {
                }
                BenCMDS main = new BenCMDS(GUI.this);
                main.LoadConfig();
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                System.gc();
            }
        });
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                System.gc();
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
    public void MakeConfigGui() {
        JFrame frame = new JFrame("Config");
        JPanel panel = new JPanel();
        JButton commandsBtn = new JButton("Commands");
        JButton cancelBtn = new JButton("Cancel");
        commandsBtn.setBounds(10,10,100,20);
        cancelBtn.setBounds(10,230,100,20);
        frame.add(commandsBtn);
        frame.add(cancelBtn);
        frame.setSize(400,300);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(panel);
        frame.setVisible(true);
        commandsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MakeCommandsGui();
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
    private void MakeCommandsGui(){
        JFrame frame = new JFrame("Commands");
        JPanel panel = new JPanel();
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(10,230,100,20);
        frame.add(cancelBtn);
        frame.setSize(400,300);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(panel);
        frame.setVisible(true);
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                System.gc();
            }
        });
    }
    private void checkBooleans() {
        if(streamType) {
            if(sqliteSelected) {
                if(!streamTxtFldIsEmpty && !activityTxtFldIsEmpty && !prefixTxtFldIsEmpty && !serverTxtFldIsEmpty && conectionIsSuccessFul) {
                    finishBtn.setEnabled(true);
                }
                else {
                    finishBtn.setEnabled(false);
                }
            }
            else {
                if(!streamTxtFldIsEmpty && !activityTxtFldIsEmpty && !prefixTxtFldIsEmpty && !serverTxtFldIsEmpty && !portTxtFldIsEmpty && !databaseNameTxtFldIsEmpty && !usernameTxtFldIsEmpty && !passwordTxtFldIsEmpty && conectionIsSuccessFul) {
                    finishBtn.setEnabled(true);
                }
                else {
                    finishBtn.setEnabled(false);
                }
            }
        }
        else {
            if(sqliteSelected) {
                if(!activityTxtFldIsEmpty && !prefixTxtFldIsEmpty && !serverTxtFldIsEmpty && conectionIsSuccessFul) {
                    finishBtn.setEnabled(true);
                }
                else {
                    finishBtn.setEnabled(false);
                }
            }
            else {
                if (!activityTxtFldIsEmpty && !prefixTxtFldIsEmpty && !serverTxtFldIsEmpty && !portTxtFldIsEmpty && !databaseNameTxtFldIsEmpty && !usernameTxtFldIsEmpty && !passwordTxtFldIsEmpty && conectionIsSuccessFul) {
                    finishBtn.setEnabled(true);
                } else {
                    finishBtn.setEnabled(false);
                }
            }
        }
    }

    private void checkSetup() {
        File file = new File("config.yaml");
        if (file.exists()) {
            if (!activity.equals("")) {
                activityTxtFldIsEmpty = false;
            }
            if (!streamUrl.equals("")) {
                streamTxtFldIsEmpty = false;
            }
            if (!prefix.equals("")) {
                prefixTxtFldIsEmpty = false;
            }
            if (!databaseUrl.equals("")) {
                serverTxtFldIsEmpty = false;
            }
            if (!port.equals("")) {
                portTxtFldIsEmpty = false;
            }
            if (!databaseName.equals("")) {
                databaseNameTxtFldIsEmpty = false;
            }
            if (!username.equals("")) {
                usernameTxtFldIsEmpty = false;
            }
            if (!password.equals("")) {
                passwordTxtFldIsEmpty = false;
            }
            checkBooleans();
        }
    }
    public void serverCount(int servers) {
        serversLbl.setText("Servers: " + servers);
    }

    public void appendLog(String text) {
        logArea.append(text);
    }
}
