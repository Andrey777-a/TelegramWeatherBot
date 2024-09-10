/*
package ua.com.telegramweatherbot.bot;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.com.telegramweatherbot.Model.WeatherResponse;
import ua.com.telegramweatherbot.service.WeatherService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
//@Service
public class TelegramBotService {

    private final String botName;
    private final WeatherService weatherService;

    @SneakyThrows
    public TelegramBotService(@Value("${BOT_KEY}") String botToken,
                              @Value("${BOT_NAME}") String botName,
                              WeatherService weatherService) {
        super(botToken);
        this.botName = botName;
        this.weatherService = weatherService;

        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "start bot"));
        commands.add(new BotCommand("/test", "test"));
        commands.add(new BotCommand("/kiev", "get weather"));

        this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
    }

    @Override
    public String getBotUsername() {
        return botName;
    }


    @SneakyThrows
    @Override
    public void onUpdateReceived(@NonNull Update update) {


        if(update.hasMessage() && update.getMessage().hasText()) {

            String text = update.getMessage().getText();


            Long chatId = update.getMessage().getChatId();



            switch (text){
                case "/start"-> answer(chatId, update.getMessage().getChat().getFirstName());
//                case "/weather"-> getWeather(chatId, "Kiev");
                case "/kiev"-> getWeather(chatId, "Kiev");

            }
            SendMessage inlineKeyboard = createInlineKeyboard(chatId);

            execute(inlineKeyboard);

            var user = update.getMessage().getFrom();

            log.info("{} {} {} {}", update.getMessage(), update.getChatMember(), update.getMessage().getText(), user.getFirstName());

            if(update.getMessage().getLocation() != null && update.getMessage() !=null) {
                double latitude = update.getMessage().getLocation().getLatitude();
                double longitude = update.getMessage().getLocation().getLongitude();

//                Long chatId = update.getMessage().getChatId();

                getWeatherLatLon(chatId, latitude, longitude);

                System.out.println(update.getMessage().getLocation().getLatitude() + " "
                        + update.getMessage().getLocation().getLongitude());
            }
        }




        if (update.hasCallbackQuery()) {

            String callbackData = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals("Kiev")) {
                getWeather(chatId, callbackData);
//                sendMessage(chatId, getWeather(chatId, "Kiev"));
            } else if (callbackData.equals("Dnipro")) {
                getWeather(chatId, callbackData);
            }
        }
    }



    private void answer(Long chatId, String name) {

        String answer = "Hi, " + name + ", nice to meet you. ";
        sendMessage(chatId, answer);

    }

    private void getWeather(Long chatId, String city) {

        List<WeatherResponse> weather = weatherService.getWeather(city);

        String format = String.format("Город: %s, погода: %s, максимальная: %s, минимальная: %s",
                weather.getFirst().getName(),
                weather.getFirst().getWeather().getFirst().getDescription(),
                weather.getFirst().getMain().getTempMax(),
                weather.getFirst().getMain().getTempMin());


        sendMessage(chatId, format);

    }

    private void getWeatherLatLon(Long chatId, double lat, double lon) {

        List<WeatherResponse> temperature = weatherService.getTemperature(lat, lon);


        String format = String.format("погода: %s, Максимальная - %s, минимальная - %s",
                temperature.get(0).getWeather().getFirst(),
                temperature.getFirst().getMain().getTempMax(),
                temperature.getFirst().getMain().getTempMin());


        sendMessage(chatId, format);

    }

    @SneakyThrows
    private void sendMessage(Long chatId, String message) {
//        SendMessage sendMessage = new SendMessage();
//        createInlineKeyboard(chatId);

        SendMessage build = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(message);

        execute(build);

    }



    public SendMessage createInlineKeyboard(Long chatId) {
        // Создание объекта сообщения
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Choose an option:");

        // Создание инлайн-кнопок
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        // Первая кнопка
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Киев");
        button1.setCallbackData("Kiev"); // Данные, которые будут отправлены при нажатии на кнопку
        row1.add(button1);

        // Вторая кнопка
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Днепр");
        button2.setCallbackData("Dnipro"); // Данные для второй кнопки
        row1.add(button2);

        // Добавляем ряд с кнопками в клавиатуру
        rowsInline.add(row1);

        // Добавляем разметку клавиатуры к сообщению
        inlineKeyboardMarkup.setKeyboard(rowsInline);
        message.setReplyMarkup(inlineKeyboardMarkup);

        return message;
    }
}
*/
