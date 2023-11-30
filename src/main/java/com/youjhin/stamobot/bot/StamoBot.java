package com.youjhin.stamobot.bot;


import com.youjhin.stamobot.bot.comands.BotCommandsConstants;
import com.youjhin.stamobot.bot.comands.StamoBotCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Component
public class StamoBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;

    public StamoBot(@Value("${bot.token}") String botToken) {
        super(botToken);
        initializeMenu();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        var msg = update.getMessage();
        var user = msg.getFrom();
        var id = user.getId();

        switch (msg.getText()) {
            case BotCommandsConstants.START -> {
                // метод приветствия
                break;
            }

            case BotCommandsConstants.HELP -> {
                // метод для хелпа
                break;
            }

            case BotCommandsConstants.HEADACHE -> {
                /* метод для ведения дневника, нужна регистрация,
                 и различие между новым и кто уже заполняет */
                break;
            }
            default -> unknownCommand(msg.getChatId());
        }


        //sendText(id, msg.getText()); // echo

        log.info("сообщение отправлено пользователю: " + user); // test logger
    }

    public void sendMessage(Long chatId, String text) {

        SendMessage sm = SendMessage.builder()
                .chatId(chatId.toString()) //Who are we sending a message to
                .text(text).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    private void unknownCommand(Long chatId) {
        var text = "Не удалось распознать команду!";
        sendMessage(chatId, text);
    }


    // инициализация меню
    private void initializeMenu() {
        List<BotCommand> commands = StamoBotCommands.getCommandList();
        try {
            execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Ошибка создания меню: " + e.getMessage());
        }
    }
}
