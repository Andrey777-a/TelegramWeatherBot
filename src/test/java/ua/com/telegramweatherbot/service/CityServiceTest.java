package ua.com.telegramweatherbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec;
import org.springframework.web.client.RestClient.RequestHeadersUriSpec;
import ua.com.telegramweatherbot.mapper.CityMapper;
import ua.com.telegramweatherbot.model.dto.CityDto;
import ua.com.telegramweatherbot.model.dto.CityResponse;
import ua.com.telegramweatherbot.model.entity.CityEntity;
import ua.com.telegramweatherbot.repository.CityRepository;
import ua.com.telegramweatherbot.service.impl.CityServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.web.client.RestClient.ResponseSpec;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private CityMapper cityMapper;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityServiceImpl cityService;

    private CityDto cityDto;
    private CityEntity cityEntity;
    private CityResponse cityResponse;

    @BeforeEach
    void setup() {

        cityDto = CityDto.builder()
                .id(1)
                .name("London")
                .build();

        cityEntity = CityEntity.builder()
                .id(1)
                .name("London")
                .build();

        cityResponse = CityResponse.builder()
                .name("London")
                .localNameList(Map.of("en", "London"))
                .build();
    }

    @Test
    void findCitiesWithPagination_shouldReturnCityDtoList() {

        Page<CityEntity> page = new PageImpl<>(List.of(cityEntity));

        when(cityRepository.findAllBy(any(Pageable.class))).thenReturn(page);
        when(cityMapper.toDto(cityEntity)).thenReturn(cityDto);

        List<CityDto> result = cityService.findCitiesWithPagination(1, 5);

        assertEquals(1, result.size());
        assertEquals(cityDto, result.get(0));

        verify(cityRepository).findAllBy(PageRequest.of(0, 5));
        verify(cityMapper).toDto(cityEntity);

        verifyNoInteractions(restClient);

    }

    @Test
    void countCities_shouldReturnCityCount() {

        when(cityRepository.countAllBy()).thenReturn(5);

        int count = cityService.countCities();

        assertEquals(5, count);

        verify(cityRepository).countAllBy();

        verifyNoInteractions(restClient, cityMapper);

    }

    @Test
    void getCity_shouldReturnCityResponse() {

        RequestHeadersUriSpec<?> requestHeadersUriSpec = mock(RequestHeadersUriSpec.class);
        RequestHeadersSpec<?> requestHeadersSpec = mock(RequestHeadersSpec.class);
        ResponseSpec responseSpec = mock(ResponseSpec.class);

        when(restClient.get()).thenReturn((RequestHeadersUriSpec)requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(List.of(cityResponse));


        List<CityResponse> city = cityService.getCity("London");

        assertEquals(1, city.size());

        verify(requestHeadersUriSpec).uri(any(Function.class));

    }

    @Test
    void getCityLocalisation_shouldReturnCityResponseList() {

        RequestHeadersUriSpec<?> requestHeadersUriSpec = mock(RequestHeadersUriSpec.class);
        RequestHeadersSpec<?> requestHeadersSpec = mock(RequestHeadersSpec.class);
        ResponseSpec responseSpec = mock(ResponseSpec.class);

        when(restClient.get()).thenReturn((RequestHeadersUriSpec)requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(List.of(cityResponse));

        List<CityResponse> result = cityService.getCitiesLocalisation(List.of("London", "New York"));

        assertEquals(2, result.size());
        verify(restClient, times(2)).get();
    }
}