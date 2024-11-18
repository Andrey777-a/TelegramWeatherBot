package ua.com.telegramweatherbot.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.com.telegramweatherbot.bot.Button;
import ua.com.telegramweatherbot.model.dto.CityResponse;
import ua.com.telegramweatherbot.model.dto.WeatherResponse;
import ua.com.telegramweatherbot.service.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CallbackQueryHandlerServiceImpl
        implements CallbackQueryHandlerService {

    WeatherService weatherService;
    UserInfoService userInfoService;
    UserManagementService userManagementService;
    CityService cityService;
    MessageService messageService;
    SettingsShowService settingsShowService;
    SettingsChangeService settingsChangeService;
    Button button;

    @Override
    public void handleCallbackQuery(
            Update update
    ) {

        String callbackData = update.getCallbackQuery().getData();

        long chatId = update.getCallbackQuery().getMessage().getChatId();

        Map<String, Runnable> commandActions = getStringRunnableMap(chatId);

        if (commandActions.containsKey(callbackData)) {
            commandActions.get(callbackData).run();
        } else {
            handleComplexCallback(chatId, callbackData);
        }

    }

    private Map<String, Runnable> getStringRunnableMap(
            long chatId
    ) {

        Map<String, Runnable> commandActions = new HashMap<>();

        commandActions.put(
                "lang",
                () -> settingsShowService.showLanguageOptions(chatId)
        );
        commandActions.put(
                "time_notification",
                () -> settingsShowService.showTimeForNotifications(chatId)
        );
        commandActions.put(
                "default_city",
                () -> settingsShowService.showChangeCityOptions(chatId, true)
        );
        commandActions.put(
                "default_units",
                () -> settingsShowService.showUnitsOptions(chatId)
        );

        return commandActions;
    }

    private void handleComplexCallback(
            long chatId,
            String callbackData
    ) {

        if (callbackData.startsWith("city_")) {

            handleWeatherRequest(
                    chatId,
                    callbackData.split("_")[1]
            );

        } else if (callbackData.startsWith("page_")) {

            int page = Integer.parseInt(callbackData.split("_")[1]);

            handlePage(
                    chatId,
                    page,
                    5,
                    false
            );

        } else if (callbackData.startsWith("language_")) {

            settingsChangeService.changeLanguageLocalisation(
                    chatId,
                    callbackData.split("_")[1]
            );

        } else if (callbackData.startsWith("change_")) {

            settingsChangeService.changeDefaultCity(
                    chatId,
                    callbackData.split("_")[1]
            );

        } else if (callbackData.startsWith("units_")) {

            settingsChangeService.changeDefaultUnits(
                    chatId,
                    callbackData.split("_")[1]
            );

        } else if (callbackData.matches("^([0][9]|[1][0-7]):[0-5][0-9]$")) {

            settingsChangeService.changeTimeNotification(
                    chatId,
                    callbackData
            );

        }
    }

    private void handleWeatherRequest(
            long chatId,
            String city
    ) {

        WeatherResponse weatherResponse = weatherService
                .getWeatherByCity(city, chatId)
                .getFirst();

        List<CityResponse> localisation = cityService.getCity(city);

        String language = userManagementService
                .findByChatId(chatId)
                .get()
                .getLanguage();

        String localNameCity = localisation
                .getFirst()
                .getLocalNameList()
                .get(language);

        userManagementService.updateLastWeatherRequest(chatId);

        String unit = userInfoService.getUserUnitsText(chatId);

        messageService.sendWeatherInfo(
                chatId,
                localNameCity,
                weatherResponse,
                unit
        );

    }

    private void handlePage(
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

}
