package ua.com.telegramweatherbot.service;

public interface LocalizationService {

    String getMessageSource(String key, long chatId, Object... args);

    String getLocalizedButtonText(String key, long chatId);

}
