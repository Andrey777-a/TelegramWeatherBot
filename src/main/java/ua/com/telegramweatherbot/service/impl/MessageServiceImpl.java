package ua.com.telegramweatherbot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.com.telegramweatherbot.bot.Button;
import ua.com.telegramweatherbot.bot.TelegramWeatherBot;
import ua.com.telegramweatherbot.model.dto.WeatherResponse;
import ua.com.telegramweatherbot.service.LocalizationService;
import ua.com.telegramweatherbot.service.MessageService;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    private final TelegramWeatherBot botService;
    private final LocalizationService localizationService;
    private final Button button;

    @Autowired
    public MessageServiceImpl(@Lazy TelegramWeatherBot botService,
                              LocalizationService localizationService, Button button) {
        this.botService = botService;
        this.localizationService = localizationService;
        this.button = button;
    }

    @Override
    public void sendUnknownCommandMessage(long chatId) {

        sendMessage(chatId, "unknown.command");

    }

    @Override
    public void sendWeatherInfo(long chatId, String localNameCity, WeatherResponse weatherResponse) {

        sendMessage(chatId, "weather", localNameCity,
                weatherResponse.getMain().getTemp(),
                weatherResponse.getWeather().getFirst().getDescription());
    }

    @Override
    public void sendWeatherInfo(long chatId, WeatherResponse weatherResponse) {

        sendMessage(chatId, "weather.location",
                weatherResponse.getMain().getTemp(),
                weatherResponse.getWeather().getFirst().getDescription());
    }

    @Override
    public void sendMessage(long chatId, String key, Object... args) {

        String localMessage = localizationService.getMessageSource(key, chatId, args);

        SendMessage build = SendMessage.builder()
                .chatId(chatId)
                .text(localMessage)
                .replyMarkup(button.replyKeyboardMarkup(chatId))
                .build();

        try {

            botService.execute(build);

        } catch (TelegramApiException e) {

            log.error(e.getMessage());

        }
    }

    @Override
    public void sendMessage(long chatId, String key, ReplyKeyboard replyKeyboard) {

        String localMessage = localizationService.getMessageSource(key, chatId);

        SendMessage build = SendMessage.builder()
                .chatId(chatId)
                .text(localMessage)
                .replyMarkup(replyKeyboard)
                .build();

        try {

            botService.execute(build);

        } catch (TelegramApiException e) {

            log.error(e.getMessage());

        }
    }
}
