package ua.com.telegramweatherbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ua.com.telegramweatherbot.Model.WeatherResponse;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@RequiredArgsConstructor
public class WeatherService {

    @Value("${WEATHER_API_KEY}")
    private String apiKey;

    private final RestClient restClient;

    public List<WeatherResponse> getTemperature(double lat, double lon) {

        List<WeatherResponse> weatherResponses = new ArrayList<>();

        WeatherResponse body = restClient.get()
                .uri("?lat={lat}&lon={lon}&appid={token}&units=metric", lat, lon, apiKey)
                .retrieve()
                .body(WeatherResponse.class);

        weatherResponses.add(body);

        return weatherResponses;
    }

    public List<WeatherResponse> getWeather(String city) {

//        String uri = "?q={city}&appid={apiKey}&units=metric&lang=ru";

        List<WeatherResponse> weatherResponses = new ArrayList<>();

        WeatherResponse body = restClient.get()
//                .uri(uri, city, apiKey)
                .uri(uriBuilder -> uriBuilder
                        .queryParam("q", city)
                        .queryParam("apiKey", apiKey)
                        .queryParam("units", "metric")
                        .queryParam("lang", "ru")
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(WeatherResponse.class);

        System.out.println(body.toString());

        weatherResponses.add(body);

        return weatherResponses;

    }

}
