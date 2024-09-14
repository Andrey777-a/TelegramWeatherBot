package ua.com.telegramweatherbot.bot;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.com.telegramweatherbot.Model.dto.CityDto;
import ua.com.telegramweatherbot.service.CityService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Button {

    private final CityService cityService;

    public static InlineKeyboardMarkup inlineMarkupLocalisation() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton rusButton = new InlineKeyboardButton();
        rusButton.setText("Русский");
        rusButton.setCallbackData("ru");

        InlineKeyboardButton ukrButton = new InlineKeyboardButton();
        ukrButton.setText("Українскій");
        ukrButton.setCallbackData("uk");

        InlineKeyboardButton engButton = new InlineKeyboardButton();
        engButton.setText("English");
        engButton.setCallbackData("en");

        row.add(rusButton);
        row.add(ukrButton);
        row.add(engButton);

        markup.setKeyboard(buttons);

        buttons.add(row);

        return markup;
    }

    public static InlineKeyboardMarkup inlineMarkupSettings() {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton lang = new InlineKeyboardButton();
        lang.setText("Мова");
        lang.setCallbackData("lang");

        row.add(lang);

        markup.setKeyboard(buttons);

        buttons.add(row);

        return markup;
    }

    public InlineKeyboardMarkup inlineMarkupAllCity(int page, int pageSize) {

        int totalCities = cityService.countCities();

        List<CityDto> cities = cityService.findCitiesWithPagination(page, pageSize);

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        for (CityDto cityDto : cities) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(cityDto.getName());
            button.setCallbackData("city_" + cityDto.getName());

            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(button);

            rowsInline.add(rowInline);

        }

        List<InlineKeyboardButton> navigationRow = getInlineKeyboardButtons(page, pageSize, totalCities);
        rowsInline.add(navigationRow);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInline);

        return markupInline;

    }

    private List<InlineKeyboardButton> getInlineKeyboardButtons(int page, int pageSize, int totalCities) {

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


    public static ReplyKeyboardMarkup replyKeyboardMarkup() {

        KeyboardRow row = new KeyboardRow();

        KeyboardButton locationButton = new KeyboardButton("Відправити геопозицію");
        locationButton.setRequestLocation(true);

        KeyboardButton cityButton = new KeyboardButton("Вибрати місто");

        KeyboardButton settingsButton = new KeyboardButton("Налаштування");

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
