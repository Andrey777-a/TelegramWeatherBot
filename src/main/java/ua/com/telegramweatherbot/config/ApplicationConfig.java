package ua.com.telegramweatherbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ApplicationConfig {

    @Value("${BASE_WEATHER_URL}")
    private String baseURL;

    @Bean
    public RestClient restClient() {

        return RestClient.builder()
                .baseUrl(baseURL)
                .build();
    }

}