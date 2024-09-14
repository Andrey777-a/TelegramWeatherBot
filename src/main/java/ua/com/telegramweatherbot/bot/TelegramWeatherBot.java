package ua.com.telegramweatherbot.bot;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import ua.com.telegramweatherbot.Model.dto.CityResponse;
import ua.com.telegramweatherbot.Model.dto.UserDto;
import ua.com.telegramweatherbot.Model.dto.WeatherResponse;
import ua.com.telegramweatherbot.service.impl.CityServiceImpl;
import ua.com.telegramweatherbot.service.impl.UserServiceImpl;
import ua.com.telegramweatherbot.service.impl.WeatherServiceImpl;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TelegramWeatherBot extends TelegramLongPollingBot {

    private final String botName;
    private final WeatherServiceImpl weatherService;
    private final UserServiceImpl userService;
    private final CityServiceImpl cityService;
    private final MessageService messageService;
    private final SettingsService settingsService;

    @SneakyThrows
    public TelegramWeatherBot(@Value("${BOT_KEY}") String botToken,
                              @Value("${BOT_NAME}") String botName,
                              WeatherServiceImpl weatherService,
                              UserServiceImpl userService, CityServiceImpl cityService, MessageService messageService, SettingsService settingsService) {
        super(botToken);
        this.botName = botName;
        this.weatherService = weatherService;
        this.userService = userService;
        this.cityService = cityService;
        this.messageService = messageService;
        this.settingsService = settingsService;

        this.execute(
                new SetMyCommands(
                        BotCommands.LIST_OF_COMMANDS,
                        new BotCommandScopeDefault(),
                        null)
        );
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(@NonNull Update update) {

        if (update.hasMessage()) {

            if (update.getMessage().hasText()) {

                handleMessage(update);

            } else if (update.getMessage().getLocation() != null) {

                handleLocationUpdate(update);

            }

        } else if (update.hasCallbackQuery()) {

            handleCallbackQuery(update);

        }
    }

    private void handleMessage(Update update) {

        String receivedMessage = update.getMessage().getText();

        long chatId = update.hasMessage() ?
                update.getMessage().getChatId() :
                update.getCallbackQuery().getMessage().getChatId();

        switch (receivedMessage) {
            case "/start" -> startBot(update);
            case "Вибрати місто" -> sendCityButtons(chatId, 1, 5);
            case "Налаштування" -> settingsService.showSettings(chatId);
            case "/help" -> messageService.sendHelpMessage(chatId);
            default -> messageService.sendUnknownCommandMessage(chatId);
        }

    }

    private void handleCallbackQuery(Update update) {

        String callbackData = update.getCallbackQuery().getData();

        Optional<UserDto> byTelegramId = userService
                .findByChatId(update.getCallbackQuery().getMessage().getChatId());

        if (callbackData.startsWith("city_")) {

            String cityName = callbackData.split("_")[1];

            getWeatherByCity(update.getCallbackQuery().getMessage().getChatId(),
                    cityName,
                    byTelegramId.get().getLanguage());

        } else if (callbackData.startsWith("page_")) {

            int page = Integer.parseInt(callbackData.split("_")[1]);

            sendCityButtons(update.getCallbackQuery().getMessage().getChatId(), page, 5);

        } else if (callbackData.equals("lang")) {

            settingsService.showLanguageOptions(update.getCallbackQuery().getMessage().getChatId());

        } else if (callbackData.matches("ru|uk|en")) {

            settingsService.changeLanguageLocalisation(update.getCallbackQuery().getMessage().getChatId(), callbackData);

        }
    }

    private void startBot(Update update) {

        long chatId = update.getMessage().getChat().getId();

        String name = Optional.ofNullable(update.getMessage().getChat().getFirstName())
                .orElse(update.getMessage().getChat().getUserName());

        String format = String.format(""" 
                        Привіт, %s!
                        Приємо познайомитись, Я телеграм бот Барбос 🐶.
                        Ви можете дізнатися прогноз погоди для свого міста.
                        Скористайтесь кнопками для вибору налаштувань.
                        """, name);

        messageService.sendMessage(chatId, format);
        messageService.sendHelpMessage(chatId);

        userService.createUser(update);

    }

    private void getWeatherByCity(long chatId, String city, String lang) {

        WeatherResponse weatherResponse = weatherService.getWeatherByCity(city).getFirst();

        List<CityResponse> localisation = cityService.getCity(city);

        String localNameCity = localisation.getFirst().getLocalNameList().get(lang);

        log.info("{}", weatherResponse);

        messageService.sendWeatherInfo(chatId, localNameCity, weatherResponse);

    }

    private void handleLocationUpdate(Update update) {

        long chatId = update.getMessage().getChatId();
        double latitude = update.getMessage().getLocation().getLatitude();
        double longitude = update.getMessage().getLocation().getLongitude();

        WeatherResponse response = weatherService
                .getWeatherByCoordinates(latitude, longitude).getFirst();

        String format = String.format("%s, %s°C",
                response.getMain().getTemp(),
                response.getWeather().getFirst().getDescription());

        messageService.sendMessage(chatId, format);

    }

    private void sendCityButtons(long chatId, int page, int pageSize) {

        messageService.sendMessage(chatId, "Оберіть місто:",
                new Button(cityService).inlineMarkupAllCity(page, pageSize));

    }
}
