package bxn4.bencmds.commands;

import bxn4.bencmds.commands.reminder.Reminder;
import bxn4.bencmds.commands.weather.Weather;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Commands extends ListenerAdapter {
    String[] magicBallAnswers = new String[] {"It is certain", " It is decidedly so", "Without a doubt", "Yes definitely", "You may rely on it", " As I see it, yes", "Most likely", "Outlook good", "Yes", "Signs point to yes", " Reply hazy, try again", "Ask again later", "Better not tell you now", "Cannot predict now", "Concentrate and ask again", "Don't count on it", "My reply is no", "My sources say no", "Outlook not so good", "Very doubtful"};

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<CommandData> commandDataList = new ArrayList<>();
        commandDataList.add(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("ping", "Test the bot response time"));
        commandDataList.add(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("8ball", "Ask the magic 8 ball").addOption(OptionType.STRING, "question", "Ask any question and you be sure find your answer", true, false));
        commandDataList.add(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("reminder_set", "Set a reminder").addOption(OptionType.INTEGER, "time", "Set the reminder time in timestamp (https://www.unixtimestamp.com)", true, false).addOption(OptionType.STRING, "about", "What this reminder for?", false,false));
        commandDataList.add(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("wiki", "Search on Wikipedia").addOption(OptionType.STRING, "query", "Search query to get articles with the same name").addOption(OptionType.STRING, "article", "Get the article"));
        commandDataList.add(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("weather", "Get the current weather information").addOption(OptionType.STRING, "place", "City or country", true).addOption(OptionType.STRING, "temperature_unit", "Set the temperature unit", false, true).addOption(OptionType.STRING, "speed_unit", "Set the speed unit", false, true).addOption(OptionType.STRING, "precipitation_unit", "Set the precipitation unit", false, true));
        event.getJDA().updateCommands().addCommands(commandDataList).queue();
    }
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        switch (command) {
            case "ping":
                event.reply("**Pong!**").setEphemeral(true).queue();
                break;
            case "8ball":
                magicBall(event);
                break;
            case "wiki":
                Wikipedia.wiki(event);
                break;
            case "weather":
                Weather.weather(event);
                break;
            case "reminder_set":
                Reminder.reminder(event);
                break;
        }
    }
    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        String command = event.getName();
        switch (command) {
            case "weather":
                Weather.weatherAutoComplete(event);
                break;
        }
    }
    private void magicBall(SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("8ball")) {
            String question = event.getOption("question").getAsString();
            EmbedBuilder eb = new EmbedBuilder();
            EmbedBuilder ebGif = new EmbedBuilder();
            int magicBallRnd = new Random().nextInt(magicBallAnswers.length);
            ebGif.setImage("https://media0.giphy.com/media/3o6ozoD1ByqYv7ARIk/giphy.gif?cid=ecf05e47m1be4rkvrn0t5lu3p06zept5imi46dx5zlxh5c6g&ep=v1_gifs_search&rid=giphy.gif&ct=g");
            ebGif.setColor(new Color(191, 64, 191));
            eb.addField("Question:", question, false);
            eb.addField("Answer is:", magicBallAnswers[magicBallRnd], false);
            eb.setThumbnail("https://github-production-user-asset-6210df.s3.amazonaws.com/78733248/253890643-a503664e-0253-4f98-8037-fa50f18a95ad.png");
            eb.setColor(new Color(191, 64, 191));
            event.deferReply().queue();
            event.getHook().sendMessageEmbeds(ebGif.build()).queue();
            event.getHook().editOriginalEmbeds(eb.build()).queueAfter(2400, TimeUnit.MILLISECONDS);
        }
    }
}