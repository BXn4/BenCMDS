package bxn4.bencmds.commands.reminder;

import bxn4.bencmds.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Reminder {
    public Map<Integer, Long> reminders = new HashMap<Integer, Long>();
    public Map<Integer, String> remindersData = new HashMap<Integer, String>();
    public static Integer reminderId;
    static Boolean haveReminder = false;

    public static void reminder(@NotNull SlashCommandInteractionEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        Long currentTimestamp = System.currentTimeMillis() / 1000;
        Long timestamp = event.getOption("time").getAsLong();
        String user = event.getUser().getId();
        String about = event.getOption("about").getAsString().replaceAll("[-'`;&-/*\"\\\\-]*", "");
        if(about.length() > 120) {
            about = about.substring(0,120);
        }
        if(timestamp < currentTimestamp) {
            eb.setTitle("Wrong timestamp!");
            eb.setColor(Color.RED);
            event.replyEmbeds(eb.build()).setEphemeral(true).queue();
        }
        else {
            Long fiveMinutesLaterTimestamp = currentTimestamp + TimeUnit.MINUTES.toSeconds(5);
            //System.out.println(currentTimestamp);
            //System.out.println(timestamp);
            //System.out.println(fiveMinutesLaterTimestamp);
            if (timestamp < fiveMinutesLaterTimestamp) {
                eb.setTitle("Please set a reminder what is not in 5 minutes");
                eb.setColor(Color.RED);
                event.replyEmbeds(eb.build()).setEphemeral(true).queue();
            }
            else {
                Database database = new Database();
                Double userId = Double.parseDouble(event.getUser().getId());
                // TODO
                database.runSQLCommand(
                        "reminders",
                        "SELECT reminderId\n" +
                                "FROM reminders\n" +
                                "ORDER BY reminderId DESC\n" +
                                "LIMIT 1;",
                        userId);
                if (!haveReminder) {
                    reminderId++;
                    database.insertToDatabase(
                            "INSERT INTO `reminders`(`reminderId`, `reminderUserId`, `reminderCreatorUserId`, `reminderTime`, `reminderAbout`) " +
                                    "VALUES ('" + reminderId++ + "','" + userId + "','" + userId + "','" + timestamp + "','" + about + "')");
                    eb.setTitle(":bell: Reminder created!");
                    eb.addField("Reminder in:", "<t:" + timestamp + ":R>", false);
                    eb.appendDescription(about);
                    eb.setColor(Color.ORANGE);
                    event.replyEmbeds(eb.build()).setEphemeral(false).addActionRow(Button.secondary("a", "Remind Me Too"), Button.danger("remove", "Cancel")).queue();
                }
                else {
                    eb.setTitle("You have a reminder!");
                    eb.setColor(Color.RED);
                    event.replyEmbeds(eb.build()).setEphemeral(true).queue();
                }
            }
        }
    }
    public void setReminderId(int reminderId) {
        Reminder.reminderId = reminderId;
    }

    // TODO
    public void checHaveReminder(Boolean have) {
        haveReminder = have.booleanValue();
    }
}