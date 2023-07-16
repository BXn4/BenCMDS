package bxn4.bencmds.commands.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hourly {
    public String[] time;
    public String[] temperature_2m;
    public Double[] relativehumidity_2m;
    public Double[] apparent_temperature;
    public Double[] precipitation_probability;
    public Double[] precipitation;
    public Integer[] weathercode;
    public String[] windspeed_10m;
    public String[] uv_index;
}