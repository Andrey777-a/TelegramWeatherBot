package ua.com.telegramweatherbot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.com.telegramweatherbot.Model.WeatherResponse;
import ua.com.telegramweatherbot.service.WeatherService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class WeatherRestController {

    private final WeatherService weatherService;

    @GetMapping("/{lat}&{lon}")
    public List<WeatherResponse> getCity(@PathVariable double lat, @PathVariable double lon) {

        return weatherService.getTemperature(lat, lon);

    }

    /*@GetMapping("/test")
    public List<WeatherResponse> getCityTest(@RequestParam String city) {

        return weatherService.getWeather(city);

    }*/

    @GetMapping("/test")
    public List<WeatherResponse> getCityTest(@RequestParam String city) {

        return weatherService.getWeather(city);

    }

}
