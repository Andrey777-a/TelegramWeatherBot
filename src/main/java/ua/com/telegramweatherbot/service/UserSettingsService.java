package ua.com.telegramweatherbot.service;

import ua.com.telegramweatherbot.model.dto.UserDto;

import java.time.LocalTime;

public interface UserSettingsService {

    UserDto changeCity(Long chatId, String city);

    UserDto changeLanguage(long chatId, String language);

    UserDto changeTimeNotification(long chatId, LocalTime time);

    UserDto changeUnits(long chatId, String units);

}
