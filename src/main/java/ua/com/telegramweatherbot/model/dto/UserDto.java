package ua.com.telegramweatherbot.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Value
public class UserDto implements Serializable {

    @NotNull
    long chatId;

    String firstname;

    String lastname;

    String username;

    String city;

    String language;

    String units;

    LocalDateTime lastWeatherRequest;

    LocalTime notificationTime;

}
