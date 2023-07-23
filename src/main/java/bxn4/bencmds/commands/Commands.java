package bxn4.bencmds.commands;

import bxn4.bencmds.commands.games.Dice;
import bxn4.bencmds.commands.games.EightBall;
import bxn4.bencmds.commands.reminder.Reminder;
import bxn4.bencmds.commands.weather.Weather;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.List;

public class Commands extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<CommandData> commandDataList = new ArrayList<>();
        commandDataList.add(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("ping", "Test the bot response time"));
        commandDataList.add(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("info", "Info about the bot"));
        commandDataList.add(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("8ball", "Ask the magic 8 ball").addOption(OptionType.STRING, "question", "Ask any question and you be sure find your answer", true, false));
        commandDataList.add(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("dice", "Roll the dice"));
        commandDataList.add(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("reminder_set", "Set a reminder").addOption(OptionType.INTEGER, "time", "Set the reminder time in timestamp (https://www.unixtimestamp.com)", true, false).addOption(OptionType.STRING, "about", "What this reminder for?", false,false));
        commandDataList.add(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("wiki", "Search on Wikipedia").addOption(OptionType.STRING, "query", "Search query to get articles with the same name").addOption(OptionType.STRING, "article", "Get the article"));
        commandDataList.add(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("weather", "Get the current weather information").addOption(OptionType.STRING, "place", "City or country", true).addOption(OptionType.STRING, "temperature_unit", "Set the temperature unit", false, true).addOption(OptionType.STRING, "speed_unit", "Set the speed unit", false, true).addOption(OptionType.STRING, "precipitation_unit", "Set the precipitation unit", false, true));
        event.getJDA().updateCommands().addCommands(commandDataList).queue();
    }
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        switch (command) {
            case "ping" -> event.reply("**Pong!**").setEphemeral(true).queue();
            case "8ball" -> EightBall.magicBall(event);
            case "wiki" -> Wikipedia.wiki(event);
            case "weather" -> Weather.weather(event);
            case "reminder_set" -> Reminder.reminder(event);
            case "dice" -> Dice.rollDice(event);
            case "info" -> botInfo(event);
        }
    }
    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        String command = event.getName();
        switch (command) {
            case "weather" -> Weather.weatherAutoComplete(event);
        }
    }

    private void botInfo(@NotNull SlashCommandInteractionEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("BenCMDS");
        eb.setThumbnail("https://cdn.discordapp.com/avatars/958043426237059132/cc0c2e340190d591f61b6574afc5f2f7.png");
        eb.addField("Developed by: ", "BXn4 (Bence) :flag_hu:", true);
        eb.addField("Discord: ", "<@430027629685506048>", true);
        eb.addField("🔗 Source code: ", "[GitHub]" + "(https://github.com/bxn4/bencmds)", false);
        event.replyEmbeds(eb.build()).addActionRow(Button.of(ButtonStyle.LINK, "https://github.com/bxn4/bencmds", "Source Code", Emoji.fromUnicode("🔗"))).queue();
    }

}