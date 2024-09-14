package ua.com.telegramweatherbot.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.com.telegramweatherbot.service.UserService;

@RequiredArgsConstructor
@Service
public class SettingsService {

    private final UserService userService;
    private final MessageService messageService;

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

}
