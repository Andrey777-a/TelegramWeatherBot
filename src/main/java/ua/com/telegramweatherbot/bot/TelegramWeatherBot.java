package ua.com.telegramweatherbot.bot;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.com.telegramweatherbot.model.dto.CityResponse;
import ua.com.telegramweatherbot.model.dto.UserDto;
import ua.com.telegramweatherbot.model.dto.WeatherResponse;
import ua.com.telegramweatherbot.service.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TelegramWeatherBot extends TelegramLongPollingBot {

    private final String botName;
    private final WeatherService weatherService;
    private final UserService userService;
    private final CityService cityService;
    private final MessageService messageService;
    private final Settings settingsService;
    private final Button button;
    private final LocalizationService localizationService;

    public TelegramWeatherBot(@Value("${BOT_KEY}") String botToken,
                              @Value("${BOT_NAME}") String botName,
                              WeatherService weatherService,
                              UserService userService,
                              CityService cityService,
                              MessageService messageService,
                              Settings settingsService,
                              Button button,
                              LocalizationService localizationService
    ) {
        super(botToken);
        this.botName = botName;
        this.weatherService = weatherService;
        this.userService = userService;
        this.cityService = cityService;
        this.messageService = messageService;
        this.settingsService = settingsService;
        this.button = button;
        this.localizationService = localizationService;

        try {

            this.execute(
                    new SetMyCommands(
                            BotCommands.LIST_OF_COMMANDS,
                            new BotCommandScopeDefault(),
                            null)
            );

        } catch (TelegramApiException e) {

            log.error(e.getMessage());

        }
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

        String settings = localizationService
                .getLocalizedButtonText("button.settings", chatId);

        String city = localizationService
                .getLocalizedButtonText("button.city", chatId);


        if (receivedMessage.equals("/start")) {

            startBot(update);

        } else if (receivedMessage.equals(city)) {

            sendCityButtons(chatId, 1, 5, false);

        } else if (receivedMessage.equals(settings)) {

            settingsService.showSettings(chatId);

        } else if (receivedMessage.equals("/help")) {

            messageService.sendMessage(chatId, "help");

        } else {
            messageService.sendUnknownCommandMessage(chatId);
        }

    }

    private void handleCallbackQuery(Update update) {

        String callbackData = update.getCallbackQuery().getData();

        long chatId = update.getCallbackQuery().getMessage().getChatId();

        Optional<UserDto> byTelegramId = userService
                .findByChatId(update.getCallbackQuery().getMessage().getChatId());

        if (callbackData.startsWith("city_")) {

            String cityName = callbackData.split("_")[1];

            getWeatherByCity(update.getCallbackQuery().getMessage().getChatId(),
                    cityName,
                    byTelegramId.get().getLanguage());

        } else if (callbackData.startsWith("page_")) {

            int page = Integer.parseInt(callbackData.split("_")[1]);

            sendCityButtons(chatId, page, 5, false);

        } else if (callbackData.equals("lang")) {

            settingsService.showLanguageOptions(chatId);

        } else if (callbackData.startsWith("language_")) {

            settingsService.changeLanguageLocalisation(chatId, callbackData.split("_")[1]);

        } else if (callbackData.equals("time_notification")) {

            Optional<UserDto> byChatId = userService.findByChatId(chatId);

            if (Optional.ofNullable(byChatId.get().getCity()).isEmpty()) {

                settingsService.showOptionsChangeCity(chatId, true);

                settingsService.showTimeOptions(chatId);

            } else {

                settingsService.showTimeOptions(chatId);

            }

        } else if (callbackData.matches("^([0][9]|[1][0-7]):[0-5][0-9]$")) {

            settingsService.changeTimeNotification(chatId, callbackData);

        } else if (callbackData.equals("default_city")) {

            settingsService.showOptionsChangeCity(chatId, true);

        } else if (callbackData.startsWith("change_")) {

            settingsService.changeDefaultCity(chatId, callbackData.split("_")[1]);

        }

    }

    private void startBot(Update update) {

        long chatId = update.getMessage().getChat().getId();

        String name = Optional.ofNullable(update.getMessage().getChat().getFirstName())
                .orElse(update.getMessage().getChat().getUserName());

        userService.createUser(update);

        messageService.sendMessage(chatId, "start", name);
        messageService.sendMessage(chatId, "help");

    }

    private void getWeatherByCity(long chatId, String city, String lang) {

        WeatherResponse weatherResponse = weatherService.getWeatherByCity(city, chatId).getFirst();

        List<CityResponse> localisation = cityService.getCity(city);

        String localNameCity = localisation.getFirst().getLocalNameList().get(lang);

        messageService.sendWeatherInfo(chatId, localNameCity, weatherResponse);

    }

    private void handleLocationUpdate(Update update) {

        long chatId = update.getMessage().getChatId();
        double latitude = update.getMessage().getLocation().getLatitude();
        double longitude = update.getMessage().getLocation().getLongitude();

        WeatherResponse weatherResponse = weatherService
                .getWeatherByCoordinates(latitude, longitude, chatId).getFirst();

        messageService.sendWeatherInfo(chatId, weatherResponse);

    }

    private void sendCityButtons(long chatId, int page, int pageSize, boolean isForNotification) {

        messageService.sendMessage(chatId, "show.options.change.city",
                button.inlineMarkupAllCity(page, pageSize, chatId, isForNotification));

    }
}
