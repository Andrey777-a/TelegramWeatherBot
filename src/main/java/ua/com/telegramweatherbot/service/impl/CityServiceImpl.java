package ua.com.telegramweatherbot.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import ua.com.telegramweatherbot.config.WeatherProperties;
import ua.com.telegramweatherbot.mapper.CityMapper;
import ua.com.telegramweatherbot.model.dto.CityDto;
import ua.com.telegramweatherbot.model.dto.CityResponse;
import ua.com.telegramweatherbot.repository.CityRepository;
import ua.com.telegramweatherbot.service.CityService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CityServiceImpl implements CityService {

    WeatherProperties weatherProperties;
    RestClient restClient;
    CityMapper cityMapper;
    CityRepository cityRepository;

    @Cacheable(value = "CityService::findCitiesWithPagination",
            key = "{#page, #pageSize}", unless = "#result==null")
    @Override
    public List<CityDto> findCitiesWithPagination(int page, int pageSize) {

        Pageable pageable = PageRequest.of(page - 1, pageSize);

        return cityRepository.findAllBy(pageable)
                .stream()
                .map(cityMapper::toDto)
                .toList();
    }


    @Cacheable(value = "CityService::countCities", key = "'count'", unless = "#result==0")
    @Override
    public int countCities() {
        return cityRepository.countAllBy();
    }

    @Cacheable(value = "CityService::cityName", key = "#cityName")
    @Override
    public List<CityResponse> getCity(String cityName) {

        List<CityResponse> cityResponses = new ArrayList<>();

        List<CityResponse> city = restClient.get()
                .uri(uriBuilder -> uriBuilder.path(weatherProperties.getCityUrl())
                        .queryParam("q", cityName)
                        .queryParam("apiKey", weatherProperties.getToken())
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        cityResponses.add(Objects.requireNonNull(city).getFirst());

        log.info("{}", cityResponses);

        return cityResponses;
    }

    @Override
    public List<CityResponse> getCitiesLocalisation(List<String> cityNames) {

        List<CityResponse> cityResponses = new ArrayList<>();

        for (String cityName : cityNames) {

            List<CityResponse> city = restClient.get()
                    .uri(uriBuilder -> uriBuilder.path(weatherProperties.getCityUrl())
                            .queryParam("q", cityName)
                            .queryParam("apiKey", weatherProperties.getToken())
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            cityResponses.add(Objects.requireNonNull(city.getFirst()));

        }

        log.info("{}", cityResponses);

        return cityResponses;
    }

}
