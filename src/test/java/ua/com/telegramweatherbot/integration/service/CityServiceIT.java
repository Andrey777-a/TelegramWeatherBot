package ua.com.telegramweatherbot.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.cache.CacheManager;
import ua.com.telegramweatherbot.integration.IntegrationTestBase;
import ua.com.telegramweatherbot.model.dto.CityDto;
import ua.com.telegramweatherbot.model.dto.CityResponse;
import ua.com.telegramweatherbot.service.CityService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class CityServiceIT extends IntegrationTestBase {

    private final CityService cityService;
    private final CacheManager cacheManager;

    @Test
    void findCitiesWithPagination_shouldReturnListCity() {

        int page = 1;
        int pageSize = 5;

        List<CityDto> citiesWithPagination = cityService.findCitiesWithPagination(page, pageSize);

        assertThat(citiesWithPagination).hasSize(pageSize);

        assertThat(citiesWithPagination.get(0).getName()).isEqualTo("Київ");

        assertThat(citiesWithPagination.get(4).getName()).isEqualTo("Запоріжжя");

        String cacheKey = String.format("%d,%d", page, pageSize);

        Object o = cacheManager
                .getCache("CityService::findCitiesWithPagination").get(cacheKey).get();

        assertThat(o).isNotNull();

    }

    @Test
    void findCitiesWithPagination_shouldReturnEmptyList() {

        int page = 4;
        int pageSize = 5;

        List<CityDto> citiesWithPagination = cityService.findCitiesWithPagination(page, pageSize);

        assertThat(citiesWithPagination).isEmpty();

    }

    @Test
    void countCities_shouldReturnCorrectCount() {

        int i = cityService.countCities();

        assertThat(i).isEqualTo(14);

        Object o = cacheManager
                .getCache("CityService::countCities");

        assertThat(o).isNotNull();

    }

    @Test
    void getCity_shouldReturnCity() {

        String city = "London";
        String key = "uk";
        String expectedCity = "Лондон";

        List<CityResponse> cityResponses = cityService.getCity(city);

        assertThat(cityResponses).hasSize(1);
        assertThat(cityResponses.getFirst().getName()).isEqualTo(city);
        assertThat(cityResponses.getFirst().getLocalNameList().get(key)).isEqualTo(expectedCity);

        Object o = cacheManager
                .getCache("CityService::cityName")
                .get(cityResponses.getFirst().getName())
                .get();

        assertThat(o).isNotNull();

    }

    @Test
    void getCityLocalisation_shouldReturnListCity() {

        List<CityResponse> cityResponses = cityService.getCitiesLocalisation(
                List.of("Kiev", "Odesa", "Dnipro")
        );

        assertThat(cityResponses).hasSize(3);

    }

}