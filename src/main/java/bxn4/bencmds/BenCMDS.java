package bxn4.bencmds;

import bxn4.bencmds.commands.Commands;
import bxn4.bencmds.commands.EventListener;
import bxn4.bencmds.commands.Wikipedia;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.io.*;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class BenCMDS extends ListenerAdapter {
    private ShardManager shardManager;
    private GUI gui;
    public BenCMDS(GUI gui) {
        this.gui = gui;
        LoadConfig();
    }
    public void StartBot() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        gui.appendLog("\n[" + dtf.format((now)) + "]" + " Starting");
        String token;
        String status;
        String type;
        String activity;
        String streamUrl = null;
        Yaml yaml = new Yaml();
        FileReader reader = null;
        InputStream inputStream = BenCMDS.class.getResourceAsStream("/config.yaml");
        try {
            reader = new FileReader("config.yaml");
        } catch (FileNotFoundException e) {
        }
        Map<String, Object> data = yaml.load(reader);
        try {
            inputStream.close();
            reader.close();
        } catch (IOException e) {
        }
        token = data.get("token").toString();
        status = data.get("status").toString();
        type = data.get("type").toString();
        activity = data.get("activity").toString();
        if(data.get("streamUrl") != null) {
            streamUrl = data.get("streamUrl").toString();
        }
        DefaultShardManagerBuilder bot = DefaultShardManagerBuilder.createDefault(token);
        switch (status) {
            case "online":
                bot.setStatus(OnlineStatus.ONLINE);
                break;
            case "idle":
                bot.setStatus(OnlineStatus.IDLE);
                break;
            case "dnd":
                bot.setStatus(OnlineStatus.DO_NOT_DISTURB);
                break;
            case "invisible":
                bot.setStatus(OnlineStatus.INVISIBLE);
                break;
        }
        switch (type) {
            case "listening":
                bot.setActivity(Activity.listening(activity));
                break;
            case "streaming":
                bot.setActivity(Activity.streaming(activity, streamUrl));
                break;
            case "playing":
                bot.setActivity(Activity.playing(activity));
                break;
            case "competing":
                bot.setActivity(Activity.competing(activity));
                break;
            case "watching":
                bot.setActivity(Activity.watching(activity));
                break;
        }
        try {
            shardManager = bot.build();
        }
        catch (InvalidTokenException ex) {
            dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            now = LocalDateTime.now();
            gui.appendLog("\n[" + dtf.format(now) + "]" + " Invalid token!");
        }
        shardManager.addEventListener(this, new EventListener(), new Commands(), new Wikipedia());
    }

    public void StopBot() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        gui.appendLog("\n[" + dtf.format(now) + "]" + " Stopped");
        shardManager.shutdown();
    }

    @Override
    public void onReady(ReadyEvent event) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        int servers = event.getGuildTotalCount();
        gui.appendLog("\n[" + dtf.format((now)) + "]" + " Listening on: " + servers + " servers");
        gui.serverCount(servers);
    }

    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            Class.forName("com.mysql.cj.jdbc.Driver");
            Class.forName("org.mariadb.jdbc.Driver");
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex, "Test connection",JOptionPane.INFORMATION_MESSAGE);
        }
        GUI gui = new GUI();
        BenCMDS bot = new BenCMDS(gui);
        gui.MakeGui();
    }

    private void LoadConfig() {
        Yaml yaml = new Yaml();
        FileReader reader = null;
        File file = new File("config.yaml");
        if (file.exists()) {
            InputStream inputStream = BenCMDS.class.getResourceAsStream("/config.yaml");
            try {
                reader = new FileReader("config.yaml");
            } catch (FileNotFoundException e) {
            }
            gui.data = yaml.load(reader);
            try {
                inputStream.close();
                reader.close();
            } catch (IOException e) {
            }
            try {
                gui.token = gui.data.get("token").toString();
                gui.status = gui.data.get("status").toString();
                gui.type = gui.data.get("type").toString();
                gui.streamUrl = gui.data.get("streamUrl").toString();
                gui.activity = gui.data.get("activity").toString();
                gui.prefix = gui.data.get("prefix").toString();
                gui.database = gui.data.get("database-type").toString();
                gui.databaseUrl = gui.data.get("server").toString();
                gui.port = gui.data.get("port").toString();
                gui.databaseName = gui.data.get("database").toString();
                gui.username = gui.data.get("username").toString();
                gui.password = gui.data.get("password").toString();
            } catch (NullPointerException e) {
            }
            System.gc();
        }
    }
}