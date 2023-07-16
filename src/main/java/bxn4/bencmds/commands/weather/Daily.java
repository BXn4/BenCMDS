package bxn4.bencmds.commands.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Daily {
    public String[] sunrise;
    public String[] sunset;
    public Double[] temperature_2m_min;
    public Double[] temperature_2m_max;
}