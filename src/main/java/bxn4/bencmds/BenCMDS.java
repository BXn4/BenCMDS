package bxn4.bencmds;

import bxn4.bencmds.GUI.MainGUI;
import bxn4.bencmds.commands.Commands;
import bxn4.bencmds.commands.EventListener;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BenCMDS extends ListenerAdapter {
    public MainGUI mainGUI = new MainGUI();
    private ShardManager shardManager;
    static String token;
    static String status;
    static String type;
    static String activity;
    static String streamUrl;
    static String skin;
    public BenCMDS(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
        shardManager = null;
    }

    public static void main(String[] args) {
        MainGUI mainGUI = new MainGUI();
        BenCMDS benCMDS = new BenCMDS(mainGUI);
        Config config = Config.getInstance();
        config.loadConfig();
        skin = config.applicationSkin;
        if(skin == null) {
            skin = "Graphite";
        }
        mainGUI.makeGUI(skin);
        try {
            Class.forName("org.sqlite.JDBC");
            Class.forName("com.mysql.cj.jdbc.Driver");
            Class.forName("org.mariadb.jdbc.Driver");
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
        }
    }

    private void set() {
        Config config = Config.getInstance();
        token = config.botToken;
        activity = config.botActivity;
        status = config.botStatus;
        type = config.botActivityType;
        streamUrl = config.botStreamUrl;
        if(activity == null) {
            activity = "Be cool!";
            System.out.println("[WARNING] The botActivity in config.yaml is empty. Using the default value.");
        }
        if(status == null) {
            status = "online";
            System.out.println("[WARNING] The botStatus in config.yaml is empty. Using the default value.");
        }
        if(type == null) {
            type = "playing";
            System.out.println("[WARNING] The botActivityType in config.yaml is empty. Using the default value.");
        }
        if(streamUrl == null && type.equals("streaming")) {
            streamUrl = "https://twitch.tv/discord";
            System.out.println("[WARNING] The botStreamUrl in config.yaml is empty. Using the default value.");
        }
    }

    public void startBot() {
        set();
        System.out.println("[INFO] Starting");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        mainGUI.appendLog("\n[" + dtf.format((now)) + "]" + " Starting");
        if(token != null) {
            if (token.length() != 0) {
                DefaultShardManagerBuilder bot = DefaultShardManagerBuilder.createDefault(token);
                switch (status) {
                    case "online" -> bot.setStatus(OnlineStatus.ONLINE);
                    case "idle" -> bot.setStatus(OnlineStatus.IDLE);
                    case "dnd" -> bot.setStatus(OnlineStatus.DO_NOT_DISTURB);
                    case "invisible" -> bot.setStatus(OnlineStatus.INVISIBLE);
                    default -> bot.setStatus(OnlineStatus.ONLINE);
                }
                switch (type) {
                    case "listening" -> bot.setActivity(Activity.listening(activity));
                    case "streaming" -> bot.setActivity(Activity.streaming(activity, streamUrl));
                    case "playing" -> bot.setActivity(Activity.playing(activity));
                    case "competing" -> bot.setActivity(Activity.competing(activity));
                    case "watching" -> bot.setActivity(Activity.watching(activity));
                    default -> bot.setActivity(Activity.playing(activity));
                }
                try {
                    shardManager = bot.build();
                }
                catch (InvalidTokenException e) {
                    Toolkit.getDefaultToolkit().beep();
                    System.out.println("[ERROR] INVALID TOKEN!");
                    dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
                    now = LocalDateTime.now();
                    mainGUI.appendLog("\n[" + dtf.format((now)) + "]" + " INVALID TOKEN!");
                }
                shardManager.addEventListener(this, new EventListener(), new Commands());
            }
        }
        else {
            System.out.println("[ERROR] Cannot start bot, because the TOKEN IS EMPTY!");
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        Database database = new Database();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        mainGUI.appendLog("\n[" + dtf.format((now)) + "]" + " Connecting to database...");
        database.connectToDatbase();
        Boolean connection = database.ConnectionIsSuccessful;
        if(connection) {
            mainGUI.appendLog("\n[" + dtf.format((now)) + "]" + " Connected to database!");
            int servers = event.getGuildTotalCount();
            mainGUI.appendLog("\n[" + dtf.format((now)) + "]" + " Listening on: " + servers + " servers");
            mainGUI.serverCount(servers);
        }
        else {
            mainGUI.appendLog("\n[" + dtf.format((now)) + "]" + " Cannot connect to database");
            stopBot();
        }
    }

    public void stopBot() {
        shardManager.shutdown();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        mainGUI.appendLog("\n[" + dtf.format((now)) + "]" + " Stopped");
    }
}