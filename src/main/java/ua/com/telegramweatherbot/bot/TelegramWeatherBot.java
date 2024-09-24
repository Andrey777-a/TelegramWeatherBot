package ua.com.telegramweatherbot.bot;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.com.telegramweatherbot.service.CallbackQueryHandlerService;
import ua.com.telegramweatherbot.service.MessageHandlerService;
import ua.com.telegramweatherbot.service.impl.CallbackQueryHandlerServiceImpl;
import ua.com.telegramweatherbot.service.impl.MessageHandlerServiceImpl;

@Slf4j
@Service
public class TelegramWeatherBot extends TelegramLongPollingBot {

    private final String botName;
    private final MessageHandlerService messageHandlerService;
    private final CallbackQueryHandlerService callbackQueryHandlerService;

    public TelegramWeatherBot(
            @Value("${BOT_KEY}") String botToken,
            @Value("${BOT_NAME}") String botName,
            MessageHandlerServiceImpl messageHandlerService,
            CallbackQueryHandlerServiceImpl callbackQueryHandlerService
    ) {
        super(botToken);
        this.botName = botName;
        this.messageHandlerService = messageHandlerService;
        this.callbackQueryHandlerService = callbackQueryHandlerService;

        try {

            this.execute(
                    new SetMyCommands(
                            BotCommands.LIST_OF_COMMANDS,
                            new BotCommandScopeDefault(),
                            null)
            );

        } catch (TelegramApiException e) {

            log.error(e.getMessage());

        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(@NonNull Update update) {

        if (update.hasMessage()) {

            if (update.getMessage().hasText()) {

                messageHandlerService.handleMessage(update);

            } else if (update.getMessage().getLocation() != null) {

                messageHandlerService.handleLocation(update);

            }

        } else if (update.hasCallbackQuery()) {

            callbackQueryHandlerService.handleCallbackQuery(update);

        }
    }
}
