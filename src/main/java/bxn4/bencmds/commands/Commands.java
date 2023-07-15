package bxn4.bencmds.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Commands extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        EmbedBuilder eb = new EmbedBuilder();
        switch (command) {
            case "ping":
                event.reply("**Pong!**").setEphemeral(true).queue();
        }
    }
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<CommandData> commandDataList = new ArrayList<>();
        commandDataList.add(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("ping", "Test the bot response time."));
        commandDataList.add(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("wiki", "Search on Wikipedia").addOption(OptionType.STRING, "query", "The search query to get articles with the same name").addOption(OptionType.STRING, "article", "Get the article"));
        event.getJDA().updateCommands().addCommands(commandDataList).queue();
    }
}