package ua.com.telegramweatherbot.service;

import ua.com.telegramweatherbot.model.dto.WeatherResponse;

import java.util.List;

public interface WeatherService {

    List<WeatherResponse> getWeatherByCoordinates(double lat, double lon);

    List<WeatherResponse> getWeatherByCity(String city);
}
