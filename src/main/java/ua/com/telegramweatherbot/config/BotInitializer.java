package ua.com.telegramweatherbot.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ua.com.telegramweatherbot.bot.TelegramWeatherBot;

@Configuration
public class BotInitializer {

    @SneakyThrows
    @Bean
    public TelegramBotsApi init(TelegramWeatherBot bot){

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        telegramBotsApi.registerBot(bot);

        return telegramBotsApi;

    }

}
