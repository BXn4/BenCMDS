package bxn4.bencmds.commands.games;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class EightBall {
    static String[] magicBallAnswers = new String[] {"It is certain", " It is decidedly so", "Without a doubt", "Yes definitely", "You may rely on it", " As I see it, yes", "Most likely", "Outlook good", "Yes", "Signs point to yes", " Reply hazy, try again", "Ask again later", "Better not tell you now", "Cannot predict now", "Concentrate and ask again", "Don't count on it", "My reply is no", "My sources say no", "Outlook not so good", "Very doubtful"};
    public static void magicBall(SlashCommandInteractionEvent event) {
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
