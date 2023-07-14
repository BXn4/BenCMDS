package bxn4.bencmds.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Wikipedia extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        EmbedBuilder eb = new EmbedBuilder();
        switch (command) {
            case "wiki":
                String query = event.getOption("query").getAsString();
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
                    for (int i = 0; i < titles.length(); i++) {
                        eb.setTitle("Query: " + query);
                        eb.addField(titles.getString(i), "🔗 [" + titles.getString(i) + "]" + "(" +  urls.getString(i) + ")", false);
                        eb.setColor(new Color(98, 177, 246));
                    }
                    event.replyEmbeds(eb.build()).setEphemeral(true).queue();
                } catch (MalformedURLException e) {
                } catch (IOException e) {
                }
                break;
        }
    }
}
