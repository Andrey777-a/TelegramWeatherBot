package ua.com.telegramweatherbot.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.com.telegramweatherbot.bot.Button;
import ua.com.telegramweatherbot.model.dto.WeatherResponse;
import ua.com.telegramweatherbot.service.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageHandlerServiceImpl implements MessageHandlerService {

    UserManagementService userManagementService;
    UserInfoService userInfoService;
    WeatherService weatherService;
    MessageService messageService;
    SettingsShowService settingsShowService;
    LocalizationService localizationService;
    Button button;

    @Override
    public void handleMessage(
            Update update
    ) {

        String receivedMessage = update.getMessage().getText();

        long chatId = update.hasMessage()
                ? update.getMessage().getChatId()
                : update.getCallbackQuery().getMessage().getChatId();

        Map<String, Runnable> commandActions = getStringRunnableMap(
                chatId, update
        );

        if (commandActions.containsKey(receivedMessage)) {
            commandActions.get(receivedMessage).run();
        } else {
            messageService.sendUnknownCommandMessage(chatId);
        }

    }

    @Override
    public void handleLocation(
            Update update
    ) {

        long chatId = update.getMessage().getChatId();
        double latitude = update.getMessage().getLocation().getLatitude();
        double longitude = update.getMessage().getLocation().getLongitude();

        WeatherResponse weatherResponse = weatherService
                .getWeatherByCoordinates(latitude, longitude, chatId)
                .getFirst();

        userManagementService.updateLastWeatherRequest(chatId);

        String unit = userInfoService.getUserUnitsText(chatId);

        messageService.sendWeatherInfo(chatId, weatherResponse, unit);

    }

    private Map<String, Runnable> getStringRunnableMap(
            long chatId,
            Update update
    ) {

        Map<String, Runnable> commandActions = new HashMap<>();

        String settingText = localizationService
                .getLocalizedButtonText("button.settings", chatId);

        String cityText = localizationService
                .getLocalizedButtonText("button.city", chatId);

        commandActions.put(
                "/start",
                () -> startBot(update)
        );
        commandActions.put(
                cityText,
                () -> sendCityButtons(chatId, 1, 5, false)
        );
        commandActions.put(
                settingText,
                () -> settingsShowService.showSettings(chatId)
        );
        commandActions.put(
                "/settings",
                () -> settingsShowService.showSettings(chatId)
        );
        commandActions.put(
                "/help",
                () -> messageService.sendMessage(chatId, "help")
        );

        return commandActions;
    }


    private void sendCityButtons(
            long chatId,
            int page,
            int pageSize,
            boolean isForNotification
    ) {

        messageService.sendMessage(
                chatId,
                "show.options.change.city",
                button.inlineMarkupAllCity(
                        page, pageSize, chatId, isForNotification
                )
        );

    }

    private void startBot(
            Update update
    ) {

        long chatId = update.getMessage().getChat().getId();

        boolean present = userManagementService
                .findByChatId(chatId)
                .isPresent();

        String name = Optional.ofNullable(
                update.getMessage().getChat().getFirstName()
                ).orElse(
                        update.getMessage().getChat().getUserName()
        );

        if (present) {
            messageService.sendMessage(
                    chatId, "start.already.exists"
            );
            return;
        }

        userManagementService.createUser(update);
        messageService.sendMessage(chatId, "start", name);
        messageService.sendMessage(chatId, "help");

    }

}
