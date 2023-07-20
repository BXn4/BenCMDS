package bxn4.bencmds;

import bxn4.bencmds.GUI.MainGUI;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.awt.*;

public class BenCMDS {
    private MainGUI mainGUI = new MainGUI();
    private ShardManager shardManager;
    static String token;
    static String status;
    static String type;
    static String activity;
    static String streamUrl;
    static String skin;
    public BenCMDS(MainGUI mainGUI) {
        shardManager = null;
    }

    public static void main(String[] args) {
        MainGUI mainGUI = new MainGUI();
        BenCMDS benCMDS = new BenCMDS(mainGUI);
        Config config = Config.getInstance();
        config.loadConfig();
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
        // NEED TO DO
        // mainGUI.appendLog("Starting");
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

                    // NEED TO DO
                    // mainGUI.appendLog("Invalid token!");
                }
            }

        }
        else {
            System.out.println("[ERROR] Cannot start bot, because the TOKEN IS EMPTY!");
        }
    }

    public void stopBot() {

    }
}