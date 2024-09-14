package ua.com.telegramweatherbot.Model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
public class UserDto implements Serializable {

    @NotNull
    Long telegramId;

    @NotNull
    long chatId;

    String firstname;

    String lastname;

    String username;

    String city;

    String language;

    String units;

    LocalDateTime lastWeatherRequest;

    LocalDateTime notificationTime;



}
