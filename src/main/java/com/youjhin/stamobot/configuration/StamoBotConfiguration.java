package com.youjhin.stamobot.configuration;


import com.youjhin.stamobot.bot.StamoBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class StamoBotConfiguration {

    @Bean
    public TelegramBotsApi telegramBotsApi(StamoBot stamoBot) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(stamoBot);
        return api;
    }

}
