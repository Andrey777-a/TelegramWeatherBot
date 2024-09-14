package ua.com.telegramweatherbot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.com.telegramweatherbot.Model.dto.CityResponse;
import ua.com.telegramweatherbot.Model.dto.WeatherResponse;
import ua.com.telegramweatherbot.service.impl.CityServiceImpl;
import ua.com.telegramweatherbot.service.impl.WeatherServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class WeatherRestController {

    private final WeatherServiceImpl weatherService;
    private final CityServiceImpl cityService;

    @GetMapping("/{lat}&{lon}")
    public List<WeatherResponse> getCityLocation(@PathVariable double lat, @PathVariable double lon) {

        return weatherService.getWeatherByCoordinates(lat, lon);

    }

    @GetMapping()
    public List<WeatherResponse> getCity(@RequestParam String city) {

        return weatherService.getWeatherByCity(city);

    }

    @GetMapping("/city")
    public List<CityResponse> getCityList(@RequestParam String city) {

        return cityService.getCity(city);

    }

    @GetMapping("/list")
    public List<CityResponse> getCityListTest(@RequestParam List<String> city) {

        return cityService.getCityLocalisation(city);

    }

}
