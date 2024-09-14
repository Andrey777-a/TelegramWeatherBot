package ua.com.telegramweatherbot.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import ua.com.telegramweatherbot.Model.dto.UserDto;

import java.util.Optional;

public interface UserService {

    void createUser(Update update);

    Optional<UserDto> findByChatId(long telegramId);

    void changeLanguage(Long chatId, String language);

}
