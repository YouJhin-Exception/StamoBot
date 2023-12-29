package com.youjhin.stamobot.bot;


import com.youjhin.stamobot.bot.comands.BotCommandsConstants;
import com.youjhin.stamobot.bot.comands.StamoBotCommands;
import com.youjhin.stamobot.bot.services.BotServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


@Slf4j
@Component
public class StamoBot extends TelegramLongPollingBot {

    @Autowired
    private BotServices botServices;

    long chatIdForName;

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
        Long chatId = msg.getChatId();




        switch (msg.getText()) {

            case BotCommandsConstants.START -> {

                // метод приветствия
                botServices.startCommand(this, msg);

                //регистрация пользователя в базе данных
                botServices.registerUser(this, msg);

            }

            case BotCommandsConstants.HELP -> {


                chatIdForName = chatId;

                // метод для хелпа
                botServices.sendMessage(this, chatId, "Ваше имя-");



            }

            case BotCommandsConstants.HEADACHE -> {

                //botServices.headacheCommand(this, update.getMessage());

                /* метод для ведения дневника, нужна регистрация,
                 и различие между новым и кто уже заполняет */
                break;
            }
            default -> {
                if (chatId == chatIdForName){
                    String name = msg.getText();
                    botServices.sendMessage(this, chatId, "Привет, " + name);
                } else{
                    botServices.unknownCommand(this, msg);
                }
            }

        }

        //botServices.sendMessage(this, id, msg.getText());// echo

        log.info("сообщение отправлено пользователю: " + user);
    }

    // инициализация меню
    private void initializeMenu() {
        List<BotCommand> commands = StamoBotCommands.getCommandList();
        try {
            execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
            //execute(new DeleteMyCommands());
        } catch (TelegramApiException e) {
            log.error("Ошибка создания меню: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

}
