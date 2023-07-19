package bxn4.bencmds.commands.reminder;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Reminder {
    public Map<Integer, Long> reminders = new HashMap<Integer, Long>();
    public Map<Integer, String> remindersData = new HashMap<Integer, String>();

    public static void reminder(@NotNull SlashCommandInteractionEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        Long currentTimestamp = System.currentTimeMillis() / 1000;
        Long timestamp = event.getOption("time").getAsLong();
        String user = event.getUser().getId();
        String about = event.getOption("about").getAsString();
        if(timestamp < currentTimestamp) {
            eb.setTitle("Wrong timestamp!");
            eb.setColor(Color.RED);
            event.replyEmbeds(eb.build()).setEphemeral(true).queue();
        }
        else {
            /*Long fiveMinutesLaterTimestamp = currentTimestamp + TimeUnit.MINUTES.toSeconds(300);
            if (timestamp > fiveMinutesLaterTimestamp) {
                eb.setTitle("Please set a reminder what is not in 5 minutes");
                eb.setColor(Color.RED);
                event.replyEmbeds(eb.build()).setEphemeral(true).queue();
            }*/
            eb.setTitle("Reminder created!");
            eb.addField("Reminder in:", "<t:" +timestamp + ":R>", false);
            eb.appendDescription(about);
            eb.setColor(Color.ORANGE);
            event.replyEmbeds(eb.build()).setEphemeral(false).queue();
        }
    }
}