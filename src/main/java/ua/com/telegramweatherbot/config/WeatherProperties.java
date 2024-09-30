package ua.com.telegramweatherbot.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "weather")
public class WeatherProperties {

    String token;

    @Setter(onMethod = @__({@Value("${weather.base-url}")}))
    String baseUrl;

    @Setter(onMethod_ = @__({@Value("${weather.weather-url}")}))
    String weatherUrl;

    @Setter(onMethod_ = @__({@Value("${weather.city-url}")}))
    String cityUrl;

}
