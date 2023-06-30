package bxn4.bencmds;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class GUI {
    public JLabel serversLbl = new JLabel("");
    public JTextArea logArea = new JTextArea("Nothing to do...");
    private String token = null;
    private Map<String, Object> map = new LinkedHashMap<>();
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
    Connection conn = null;
    String databaseFileUrl = null;
    String databaseUrl = null;
    String database = null;
    JButton finishBtn = new JButton("Finish");
    public void MakeGui() {
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
    }

    public void MakeSetUpGUI() {
        JFrame frame = new JFrame("Set-up");
        JPanel panel = new JPanel();
        JTextField tokenTextField = new JTextField("");
        JButton nextBtn = new JButton("Next");
        nextBtn.setEnabled(false);
        JButton cancelBtm = new JButton("Cancel");
        JButton getTokenBtn = new JButton("Get Token");
        tokenTextField.setBounds(40,10,300,25);
        nextBtn.setBounds(270,80,100,20);
        cancelBtm.setBounds(10,80,100,20);
        getTokenBtn.setBounds(270,50,100,20);
        frame.add(tokenTextField);
        frame.add(nextBtn);
        frame.add(cancelBtm);
        frame.add(getTokenBtn);
        frame.setSize(400,150);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(panel);
        frame.setVisible(true);
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
        cancelBtm.addActionListener(new ActionListener() {
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
        String[] ports = { "", "3306", "5432", "1433", "" };
        JFrame frame = new JFrame("Set-up");
        JPanel panel = new JPanel();
        JLabel statusLbl = new JLabel("Status:");
        JLabel typeLbl = new JLabel("Type:");
        JLabel streamUrlLbl = new JLabel("Stream Url:");
        JLabel activityLbl = new JLabel("Activity:");
        JLabel prefixLbl = new JLabel("Prefix (for custom commands):");
        JLabel databaseTypeLbl = new JLabel("Database type:");
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

        JButton cancelBtm = new JButton("Cancel");
        JButton browseBtn = new JButton("...");
        JButton testConnectionBtn = new JButton("Test connection");

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
        testConnectionBtn.setEnabled(false);
        databaseTxtFld.setEnabled(false);

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
        databaseTypeCmbx.setBounds(350,90,100,20);

        finishBtn.setBounds(530,280,100,20);
        cancelBtm.setBounds(10,280,100,20);
        browseBtn.setBounds(450,140,20,20);
        testConnectionBtn.setBounds(480,140,150,20);

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
        frame.add(cancelBtm);
        frame.add(browseBtn);
        frame.add(testConnectionBtn);

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
                /*System.out.println("streamTxtFldIsEmpty: " + streamTxtFldIsEmpty);
                System.out.println("activityTxtFldIsEmpty: " + activityTxtFldIsEmpty);
                System.out.println("prefixTxtFldIsEmpty: " + prefixTxtFldIsEmpty);
                System.out.println("serverTxtFldIsEmpty: " + serverTxtFldIsEmpty);
                System.out.println("portTxtFldIsEmpty: " + portTxtFldIsEmpty);
                System.out.println("databaseNameTxtFldIsEmpty: " + databaseNameTxtFldIsEmpty);
                System.out.println("usernameTxtFldIsEmpty: " + usernameTxtFldIsEmpty);
                System.out.println("passwordTxtFldIsEmpty: " + passwordTxtFldIsEmpty);
                System.out.println("sqliteSelected: " + sqliteSelected);
                System.out.println("conectionIsSuccessFul: " + conectionIsSuccessFul);
                System.out.println("streamType: " + streamType); */
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
                        JOptionPane.showMessageDialog(null, "Error occurred during connection test: " + ex.getMessage(), "Test connection",JOptionPane.WARNING_MESSAGE);
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
                            JOptionPane.showMessageDialog(null, "Error occurred during connection test: File not found!", "Test connection",JOptionPane.WARNING_MESSAGE);
                            conectionIsSuccessFul = false;
                            checkBooleans();
                        }
                    }
                    else {
                        String serverUrl = serverUrlTxtFld.getText();
                        String port = portTxtFld.getText();
                        String databaseName = databaseTxtFld.getText();
                        String username = usernameTxtFld.getText();
                        String password = passwordTxtFld.getText();
                        String databaseUrl = "jdbc:" + database + "://" + serverUrl + ":" + port + "/" + databaseName;
                        conn = DriverManager.getConnection(databaseUrl, username, password);
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error occurred during connection test: " + e.getMessage(), "Test connection",JOptionPane.WARNING_MESSAGE);
                    conectionIsSuccessFul = false;
                    checkBooleans();
                }
                return conn;
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
                        portTxtFld.setText(ports[1]);
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
                        portTxtFld.setText(ports[2]);
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
                        portTxtFld.setText(ports[3]);
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
                    checkBooleans();
                }
                else {
                    usernameTxtFldIsEmpty = true;
                    finishBtn.setEnabled(false);
                }
            }
        });
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
                File configFile = new File(getClass().getClassLoader().getResource("src/config.yaml").getFile());
                FileWriter writer = null;
                try {
                    writer = new FileWriter(configFile);
                } catch (IOException ex) {
                }
                yaml.dump(map, writer);
                try {
                    writer.close();
                } catch (IOException ex) {
                }
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                System.gc();
            }
        });
        cancelBtm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                System.gc();
            }
        });
    }
    public void checkBooleans() {
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
    public void serverCount(int servers) {
        serversLbl.setText("Servers: " + servers);
    }

    public void appendLog(String text) {
        logArea.append(text);
    }
}
