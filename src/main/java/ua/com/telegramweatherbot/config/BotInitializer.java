package ua.com.telegramweatherbot.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ua.com.telegramweatherbot.bot.TelegramBotService;


@Configuration
@RequiredArgsConstructor
public class BotInitializer {

    private final TelegramBotService bot;

    @SneakyThrows
    @EventListener({ContextRefreshedEvent.class})
    public void init(){

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        telegramBotsApi.registerBot(bot);

    }

}
