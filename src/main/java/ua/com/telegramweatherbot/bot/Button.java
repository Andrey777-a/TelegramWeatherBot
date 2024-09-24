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
import ua.com.telegramweatherbot.service.UserInfoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class Button {

    private final CityService cityService;
    private final UserInfoService userInfoService;
    private final LocalizationService localizationService;

    public InlineKeyboardMarkup inlineMarkupLanguage(long chatId) {

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton> inlineKeyboardButtons = inlineKeyboardButtons(
                chatId,
                Map.of(
                        "button.language.ru", "language_ru",
                        "button.language.uk", "language_uk",
                        "button.language.en", "language_en"
                )
        );

        buttons.add(inlineKeyboardButtons);

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    public InlineKeyboardMarkup inlineKeyboardMetric(long chatId) {

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton> inlineKeyboardButtons = inlineKeyboardButtons(
                chatId,
                Map.of(
                        "button.unit.standard", "units_standard",
                        "button.unit.metric", "units_metric",
                        "button.unit.imperial", "units_imperial"
                )
        );

        buttons.add(inlineKeyboardButtons);

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    public InlineKeyboardMarkup inlineKeyboardSettings(long chatId) {

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton> inlineKeyboardButtons = inlineKeyboardButtons(
                chatId,
                Map.of(
                        "button.language", "lang",
                        "button.time.notification", "time_notification",
                        "button.default.city", "default_city",
                        "button.units", "default_units"
                )
        );

        buttons.add(inlineKeyboardButtons);

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
                    .get(userInfoService.getUserLanguage(chatId));

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

    public ReplyKeyboardMarkup replyKeyboardMarkupMenu(long chatId) {

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow keyboardButtons = replyKeyboardMarkups(chatId,
                List.of(
                        "button.location",
                        "button.city",
                        "button.settings"
                ));

        keyboardRows.add(keyboardButtons);

        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
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

    private List<InlineKeyboardButton> inlineKeyboardButtons(long chatId,
                                                             Map<String, String> buttons) {

        List<InlineKeyboardButton> row = new ArrayList<>();

        for (Map.Entry<String, String> entry : buttons.entrySet()) {

            InlineKeyboardButton button = new InlineKeyboardButton(
                    localizationService.getLocalizedButtonText(entry.getKey(), chatId)
            );

            button.setCallbackData(entry.getValue().trim());

            row.add(button);

        }

        return row;

    }

    private KeyboardRow replyKeyboardMarkups(long chatId,
                                             List<String> buttons) {

        KeyboardRow row = new KeyboardRow();

        for (String button : buttons) {

            KeyboardButton locationButton = new KeyboardButton(
                    localizationService.getLocalizedButtonText(button, chatId)
            );

            if (button.endsWith("location")) {
                locationButton.setRequestLocation(true);
            }

            row.add(locationButton);

        }

        return row;
    }

}
