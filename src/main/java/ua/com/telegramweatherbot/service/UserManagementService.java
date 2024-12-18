package ua.com.telegramweatherbot.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import ua.com.telegramweatherbot.model.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserManagementService {

    List<UserDto> findAll();

    Optional<UserDto> findByChatId(long chatId);

    UserDto createUser(Update update);

    UserDto updateLastWeatherRequest(long chatId);

}
