package ua.com.telegramweatherbot.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;


@NoArgsConstructor(force = true)
@AllArgsConstructor
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse implements Serializable {

    @JsonProperty("id")
    int id;

    @JsonProperty("name")
    String name;

    @JsonProperty("weather")
    List<Weather> weather;

    @JsonProperty("main")
    Main main;


    @ToString
    @Getter
    public static class Weather {

        @JsonProperty
        private String main;

        @JsonProperty("description")
        private String description;

    }

    @ToString
    @Getter
    public static class Main {
        @JsonProperty("temp")
        private double temp;

        @JsonProperty("feels_like")
        private double feelsLike;

        @JsonProperty("temp_min")
        private double tempMin;

        @JsonProperty("temp_max")
        private double tempMax;

    }
}
