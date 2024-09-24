package ua.com.telegramweatherbot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ua.com.telegramweatherbot.service.LocalizationService;
import ua.com.telegramweatherbot.service.UserInfoService;

import java.util.Locale;

@RequiredArgsConstructor
@Service
public class LocalizationServiceImpl implements LocalizationService {

    private final MessageSource messageSource;
    private final UserInfoService userInfoService;

    @Override
    public String getMessageSource(String key, long chatId, Object... args) {

        return messageSource.getMessage(
                key,
                args,
                Locale.of(userInfoService.getUserLanguage(chatId))
        );

    }

    @Override
    public String getLocalizedButtonText(String key, long chatId) {

        return messageSource.getMessage(
                key,
                null,
                Locale.of(userInfoService.getUserLanguage(chatId))
        );

    }

}
