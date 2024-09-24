package ua.com.telegramweatherbot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.com.telegramweatherbot.bot.Button;
import ua.com.telegramweatherbot.model.dto.UserDto;
import ua.com.telegramweatherbot.service.*;

import java.time.LocalTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SettingsServiceImpl implements SettingsShowService, SettingsChangeService {

    private final UserSettingsService userSettingsService;
    private final UserManagementService userManagementService;
    private final MessageService messageService;
    private final Button button;

    @Override
    public void showSettings(long chatId) {

        messageService.sendMessage(
                chatId,
                "show.settings",
                button.inlineKeyboardSettings(chatId)
        );

    }

    @Override
    public void showLanguageOptions(long chatId) {

        messageService.sendMessage(chatId,
                "show.language.options",
                button.inlineMarkupLanguage(chatId)
        );

    }

    @Override
    public void showUnitsOptions(long chatId) {

        messageService.sendMessage(chatId, "show.units.options",
                button.inlineKeyboardMetric(chatId));

    }

    @Override
    public void showTimeForNotifications(long chatId) {

        Optional<UserDto> byChatId = userManagementService.findByChatId(chatId);

        if (Optional.ofNullable(byChatId.get().getCity()).isEmpty()) {

            showChangeCityOptions(chatId, true);

        }

        showTimeOptions(chatId);

    }

    @Override
    public void showChangeCityOptions(long chatId, boolean isForNotification) {

        Optional<UserDto> byChatId = userManagementService.findByChatId(chatId);

        if (Optional.ofNullable(byChatId.get().getCity()).isEmpty()) {
            messageService.sendMessage(chatId,
                    "default.city.no");

            messageService.sendMessage(chatId,
                    "show.options.change.city",
                    button.inlineMarkupAllCity(1, 5, chatId, isForNotification));

        } else {

            messageService.sendMessage(chatId,
                    "default.city.yes", byChatId.get().getCity());

            messageService.sendMessage(chatId,
                    "show.options.change.city",
                    button.inlineMarkupAllCity(1, 5, chatId, isForNotification));
        }

    }

    @Override
    public void changeLanguageLocalisation(long chatId, String lang) {

        userSettingsService.changeLanguage(chatId, lang);

        messageService.sendMessage(chatId, "change.language");

    }

    @Override
    public void changeTimeNotification(long chatId, String time) {

        userSettingsService.changeTimeNotification(chatId, LocalTime.parse(time));

        messageService.sendMessage(chatId, "change.time.notification");

    }

    @Override
    public void changeDefaultCity(long chatId, String city) {

        Optional<UserDto> byChatId = userManagementService.findByChatId(chatId);

        if (Optional.ofNullable(byChatId.get().getCity()).equals(city.trim())) {

            messageService.sendMessage(chatId, "no.change.default.city");

        } else {

            userSettingsService.changeCity(chatId, city);

            messageService.sendMessage(chatId, "change.default.city");

        }
    }

    @Override
    public void changeDefaultUnits(long chatId, String units) {

        Optional<UserDto> byChatId = userManagementService.findByChatId(chatId);

        if (Optional.ofNullable(byChatId.get().getUnits()).equals(units.trim())) {

            messageService.sendMessage(chatId, "no.change.default.units");

        } else {

            userSettingsService.changeUnits(chatId, units);

            messageService.sendMessage(chatId, "change.default.units");

        }
    }

    private void showTimeOptions(long chatId) {

        messageService.sendMessage(chatId, "show.time.options",
                button.inlineTimeKeyboard());

    }

}
