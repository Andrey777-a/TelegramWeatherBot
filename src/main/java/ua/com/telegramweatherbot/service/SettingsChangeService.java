package ua.com.telegramweatherbot.service;

public interface SettingsChangeService {

    void changeLanguageLocalisation(long chatId, String lang);

    void changeTimeNotification(long chatId, String time);

    void changeDefaultCity(long chatId, String city);

    void changeDefaultUnits(long chatId, String units);

}
