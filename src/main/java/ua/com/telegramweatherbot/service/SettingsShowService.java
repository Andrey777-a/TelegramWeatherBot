package ua.com.telegramweatherbot.service;

public interface SettingsShowService {

    void showSettings(long chatId);

    void showLanguageOptions(long chatId);

    void showUnitsOptions(long chatId);

    void showTimeForNotifications(long chatId);

    void showChangeCityOptions(long chatId, boolean isForNotification);

}
