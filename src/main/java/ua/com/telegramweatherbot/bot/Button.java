package ua.com.telegramweatherbot.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.com.telegramweatherbot.model.dto.CityDto;
import ua.com.telegramweatherbot.model.dto.CityResponse;
import ua.com.telegramweatherbot.service.CityService;
import ua.com.telegramweatherbot.service.LocalizationService;
import ua.com.telegramweatherbot.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class Button {

    private final CityService cityService;
    private final UserService userService;
    private final LocalizationService localizationService;

    public InlineKeyboardMarkup inlineMarkupLocalisation(long chatId) {

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton rusButton = new InlineKeyboardButton(
                localizationService.getLocalizedButtonText("button.ru", chatId)
        );
        rusButton.setCallbackData("language_ru");

        InlineKeyboardButton ukrButton = new InlineKeyboardButton(
                localizationService.getLocalizedButtonText("button.uk", chatId)
        );
        ukrButton.setCallbackData("language_uk");

        InlineKeyboardButton engButton = new InlineKeyboardButton(
                localizationService.getLocalizedButtonText("button.en", chatId)
        );
        engButton.setCallbackData("language_en");

        row.add(rusButton);
        row.add(ukrButton);
        row.add(engButton);

        buttons.add(row);

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    public InlineKeyboardMarkup inlineMarkupSettings(long chatId) {

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton lang = new InlineKeyboardButton(
                localizationService.getLocalizedButtonText("button.language", chatId)
        );
        lang.setCallbackData("lang");

        row.add(lang);

        InlineKeyboardButton notifications = new InlineKeyboardButton(
                localizationService.getLocalizedButtonText("button.time.notification", chatId)
        );
        notifications.setCallbackData("time_notification");

        row.add(notifications);

        InlineKeyboardButton defaultCity = new InlineKeyboardButton(
                localizationService.getLocalizedButtonText("button.default.city", chatId)
        );
        defaultCity.setCallbackData("default_city");

        row.add(defaultCity);

        buttons.add(row);

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    public InlineKeyboardMarkup inlineTimeKeyboard() {

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (int hour = 9; hour <= 17; hour++) {

            String time = String.format("%02d:00", hour);
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(time.trim());
            button.setCallbackData(time);

            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(button);

            rows.add(row);

        }
        return InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
    }

    public InlineKeyboardMarkup inlineMarkupAllCity(int page,
                                                    int pageSize,
                                                    long chatId,
                                                    boolean isForNotification) {

        int totalCities = cityService.countCities();

        List<CityDto> cities = cityService.findCitiesWithPagination(page, pageSize);

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        for (CityDto cityDto : cities) {

            List<CityResponse> city = cityService.getCity(cityDto.getName());

            String localNameCity = city.getFirst()
                    .getLocalNameList()
                    .get(userService.getUserLanguage(chatId));

            InlineKeyboardButton button = new InlineKeyboardButton(localNameCity);

            String callBackData = isForNotification ?
                    "change_" + localNameCity :
                    "city_" + localNameCity;

            button.setCallbackData(callBackData.trim());

            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(button);

            rowsInline.add(rowInline);

        }

        List<InlineKeyboardButton> navigationRow =
                getInlineKeyboardButtons(page, pageSize, totalCities);

        rowsInline.add(navigationRow);

        return InlineKeyboardMarkup.builder()
                .keyboard(rowsInline)
                .build();

    }

    private List<InlineKeyboardButton> getInlineKeyboardButtons(int page,
                                                                int pageSize,
                                                                int totalCities) {

        int totalPages = (int) Math.ceil((double) totalCities / pageSize);

        List<InlineKeyboardButton> navigationRow = new ArrayList<>();

        if (page > 1) {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText("⬅");
            inlineKeyboardButton.setCallbackData("page_" + (page - 1));
            navigationRow.add(inlineKeyboardButton);
        }
        if (page < totalPages) {

            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText("➡");
            inlineKeyboardButton.setCallbackData("page_" + (page + 1));
            navigationRow.add(inlineKeyboardButton);
        }
        return navigationRow;
    }


    public ReplyKeyboardMarkup replyKeyboardMarkup(long chatId) {

        KeyboardRow row = new KeyboardRow();

        KeyboardButton locationButton = new KeyboardButton(
                localizationService.getLocalizedButtonText("button.location", chatId)
        );
        locationButton.setRequestLocation(true);

        KeyboardButton cityButton = new KeyboardButton(
                localizationService.getLocalizedButtonText("button.city", chatId)
        );

        KeyboardButton settingsButton = new KeyboardButton(
                localizationService.getLocalizedButtonText("button.settings", chatId)
        );

        row.add(locationButton);
        row.add(cityButton);
        row.add(settingsButton);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(row);

        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();

    }
}
