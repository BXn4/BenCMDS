package bxn4.bencmds;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class Config {
    private static Config instance;
    public String botToken;
    public String botStatus;
    public String botActivityType;
    public String botStreamUrl;
    public String botActivity;
    public String botPrefix;
    public String databaseType;
    public String databaseServer;
    public String databasePort;
    public String databaseName;
    public String databaseUsername;
    public String databasePassword;
    public String applicationSkin;

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public void loadConfig() {
        Yaml yaml = new Yaml();
        FileReader reader = null;
        try {
            reader = new FileReader("config.yaml");
            Map<String, Object> data = yaml.load(reader);
            try {
                reader.close();
            } catch (IOException e) {
            }

            try {
                botToken = data.get("botToken").toString();
            } catch (NullPointerException e) {
            }

            try {
                botStatus = data.get("botStatus").toString();
            } catch (NullPointerException e) {
            }

            try {
                botActivityType = data.get("botActivityType").toString();
            } catch (NullPointerException e) {
            }

            try {
                botStreamUrl = data.get("botStreamUrl").toString();
            } catch (NullPointerException e) {
            }

            try {
                botActivity = data.get("botActivity").toString();
            } catch (NullPointerException e) {
            }

            try {
                botPrefix = data.get("botPrefix").toString();
            } catch (NullPointerException e) {
            }

            try {
                databaseType = data.get("databaseType").toString();
            } catch (NullPointerException e) {
            }

            try {
                databaseServer = data.get("databaseServer").toString();
            } catch (NullPointerException e) {
            }

            try {
                databasePort = data.get("databasePort").toString();
            } catch (NullPointerException e) {
            }

            try {
                databaseName = data.get("databaseName").toString();
            } catch (NullPointerException e) {
            }

            try {
                databaseUsername = data.get("databaseUsername").toString();
            } catch (NullPointerException e) {
            }

            try {
                databasePassword = data.get("databasePassword").toString();
            } catch (NullPointerException e) {
            }

            try {
                applicationSkin = data.get("applicationSkin").toString();
            } catch (NullPointerException e) {
            }

        } catch (FileNotFoundException e) {
            System.out.println("[WARNING] Config file not found! Creating new config file when changing settings.");
        }
    }

    public void saveConfig() {
        Map<String, Object> data = null;
        Map<String, Object> map = new LinkedHashMap<>();
        DumperOptions dumper = new DumperOptions();
        map.put("botToken", botToken);
        map.put("botStatus", botStatus);
        map.put("botActivityType", botActivityType);
        map.put("botStreamUrl", botStreamUrl);
        map.put("botActivity", botActivity);
        map.put("botPrefix", botPrefix);
        map.put("databaseType", databaseType);
        map.put("databaseServer", databaseServer);
        map.put("databasePort", databasePort);
        map.put("databaseName", databaseName);
        map.put("databaseUsername", databaseUsername);
        map.put("databasePassword", databasePassword);
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
        } catch (IOException ex) {
        }
        System.gc();
    }
}
