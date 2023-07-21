package bxn4.bencmds.commands.games;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Random;

public class Dice {
    public static void rollDice(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("dice")) {
            event.deferReply().queue();
            EmbedBuilder eb = new EmbedBuilder();
            Random rnd = new Random();
            eb.setColor(Color.GREEN);
            eb.setTitle(":game_die: You rolled: " + rnd.nextInt(1,7));
            event.getHook().sendMessageEmbeds(eb.build()).queue();
        }
    }
}