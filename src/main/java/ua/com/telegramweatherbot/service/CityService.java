package ua.com.telegramweatherbot.service;

import ua.com.telegramweatherbot.model.dto.CityDto;
import ua.com.telegramweatherbot.model.dto.CityResponse;

import java.util.List;


public interface CityService {

    List<CityDto> findCitiesWithPagination(int page, int pageSize);

    int countCities();

    List<CityResponse> getCity(String cityName);

    List<CityResponse> getCitiesLocalisation(List<String> cityNames);

}
