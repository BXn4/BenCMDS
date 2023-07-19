package bxn4.bencmds.commands;

import Jwiki.Jwiki;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Wikipedia {
    public static void wiki(@NotNull SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();
        String query = null;
        try {
            query = event.getOption("query").getAsString();
        } catch (NullPointerException e) {
        }
        if (query != null && !query.isEmpty()) {
            EmbedBuilder eb = new EmbedBuilder();
            try {
                URL wikiUrl = new URL("https://wikipedia.org/w/api.php?action=opensearch&search=" + query);
                HttpURLConnection connection = (HttpURLConnection) wikiUrl.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                JSONArray data = new JSONArray(response.toString());
                JSONArray titles = data.getJSONArray(1);
                JSONArray urls = data.getJSONArray(3);
                eb.setTitle("Query: " + query);
                eb.setColor(new Color(98, 177, 246));
                if (titles.length() > 0) {
                    for (int i = 0; i < titles.length(); i++) {
                        eb.addField(titles.getString(i), "🔗 [" + titles.getString(i) + "]" + "(" + urls.getString(i) + ")", false);
                    }
                    eb.setFooter("Articles found: " + titles.length());
                } else {
                    eb.setFooter("Articles found: 0");
                }
                event.getHook().sendMessageEmbeds(eb.build()).setEphemeral(true).queue();
                response = null;
                data = null;
                titles = null;
                urls = null;
                System.gc();
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            }
        } else {
            EmbedBuilder eb = new EmbedBuilder();
            try {
                String article = event.getOption("article").getAsString();
                Jwiki jwiki = new Jwiki(article);

                // NEED TITLE FORMATTING
                String titleRaw = jwiki.getDisplayTitle();
                String title = titleRaw.replace("<span class=\"mw-page-title-main\">", "").replace("</span>", "");
                String url = title.replace(" ", "_");
                eb.addField(title, jwiki.getExtractText(), false);
                eb.addField("🔗 Source: ", "[Wikipedia]" + "(https://wikipedia.org/wiki/" + url + ")", false);
                eb.setThumbnail(jwiki.getImageURL());
                event.getHook().sendMessageEmbeds(eb.build()).setEphemeral(true).queue();
            } catch (NullPointerException e) {
                eb.setTitle("Not found");
                eb.setColor(Color.RED);
                event.getHook().sendMessageEmbeds(eb.build()).setEphemeral(true).queue();
            }
        }
    }
}