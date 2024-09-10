package ua.com.telegramweatherbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ApplicationConfig {

    @Value("${BASE_WEATHER_URL}")
    private String baseURL;

    @Value("${WEATHER_API_KEY}")
    private String apiKey;

    @Bean
    public RestClient restClient() {

        return RestClient.builder()
                .baseUrl(baseURL)
//                .defaultUriVariables(Map.of("test", "a46d6d8cfbea91a7be7bd8ab16b3d351"))
                .build();
    }

}
