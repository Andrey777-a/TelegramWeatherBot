package ua.com.telegramweatherbot.bot;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.com.telegramweatherbot.Model.WeatherResponse;
import ua.com.telegramweatherbot.service.WeatherService;

import java.util.List;

@Slf4j
@Service
public class TelegramBotService extends TelegramLongPollingBot {

    private final String botName;
    private final WeatherService weatherService;

    @SneakyThrows
    public TelegramBotService(@Value("${BOT_KEY}") String botToken,
                              @Value("${BOT_NAME}") String botName,
                              WeatherService weatherService) {
        super(botToken);
        this.botName = botName;
        this.weatherService = weatherService;

        /*List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "start bot"));
        commands.add(new BotCommand("/test", "test"));
        commands.add(new BotCommand("/kiev", "get weather"));

        this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));*/
    }

    @Override
    public String getBotUsername() {
        return botName;
    }


    @SneakyThrows
    @Override
    public void onUpdateReceived(@NonNull Update update) {

        long chatId = 0;
        long userId = 0;
        String userName;
        String receivedMessage;

        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();
            userName = update.getMessage().getFrom().getFirstName();

            if (update.getMessage().hasText()) {
                receivedMessage = update.getMessage().getText();
                answerUtils(receivedMessage, chatId, userName);
            }

        } else if (update.hasCallbackQuery()) {

            chatId = update.getCallbackQuery().getMessage().getChatId();
            userName = update.getCallbackQuery().getFrom().getFirstName();
            receivedMessage = update.getCallbackQuery().getData();

            answerUtils(receivedMessage, chatId, userName);

        } else if (update.getMessage().getLocation() != null) {

            chatId = update.getMessage().getChatId();
            double latitude = update.getMessage().getLocation().getLatitude();
            double longitude = update.getMessage().getLocation().getLongitude();

            getWeatherLatLon(chatId, latitude, longitude);
        }


    }


    private void answerUtils(String receivedMessage, long chatId, String userName) {

        switch (receivedMessage) {
            case "/start" -> startBot(chatId, userName);
            case "/city" -> cityBot(chatId);
            case "Dnipro", "Kiev" -> getWeather(chatId, receivedMessage);
            case "/location" -> locationBot(chatId);
            case "/back" -> mainMenu(chatId);

        }

    }

    @SneakyThrows
    private void startBot(long chatId, String userName) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Привет, " + userName + " ! Я телеграм Бот.");

        sendMessage.setReplyMarkup(Button.inlineMarkup());

        execute(sendMessage);

    }

    @SneakyThrows
    private void mainMenu(long chatId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Меню");
        sendMessage.setReplyMarkup(Button.inlineMarkup());

        execute(sendMessage);

    }

    @SneakyThrows
    private void cityBot(long chatId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Необходимо выбрать город:");
        sendMessage.setReplyMarkup(Button.inlineMarkupCity());


        execute(sendMessage);

    }

    @SneakyThrows
    private void locationBot(long chatId) {
        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(chatId);
        sendMessage.setText("Пожалуйста, отправьте вашу геопозицию для прогноза погоды.");
        sendMessage.setReplyMarkup(Button.inlineMarkupLocation());

        execute(sendMessage);

    }

    private void getWeather(long chatId, String city) {

        List<WeatherResponse> weather = weatherService.getWeather(city);

        String format = String.format("Город: %s, погода: %s, максимальная: %s, минимальная: %s",
                weather.getFirst().getName(),
                weather.getFirst().getWeather().getFirst().getDescription(),
                weather.getFirst().getMain().getTempMax(),
                weather.getFirst().getMain().getTempMin());


        sendMessage(chatId, format);

    }

    private void getWeatherLatLon(long chatId, double lat, double lon) {

        List<WeatherResponse> temperature = weatherService.getTemperature(lat, lon);


        String format = String.format("Город: %s, погода: %s, максимальная: %s, минимальная: %s",
                temperature.getFirst().getName(),
                temperature.getFirst().getWeather().getFirst().getDescription(),
                temperature.getFirst().getMain().getTempMax(),
                temperature.getFirst().getMain().getTempMin());


        sendMessage(chatId, format);

    }

    @SneakyThrows
    private void sendMessage(long chatId, String message) {

        SendMessage build = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();

        execute(build);

        mainMenu(chatId);

    }
}
