package ua.com.telegramweatherbot.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ua.com.telegramweatherbot.config.WeatherProperties;
import ua.com.telegramweatherbot.model.dto.WeatherResponse;
import ua.com.telegramweatherbot.service.UserInfoService;
import ua.com.telegramweatherbot.service.WeatherService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WeatherServiceImpl implements WeatherService {

    WeatherProperties weatherProperties;
    RestClient restClient;
    UserInfoService userInfoService;

    @Cacheable(
            value = "WeatherService::getWeatherByCity",
            key = "{#lat, #lon, #chatId}"
    )
    @Override
    public List<WeatherResponse> getWeatherByCoordinates(
            double lat,
            double lon,
            long chatId
    ) {

        List<WeatherResponse> weatherResponses = new ArrayList<>();

        WeatherResponse body = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(weatherProperties.getWeatherUrl())
                        .queryParam(
                                "lat", lat
                        )
                        .queryParam(
                                "lon", lon
                        )
                        .queryParam(
                                "apiKey", weatherProperties.getToken()
                        )
                        .queryParam(
                                "lang", userInfoService.getUserLanguage(chatId)
                        )
                        .queryParam(
                                "units", userInfoService.getUserUnits(chatId)
                        )
                        .build())
                .retrieve()
                .body(WeatherResponse.class);

        weatherResponses.add(body);

        log.info("{}", weatherResponses);

        return weatherResponses;
    }

    @Cacheable(
            value = "WeatherService::getWeatherByCity",
            key = "{#city, #chatId}"
    )
    @Override
    public List<WeatherResponse> getWeatherByCity(
            String city,
            long chatId
    ) {

        List<WeatherResponse> weatherResponses = new ArrayList<>();

        WeatherResponse body = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(weatherProperties.getWeatherUrl())
                        .queryParam(
                                "q", city
                        )
                        .queryParam(
                                "apiKey", weatherProperties.getToken()
                        )
                        .queryParam(
                                "lang", userInfoService.getUserLanguage(chatId)
                        )
                        .queryParam(
                                "units", userInfoService.getUserUnits(chatId)
                        )
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(WeatherResponse.class);

        weatherResponses.add(body);

        log.info("{}", weatherResponses);

        return weatherResponses;

    }
}
