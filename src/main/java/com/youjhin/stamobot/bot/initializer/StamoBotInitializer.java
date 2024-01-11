package com.youjhin.stamobot.bot.initializer;

import com.youjhin.stamobot.bot.StamoBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


/**
 * Компонент, отвечающий за инициализацию бота при старте приложения.
 */
@Slf4j
@Component
public class StamoBotInitializer {

    @Autowired
    StamoBot bot;

    /**
     * Инициализация бота при старте приложения.
     *
     * @throws TelegramApiException Исключение, возникающее при проблемах с регистрацией бота.
     */
    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        // Создание объекта TelegramBotsApi для регистрации бота
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            // Регистрация бота в Telegram
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            log.error("Ошибка создания бота и API: " + e.getMessage());
        }
    }
}
