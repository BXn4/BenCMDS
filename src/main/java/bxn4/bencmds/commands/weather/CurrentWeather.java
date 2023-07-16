package bxn4.bencmds.commands.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentWeather {
    public double temperature;
    public double windspeed;
    public int weathercode;
    public int is_day;
    public String time;
}