package bxn4.bencmds.commands.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherData {
    public CurrentWeather current_weather;
    public HourlyUnits hourly_units;
    public Hourly hourly;
    public Daily daily;
}