package ua.com.telegramweatherbot.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import ua.com.telegramweatherbot.model.dto.UserDto;
import ua.com.telegramweatherbot.model.entity.UserEntity;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserEntity> findAll();

    UserDto createUser(Update update);

    Optional<UserDto> findByChatId(long chatId);

    UserDto changeLanguage(long chatId, String language);

    UserDto changeTimeNotification(long chatId, LocalTime time);

    UserDto changeCity(Long chatId, String city);

    String getUserLanguage(long chatId);

}
