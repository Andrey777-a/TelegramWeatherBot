package ua.com.telegramweatherbot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageHandlerService {

    void handleMessage(Update update);

    void handleLocation(Update update);

}
