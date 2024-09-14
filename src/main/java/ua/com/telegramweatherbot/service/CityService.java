package ua.com.telegramweatherbot.service;

import ua.com.telegramweatherbot.Model.dto.CityDto;
import ua.com.telegramweatherbot.Model.dto.CityResponse;

import java.util.List;
import java.util.Optional;


public interface CityService {

    Optional<CityDto> findCityByName(String cityName);

    List<CityDto> findCitiesWithPagination(int page, int pageSize);

    int countCities();

    List<CityResponse> getCity(String cityName);

    List<CityResponse> getCityLocalisation(List<String> cityNames);

}
