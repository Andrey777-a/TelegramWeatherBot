package ua.com.telegramweatherbot.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ua.com.telegramweatherbot.model.dto.CityResponse;
import ua.com.telegramweatherbot.model.dto.WeatherResponse;
import ua.com.telegramweatherbot.service.impl.CityServiceImpl;
import ua.com.telegramweatherbot.service.impl.WeatherServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/weather")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WeatherRestController {

    WeatherServiceImpl weatherService;
    CityServiceImpl cityService;

    @GetMapping("/{lat}&{lon}")
    public List<WeatherResponse> getCityLocation(@PathVariable double lat, @PathVariable double lon) {

        return weatherService.getWeatherByCoordinates(lat, lon, 0);

    }

    @GetMapping()
    public List<WeatherResponse> getCity(@RequestParam String city) {

        return weatherService.getWeatherByCity(city, 0);

    }

    @GetMapping("/cities")
    public List<CityResponse> getCityList(@RequestParam List<String> city) {

        return cityService.getCityLocalisation(city);

    }

}
