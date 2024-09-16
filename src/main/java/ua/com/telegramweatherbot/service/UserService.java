package ua.com.telegramweatherbot.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import ua.com.telegramweatherbot.model.dto.UserDto;
import ua.com.telegramweatherbot.model.entity.UserEntity;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserEntity> findAll();

    void createUser(Update update);

    Optional<UserDto> findByChatId(long chatId);

    void changeLanguage(long chatId, String language);

    void changeTimeNotification(long chatId, LocalTime time);

    void changeCity(Long chatId, String city);

    String getUserLanguage(long chatId);
}
