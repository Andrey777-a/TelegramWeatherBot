package ua.com.telegramweatherbot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallbackQueryHandlerService {

    void handleCallbackQuery(Update update);

}
