package ua.com.telegramweatherbot.service;

public interface UserInfoService {

    String getUserLanguage(long chatId);

    String getUserUnits(long chatId);

    String getUserUnitsText(long chatId);

}
