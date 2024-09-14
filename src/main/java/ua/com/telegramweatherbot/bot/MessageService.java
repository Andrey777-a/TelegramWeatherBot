package ua.com.telegramweatherbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.com.telegramweatherbot.Model.dto.WeatherResponse;

@Slf4j
@Service
public class MessageService {

    private final TelegramWeatherBot botService;

    @Autowired
    public MessageService(@Lazy TelegramWeatherBot botService) {
        this.botService = botService;
    }

    public void sendHelpMessage(long chatId) {

        String HELP_TEXT = """ 
                Доступні команди:
                /start - запуск бота
                /help - меню допомоги
                Якщо необхідно дізнатись погодні умови саме в точці,
                в якій ви знаходитесь - натисніть на кнопку "Відправити геопозицію"
                При натискані кнопки "вибрати місто" - вам будуть надані міста,
                в яких можна переглянути погоду
                """;

        sendMessage(chatId, HELP_TEXT);

    }

    public void sendUnknownCommandMessage(long chatId) {

        sendMessage(chatId, "Невідома команда, перегляньте доступні команди через /help.");

    }

    public void sendWeatherInfo(long chatId, String localNameCity, WeatherResponse weatherResponse) {

        String weatherInfo = String.format("Погода в місті %s - %s°C, %s",
                localNameCity,
                weatherResponse.getMain().getTemp(),
                weatherResponse.getWeather().getFirst().getDescription());

        sendMessage(chatId, weatherInfo);
    }

    public void sendMessage(long chatId, String message) {

        SendMessage build = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .replyMarkup(Button.replyKeyboardMarkup())
                .build();

        try {

            botService.execute(build);

        } catch (TelegramApiException e) {

            log.error(e.getMessage());

        }
    }


    public void sendMessage(long chatId, String message, ReplyKeyboard replyKeyboard) {

        SendMessage build = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .replyMarkup(replyKeyboard)
                .build();

        try {

            botService.execute(build);

        } catch (TelegramApiException e) {

            log.error(e.getMessage());

        }
    }
}
