package bxn4.bencmds.commands.weather;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.jetbrains.annotations.NotNull;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Weather {
    private static String[] tempUnits = new String[] {"Celsius", "Fahrenheit"};
    private static String[] speedUnits = new String[] {"Km/h", "m/s", "Mph", "Knots"};
    private static String[] precipitationUnits = new String[] {"mm", "Inch"};
    private static Map<Integer, String> wmo = new HashMap<Integer, String>();

    public static void weatherAutoComplete(CommandAutoCompleteInteractionEvent event) {
        switch (event.getFocusedOption().getName()) {
            case "temperature_unit":
                List<Command.Choice> optionsTemp = Stream.of(tempUnits).filter(word -> word.startsWith(event.getFocusedOption().getValue()))
                        .map(word -> new Command.Choice(word, word)).collect(Collectors.toList());
                event.replyChoices(optionsTemp).queue();
                break;
            case "speed_unit":
                List<Command.Choice> optionsSpeed = Stream.of(speedUnits).filter(word -> word.startsWith(event.getFocusedOption().getValue()))
                        .map(word -> new Command.Choice(word, word)).collect(Collectors.toList());
                event.replyChoices(optionsSpeed).queue();
                break;
            case "precipitation_unit":
                List<Command.Choice> optionsPrecipitation = Stream.of(precipitationUnits).filter(word -> word.startsWith(event.getFocusedOption().getValue()))
                        .map(word -> new Command.Choice(word, word)).collect(Collectors.toList());
                event.replyChoices(optionsPrecipitation).queue();
                break;
        }
    }

    public static void weather(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("weather")) {
            EmbedBuilder eb = new EmbedBuilder();
            event.deferReply().setEphemeral(true).queue();
            String tempUnit = null;
            String speedUnit = null;
            String precipitationUnit = null;
            String place = event.getOption("place").getAsString();
            try {
                tempUnit = event.getOption("temperature_unit").getAsString();
            } catch (NullPointerException e) {
            }
            try {
                speedUnit = event.getOption("speed_unit").getAsString();
            } catch (NullPointerException e) {
            }
            try {
                precipitationUnit = event.getOption("precipitation_unit").getAsString();
            } catch (NullPointerException e) {
            }
            if (!Arrays.asList(tempUnits).contains(tempUnit)) {
                tempUnit = "celsius";
            }
            if (!Arrays.asList(speedUnits).contains(speedUnit)) {
                speedUnit = "kmh";
            }
            if (!Arrays.asList(precipitationUnits).contains(precipitationUnit)) {
                precipitationUnit = "mm";
            }
            tempUnit = tempUnit.toLowerCase();
            speedUnit = speedUnit.toLowerCase();
            precipitationUnit = precipitationUnit.toLowerCase();
            place = place.replace(" ", "+");
            try {
                URL geocodingUrl = new URL("https://geocoding-api.open-meteo.com/v1/search?name=" + place + "&count=1&language=en&format=json");
                HttpURLConnection connection = (HttpURLConnection) geocodingUrl.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                JSONObject jsonObject = new JSONObject(response.toString());
                JSONArray results = jsonObject.getJSONArray("results");
                if (results.length() > 0) {
                    JSONObject result = results.getJSONObject(0);
                    String name = result.getString("name");
                    Double latitude = result.getDouble("latitude");
                    Double longitude = result.getDouble("longitude");
                    String countryCode = result.getString("country_code").toLowerCase();
                    getWeather(tempUnit, speedUnit, precipitationUnit, name, latitude, longitude, countryCode, event);
                } else {
                }
            } catch (JSONException e) {
                eb.setTitle("Not found");
                eb.setColor(Color.RED);
                event.getHook().sendMessageEmbeds(eb.build()).setEphemeral(true).queue();
            } catch (MalformedURLException e) {
                eb.setTitle("Not found");
                eb.setColor(Color.RED);
                event.getHook().sendMessageEmbeds(eb.build()).setEphemeral(true).queue();
            } catch (IOException e) {
                eb.setTitle("Not found");
                eb.setColor(Color.RED);
                event.getHook().sendMessageEmbeds(eb.build()).setEphemeral(true).queue();
            }
        }
    }
    private static void getWeather(String tempUnit, String speedUnit, String precipitationUnit, String name, Double latitude, Double longitude, String countryCode, SlashCommandInteractionEvent event) {
        if(wmo.isEmpty() == true) {
            setWMO();
        }
        EmbedBuilder eb = new EmbedBuilder();
        switch (speedUnit) {
            case "km/h":
                speedUnit = "kmh";
                break;
            case "m/s":
                speedUnit = "ms";
                break;
            case "knots":
                speedUnit = "kn";
                break;
        }
        try {
            URL weatherUrl = new URL("https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                    "&longitude=" + longitude + "&hourly=temperature_2m,relativehumidity_2m,apparent_temperature," +
                    "precipitation_probability,precipitation,weathercode,windspeed_10m,uv_index&daily=temperature_2m_max," +
                    "temperature_2m_min,sunrise,sunset&current_weather=true&temperature_unit=" + tempUnit + "&windspeed_unit=" + speedUnit + "&precipitation_unit=" + precipitationUnit + "&timezone=auto");
            HttpURLConnection connection = (HttpURLConnection) weatherUrl.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            String json = response.toString();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                WeatherData weatherData = objectMapper.readValue(json, WeatherData.class);
                Double temperature = weatherData.current_weather.temperature;
                tempUnit = weatherData.hourly_units.temperature_2m;
                speedUnit = weatherData.hourly_units.windspeed_10m;
                Double windSpeed = weatherData.current_weather.windspeed;
                Integer weatherCode = weatherData.current_weather.weathercode;
                String weather = wmo.get(weatherCode);
                String time = weatherData.current_weather.time;
                String[] times = weatherData.hourly.time;
                Integer i = Arrays.asList(times).indexOf(time);
                Double feelslike = weatherData.hourly.apparent_temperature[i];
                Double minTemp = weatherData.daily.temperature_2m_min[0];
                Double maxTemp = weatherData.daily.temperature_2m_max[0];
                Double humidity = weatherData.hourly.relativehumidity_2m[i];
                Double precipitationProbability = weatherData.hourly.precipitation_probability[i];
                Integer uvIndex = weatherData.hourly.uv_index[i];
                String sunrise = weatherData.daily.sunrise[0];
                String sunset = weatherData.daily.sunset[0];
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                LocalTime sunriseTime = LocalTime.parse(sunrise, dateTimeFormatter);
                LocalTime sunsetTime = LocalTime.parse(sunset, dateTimeFormatter);
                // Icon from https://icons8.com
                String dayNightEmoji = "<:sun:1130253394808934521>";
                if(weatherData.current_weather.is_day == 0) {
                    // Icon from https://icons8.com
                    dayNightEmoji = "<:night:1130253392720183378>";
                }
                sunrise = sunriseTime.toString();
                sunset = sunsetTime.toString();
                eb.setTitle("Current weather in " + ":flag_" + countryCode + ": " + name);
                eb.addField("Weather:", weather, false);
                if(temperature > 15) {
                    eb.setColor(Color.ORANGE);
                    // Icon from https://icons8.com
                    eb.addField("<:temp_hot:1130149205026017280> Temperature:", temperature + tempUnit, true);
                }
                else {
                    eb.setColor(new Color(13, 114, 191));
                    // Icon from https://icons8.com
                    eb.addField("<:temp_cold:1130149202794643566> Temperature:", temperature + tempUnit, true);
                }
                eb.addField("Feels like:", feelslike.toString() + tempUnit, true);
                eb.addField("Min/Max", + minTemp + "/" + maxTemp + tempUnit, true);
                // Icon from: https://www.freepik.com/icon/windsock_7137989#position=32&page=5&fromView=keyword by: Iconic Panda
                eb.addField("<:wind:1130256021546356876> Wind speed:", windSpeed + speedUnit,true);
                eb.addField("UV index:", uvIndex.toString(), true);
                eb.addField("Humidity:", humidity + "%", true);
                eb.addField(":droplet: Prec. prob.:", + precipitationProbability + "%", true);
                if(precipitationProbability != 0) {
                    Double precipitation = weatherData.hourly.precipitation[i];
                    eb.addField("Precipitation:", + precipitation + precipitationUnit,  true);
                }
                eb.addField(dayNightEmoji + "Sunrise - Sunset:", sunrise + "-" + sunset, false);
                eb.addField("🔗 Source: ", "[Open-Meteo]" + "(https://open-meteo.com)", false);
                event.getHook().sendMessageEmbeds(eb.build()).setEphemeral(true).queue();
                System.gc();
            }
            catch (Exception e) {
            }
        }
        catch (MalformedURLException e) {
        } catch (IOException e) {
        }
    }
    private static void setWMO() {
        wmo.put(0, "Clear sky");
        wmo.put(1, "Mainly clear");
        wmo.put(2, "Partly cloudy");
        wmo.put(3, "Overcast");
        wmo.put(45, "Fog and depositing rime fog");
        wmo.put(48, "Fog and depositing rime fog");
        wmo.put(51, "Drizzle: Light intensity");
        wmo.put(53, "Drizzle: Moderate intensity");
        wmo.put(55, "Drizzle: Dense intensity");
        wmo.put(56, "Freezing Drizzle: Light intensity");
        wmo.put(57, "Freezing Drizzle: Dense intensity");
        wmo.put(61, "Rain: Slight intensity");
        wmo.put(63, "Rain: Moderate intensity");
        wmo.put(65, "Rain: Heavy intensity");
        wmo.put(66, "Freezing Rain: Light intensity");
        wmo.put(67, "Freezing Rain: Heavy intensity");
        wmo.put(71, "Snowfall: Slight intensity");
        wmo.put(73, "Snowfall: Moderate intensity");
        wmo.put(75, "Snowfall: Heavy intensity");
        wmo.put(77, "Snow grains");
        wmo.put(80, "Rain showers: Slight intensity");
        wmo.put(81, "Rain showers: Moderate intensity");
        wmo.put(82, "Rain showers: Violent intensity");
        wmo.put(85, "Snow showers: Slight intensity");
        wmo.put(86, "Snow showers: Heavy intensity");
        wmo.put(95, "Thunderstorm: Slight or moderate");
        wmo.put(96, "Thunderstorm with slight hail");
        wmo.put(99, "Thunderstorm with heavy hail");
    }
}