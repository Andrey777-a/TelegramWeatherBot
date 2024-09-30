package ua.com.telegramweatherbot.bot;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.com.telegramweatherbot.config.BotProperties;
import ua.com.telegramweatherbot.service.CallbackQueryHandlerService;
import ua.com.telegramweatherbot.service.MessageHandlerService;
import ua.com.telegramweatherbot.service.impl.CallbackQueryHandlerServiceImpl;
import ua.com.telegramweatherbot.service.impl.MessageHandlerServiceImpl;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TelegramWeatherBot extends TelegramLongPollingBot {

    BotProperties botProperties;
    MessageHandlerService messageHandlerService;
    CallbackQueryHandlerService callbackQueryHandlerService;

    public TelegramWeatherBot(
            BotProperties botProperties,
            MessageHandlerServiceImpl messageHandlerService,
            CallbackQueryHandlerServiceImpl callbackQueryHandlerService
    ) {
        super(botProperties.getToken());
        this.botProperties = botProperties;
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
        return botProperties.getName();
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
