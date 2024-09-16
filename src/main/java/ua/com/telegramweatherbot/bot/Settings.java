package ua.com.telegramweatherbot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.com.telegramweatherbot.model.dto.UserDto;
import ua.com.telegramweatherbot.service.MessageService;
import ua.com.telegramweatherbot.service.UserService;

import java.time.LocalTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class Settings {

    private final UserService userService;
    private final MessageService messageService;
    private final Button button;

    public void showSettings(long chatId) {

        messageService.sendMessage(
                chatId,
                "show.settings",
                button.inlineMarkupSettings(chatId)
        );

    }

    public void showLanguageOptions(long chatId) {

        messageService.sendMessage(chatId,
                "show.language.options",
                button.inlineMarkupLocalisation(chatId)
        );

    }

    public void changeLanguageLocalisation(long chatId, String lang) {

        userService.changeLanguage(chatId, lang);

        messageService.sendMessage(chatId, "change.language");

    }

    public void showTimeOptions(long chatId) {

        messageService.sendMessage(chatId, "show.time.options",
                button.inlineTimeKeyboard());

    }

    public void changeTimeNotification(long chatId, String time) {

        userService.changeTimeNotification(chatId, LocalTime.parse(time));

        messageService.sendMessage(chatId, "change.time.notification");

    }

    public void showOptionsChangeCity(long chatId, boolean isForNotification) {

        Optional<UserDto> byChatId = userService.findByChatId(chatId);

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
                    button.inlineMarkupAllCity(1, 5,chatId, isForNotification));
        }

    }

    public void changeDefaultCity(long chatId, String city) {

        Optional<UserDto> byChatId = userService.findByChatId(chatId);

        if (Optional.ofNullable(byChatId.get().getCity()).equals(city.trim())) {

            messageService.sendMessage(chatId, "no.change.default.city");

        } else {

            userService.changeCity(chatId, city);
            messageService.sendMessage(chatId, "change.default.city");

        }
    }

}
