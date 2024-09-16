package ua.com.telegramweatherbot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ua.com.telegramweatherbot.model.dto.WeatherResponse;

public interface MessageService {

    void sendUnknownCommandMessage(long chatId);

    void sendWeatherInfo(long chatId, String localNameCity, WeatherResponse weatherResponse);

    void sendWeatherInfo(long chatId, WeatherResponse weatherResponse);

    void sendMessage(long chatId, String key, Object... args);

    void sendMessage(long chatId, String key, ReplyKeyboard replyKeyboard);

}
