package ua.com.telegramweatherbot.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse implements Serializable {

    @JsonProperty("id")
    int id;

    @JsonProperty("name")
    String name;

    @JsonProperty("coord")
    Coord coord;

    @JsonProperty("weather")
    List<Weather> weather;

    @JsonProperty("main")
    Main main;

    @JsonProperty("wind")
    Wind wind;

    @JsonProperty("sys")
    Sys sys;

    @ToString
    @Getter
    public static class Coord {

        @JsonProperty("lon")
        double lon;
        @JsonProperty("lan")
        double lat;

    }

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

        @JsonProperty("humidity")
        private int humidity;

        @JsonProperty("temp_min")
        private double tempMin;

        @JsonProperty("temp_max")
        private double tempMax;

    }

    @ToString
    @Getter
    public static class Wind {

        @JsonProperty("speed")
        private double speed;
        ;
    }

    @ToString
    @Getter
    public static class Sys {

        @JsonProperty("country")
        private String country;

        @JsonProperty("sunrise")
        private long sunrise;

        @JsonProperty("sunset")
        private long sunset;

    }

}
