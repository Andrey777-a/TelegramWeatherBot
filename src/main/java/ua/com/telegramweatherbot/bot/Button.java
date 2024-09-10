package ua.com.telegramweatherbot.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Button {

    private static final InlineKeyboardButton START_BUTTON = new InlineKeyboardButton("Start");
    private static final InlineKeyboardButton CITY_BUTTON = new InlineKeyboardButton("City");
    private static final InlineKeyboardButton LOCATION_BUTTON = new InlineKeyboardButton("Send location");
    private static final InlineKeyboardButton HELP_BUTTON = new InlineKeyboardButton("Help");

    private static final InlineKeyboardButton KIEV_BUTTON = new InlineKeyboardButton("Kiev");
    private static final InlineKeyboardButton DNIPRO_BUTTON = new InlineKeyboardButton("Dnipro");

//    private static final InlineKeyboardButton DNIPRO_BUTTON = new InlineKeyboardButton("Dnipro");

    public static InlineKeyboardMarkup inlineMarkup() {
        START_BUTTON.setCallbackData("/start");
        CITY_BUTTON.setCallbackData("/city");
        LOCATION_BUTTON.setCallbackData("/location");
        HELP_BUTTON.setCallbackData("/help");

        List<InlineKeyboardButton> rowInline =
                List.of(START_BUTTON, CITY_BUTTON, LOCATION_BUTTON, HELP_BUTTON);

        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;

    }

    public static InlineKeyboardMarkup inlineMarkupCity() {
        KIEV_BUTTON.setCallbackData("Kiev");
        DNIPRO_BUTTON.setCallbackData("Dnipro");

        List<InlineKeyboardButton> rowInline =
                List.of(KIEV_BUTTON, DNIPRO_BUTTON);

        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;

    }


    public static InlineKeyboardMarkup inlineMarkupLanguage() {
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

    public static ReplyKeyboardMarkup inlineMarkupLocation() {

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);

        // Создаем строку с кнопкой
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        // Кнопка для отправки геопозиции
        KeyboardButton locationButton = new KeyboardButton();
        locationButton.setText("Отправить геопозицию");
        locationButton.setRequestLocation(true);

        // Добавляем кнопку в строку
        row.add(locationButton);
        keyboard.add(row);

        // Добавляем клавиатуру в сообщение
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;

    }

}
