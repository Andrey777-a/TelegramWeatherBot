package ua.com.telegramweatherbot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.com.telegramweatherbot.model.dto.UserDto;
import ua.com.telegramweatherbot.service.CityService;
import ua.com.telegramweatherbot.service.UserService;

import java.time.LocalTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SettingsService {

    private final UserService userService;
    private final MessageService messageService;
    private final CityService cityService;

    public void showSettings(long chatId) {

        messageService.sendMessage(chatId, "Налаштування:", Button.inlineMarkupSettings());

    }

    public void showLanguageOptions(long chatId) {

        messageService.sendMessage(chatId, "Виберіть мову:", Button.inlineMarkupLocalisation());

    }

    public void changeLanguageLocalisation(long chatId, String lang) {

        userService.changeLanguage(chatId, lang);

        messageService.sendMessage(chatId, "Мова змінена. Гарного дня.");

    }

    public void showTimeOptions(long chatId) {

        messageService.sendMessage(chatId, "Виберіть час:",
                Button.inlineTimeKeyboard());

    }

    public void changeTimeNotification(long chatId, String time) {

        userService.changeTimeNotification(chatId, LocalTime.parse(time));

        messageService.sendMessage(chatId, "Час інформування встановлений.");

    }

    public void showOptionsChangeCity(long chatId, boolean isForNotification) {

        Optional<UserDto> byChatId = userService.findByChatId(chatId);

        if (Optional.ofNullable(byChatId.get().getCity()).isEmpty()) {
            messageService.sendMessage(chatId,
                    "Зараз базове місто не вказано.");

            messageService.sendMessage(chatId,
                    "Виберіть місто",
                    new Button(cityService).inlineMarkupAllCity(1, 5, isForNotification));

        } else {

            messageService.sendMessage(chatId,
                    "Зараз у вас встановлено місто " + byChatId.get().getCity());

            messageService.sendMessage(chatId,
                    "Виберіть місто",
                    new Button(cityService).inlineMarkupAllCity(1, 5, isForNotification));
        }

    }

    public void changeDefaultCity(long chatId, String city) {

        Optional<UserDto> byChatId = userService.findByChatId(chatId);

        if (Optional.ofNullable(byChatId.get().getCity()).equals(city.trim())) {

            messageService.sendMessage(chatId, "Місто не змінено, ви обрали поточне місто.");

        } else {
            userService.changeCity(chatId, city);
            messageService.sendMessage(chatId, "Місто змінено.");
        }
    }

}
