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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class StamoBot extends TelegramLongPollingBot {
    private String firstName;
    private String lastName;
    private String age;
    private boolean waitAnswer = false;
    private int regStep = 0;

    @Autowired
    private BotServices botServices;

    @Value("${bot.name}")
    private String botName;

    public StamoBot(@Value("${bot.token}") String botToken) {
        super(botToken);

        initializeMenu();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText() && !waitAnswer) {

            var msg = update.getMessage();
            Long chatId = msg.getChatId();

            // Обработка обычных сообщений или других команд
            switch (msg.getText()) {
                case BotCommandsConstants.START -> {
                    // метод приветствия
                    botServices.startCommand(this, msg);

                }
                case BotCommandsConstants.REG -> {
                    //регистрация пользователя в базе данных
                    botServices.sendMessage(this, chatId, "Введите Ваше имя");
                    waitAnswer = true;
                    regStep = 1;
                }
                case BotCommandsConstants.HEADACHE -> {

                    botServices.headacheCommand(this,chatId);

                }

                default -> botServices.unknownCommand(this, msg);
            }
        }else if (update.hasMessage() && update.getMessage().hasText() && waitAnswer) {

            var msg = update.getMessage();
            Long chatId = msg.getChatId();


            if (regStep == 1) {
                firstName = msg.getText();
                botServices.sendMessage(this, chatId, "Введите Вашу фамилию");
                regStep = 2;

            } else if (regStep == 2) {
                lastName = msg.getText();
                botServices.sendMessage(this, chatId, "Введите Ваш возраст ");
                regStep = 3;

            } else if (regStep == 3) {
                age = msg.getText();

                botServices.registerUser(this,firstName,lastName,age,msg);

                waitAnswer = false;
                regStep = 0;
            }
        }else if (update.hasCallbackQuery() && !waitAnswer ) {

            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            //botServices.sendMessage(this,chat_id,"Hi");

            if(call_data.equals("YES")){
                String text = "You pressed YES button";
                executeEditMessageText(text, chat_id, message_id);
            }
            else if(call_data.equals("NO")){
                String text = "You pressed NO button";
                executeEditMessageText(text, chat_id, message_id);
            }

        }
        //log.info("сообщение отправлено пользователю: " + user);
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
    @Override
    public String getBotUsername() {
        return botName;
    }

    private void executeEditMessageText(String text, long chatId, long messageId){
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }




}



