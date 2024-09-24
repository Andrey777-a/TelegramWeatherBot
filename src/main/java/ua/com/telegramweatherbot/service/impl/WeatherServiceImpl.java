package ua.com.telegramweatherbot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ua.com.telegramweatherbot.model.dto.WeatherResponse;
import ua.com.telegramweatherbot.service.UserInfoService;
import ua.com.telegramweatherbot.service.WeatherService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    @Value("${WEATHER_API_KEY}")
    private final String apiKey;
    private final RestClient restClient;
    private final UserInfoService userInfoService;

    @Cacheable(value = "WeatherService::getWeatherByCity", key = "{#lat, #lon, #chatId}")
    @Override
    public List<WeatherResponse> getWeatherByCoordinates(double lat, double lon, long chatId) {

        List<WeatherResponse> weatherResponses = new ArrayList<>();

        WeatherResponse body = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("data/2.5/weather")
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("apiKey", apiKey)
                        .queryParam("lang", userInfoService.getUserLanguage(chatId))
                        .queryParam("units", userInfoService.getUserUnits(chatId))
                        .build())
                .retrieve()
                .body(WeatherResponse.class);

        weatherResponses.add(body);

        log.info("{}", weatherResponses);

        return weatherResponses;
    }

    @Cacheable(value = "WeatherService::getWeatherByCity", key = "{#city, #chatId}")
    @Override
    public List<WeatherResponse> getWeatherByCity(String city, long chatId) {

        List<WeatherResponse> weatherResponses = new ArrayList<>();

        WeatherResponse body = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/data/2.5/weather")
                        .queryParam("q", city)
                        .queryParam("apiKey", apiKey)
                        .queryParam("lang", userInfoService.getUserLanguage(chatId))
                        .queryParam("units", userInfoService.getUserUnits(chatId))
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(WeatherResponse.class);

        weatherResponses.add(body);

        log.info("{}", weatherResponses);

        return weatherResponses;

    }
}
