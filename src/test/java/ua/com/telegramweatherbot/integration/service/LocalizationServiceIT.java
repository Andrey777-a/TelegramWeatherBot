package ua.com.telegramweatherbot.integration.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import ua.com.telegramweatherbot.integration.IntegrationTestBase;
import ua.com.telegramweatherbot.service.LocalizationService;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocalizationServiceIT extends IntegrationTestBase {

    LocalizationService localizationService;

    @Test
    void testGetMessageSource() {

        String key = "start";
        long chatId = 678228966L;
        long chatId2 = 678228745L;

        String messageSource = localizationService.getMessageSource(key, chatId, "Jon");
        assertThat(messageSource).isEqualTo("Hello Jon!");

        String messageSource2 = localizationService.getMessageSource(key, chatId2, "Сергій");
        assertThat(messageSource2).isEqualTo("Привіт Сергій!");

    }

    @Test
    void testGetLocalizedButtonText() {

        String key = "button.save";
        long chatId = 678228966L;
        long chatId2 = 678228745L;

        String messageSource = localizationService.getMessageSource(key, chatId);
        assertThat(messageSource).isEqualTo("Save.");

        String messageSource2 = localizationService.getMessageSource(key, chatId2);
        assertThat(messageSource2).isEqualTo("Зберегти.");

    }

}
