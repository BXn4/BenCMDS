package bxn4.bencmds;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class BenCMDS extends ListenerAdapter {
    private ShardManager shardManager;
    private GUI gui;
    public BenCMDS(GUI gui) {
        this.gui = gui;
    }
    public void StartBot() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        gui.appendLog("\n[" + dtf.format((now)) + "]" + " Starting");
        String token = "";
        String status = "";
        String type = "";
        String activity = "";
        String streamUrl = "";
        Yaml yaml = new Yaml();
        InputStream inputStream = null;
        inputStream = getClass().getClassLoader().getResourceAsStream("src/config.yaml");
        Map<String, Object> data = yaml.load(inputStream);
        try {
            inputStream.close();
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
        shardManager.addEventListener(this);
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
        GUI gui = new GUI();
        BenCMDS bot = new BenCMDS(gui);
        gui.MakeGui();
    }
}
