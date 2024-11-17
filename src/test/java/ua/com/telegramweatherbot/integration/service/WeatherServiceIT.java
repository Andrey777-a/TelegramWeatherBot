package ua.com.telegramweatherbot.integration.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import ua.com.telegramweatherbot.integration.IntegrationTestBase;
import ua.com.telegramweatherbot.model.dto.WeatherResponse;
import ua.com.telegramweatherbot.service.WeatherService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WeatherServiceIT extends IntegrationTestBase {

    WeatherService weatherService;

    @Test
    void testGetWeatherByCoordinates() {

        double lat = 46.4598;
        double lon = 30.7207;
        long chatId = 678228966L;

        List<WeatherResponse> weatherResponses =
                weatherService.getWeatherByCoordinates(lat, lon, chatId);

        assertThat(weatherResponses).hasSize(1);
        assertThat(weatherResponses.getFirst().getCoord().getLon()).isEqualTo(lon);
        assertThat(weatherResponses.getFirst().getName()).isEqualTo("Odesa");

    }

    @Test
    void testGetWeatherByCity() {

        String city = "New York";
        long chatId = 678228966L;

        List<WeatherResponse> weatherResponses =
                weatherService.getWeatherByCity(city, chatId);

        assertThat(weatherResponses).hasSize(1);
        assertThat(weatherResponses.getFirst().getCoord().getLon()).isEqualTo("74.006");
        assertThat(weatherResponses.getFirst().getName()).isEqualTo(city);

    }
}
