package com.youjhin.stamobot.bot.services;

import com.vdurmont.emoji.EmojiParser;
import com.youjhin.stamobot.bot.StamoBot;
import com.youjhin.stamobot.bot.model.UserRepository;
import com.youjhin.stamobot.bot.questions.QuestionsForDiary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Сервис для управления ботом и обработки команд пользователя.
 */
@Slf4j
@Service
public class BotServices {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private QuestionsForDiary questions;

    /**
     * Обрабатывает команду старта.
     *
     * @param bot     Экземпляр бота.
     * @param message Объект сообщения от пользователя.
     */
    public void startCommand(StamoBot bot, Message message) {
        String startMsg = EmojiParser.parseToUnicode("Здравствуйте, " + message.getChat().getFirstName() + "! \uD83D\uDC4B \n\n" +
                "Я бот \uD83E\uDD16 помогающий вести дневник головной боли. \n\n" +
                "Далее этот дневник будет отправляться врачу-цефалгологу для анализа Ваших мигреней.\n" +
                "Для начала зарегистрируйтесь через команду /reg \n" +
                "Далее, чтобы начать вводить Ваш дневник, воспользуйтесь командой /head в меню");

        sendMessage(bot, message.getChatId(), startMsg);
    }

    /**
     * Обрабатывает команду начала ввода данных о головной боли.
     *
     * @param bot     Экземпляр бота.
     * @param chatId  Идентификатор чата пользователя.
     * @param message Объект сообщения от пользователя.
     */
    public void headacheCommand(StamoBot bot, Long chatId, Message message) {
        if (userRepo.findById(message.getChatId()).isEmpty()) {
            sendMessage(bot, chatId, "Вы не зарегистрированы. Для ведения дневника зарегистрируйтесь через команду /reg \nили через кнопку Меню");
        } else {
            executeMessage(bot, questions.askQuestionByNumber(chatId, 1));
        }
    }

    /**
     * Обрабатывает неизвестную команду.
     *
     * @param bot     Экземпляр бота.
     * @param message Объект сообщения от пользователя.
     */
    public void unknownCommand(StamoBot bot, Message message) {
        String answer = EmojiParser.parseToUnicode("Не удалось распознать команду! :skull:");
        String an2 = EmojiParser.parseToUnicode(":ghost:");

        sendMessage(bot, message.getChatId(), answer);
        sendMessage(bot, message.getChatId(), an2);
    }

    /**
     * Отправляет сообщение пользователю.
     *
     * @param bot    Экземпляр бота.
     * @param chatId Идентификатор чата пользователя.
     * @param text   Текст сообщения.
     */
    public void sendMessage(StamoBot bot, Long chatId, String text) {
        SendMessage sm = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .build();
        executeMessage(bot, sm);
    }

    /**
     * Выполняет отправку сообщения через бота.
     *
     * @param bot     Экземпляр бота.
     * @param message Объект SendMessage для отправки.
     */
    public void executeMessage(StamoBot bot, SendMessage message) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error("ERROR_TEXT " + e.getMessage());
        }
    }
}

