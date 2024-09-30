package ua.com.telegramweatherbot.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WeatherConfig {

    WeatherProperties weatherProperties;

    @Bean
    public RestClient restClient() {

        return RestClient.builder()
                .baseUrl(weatherProperties.getBaseUrl())
                .build();
    }

}
