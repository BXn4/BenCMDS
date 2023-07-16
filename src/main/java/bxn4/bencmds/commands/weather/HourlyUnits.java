package bxn4.bencmds.commands.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HourlyUnits {
    public String temperature_2m;
    public String relativehumidity_2m;
    public String apparent_temperature;
    public String precipitation_probability;
    public String precipitation;
    public String windspeed_10m;
}