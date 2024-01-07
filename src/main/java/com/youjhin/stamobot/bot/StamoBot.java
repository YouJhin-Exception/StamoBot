package com.youjhin.stamobot.bot;


import com.youjhin.stamobot.bot.comands.BotCommandsConstants;
import com.youjhin.stamobot.bot.comands.StamoBotCommands;
import com.youjhin.stamobot.bot.questions.QuestionsForDiary;
import com.youjhin.stamobot.bot.services.BotServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
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



    private int currenQuestion = 1;

    @Autowired
    private BotServices botServices;

    @Autowired
    private QuestionsForDiary questions;

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

                firstName = null;
                lastName = null;
                age = null;

            }
        }else if (update.hasCallbackQuery() && !waitAnswer ) {


//            String call_data = update.getCallbackQuery().getData();
//            long message_id = update.getCallbackQuery().getMessage().getMessageId();
//            long chat_id = update.getCallbackQuery().getMessage().getChatId();
//
//
//            List<String> answers = new ArrayList<>();
//            answers.add(call_data);

            CallbackQuery callbackQuery = update.getCallbackQuery();
            handleCallbackQuery(callbackQuery);






        }
        //log.info("сообщение отправлено пользователю: " + user);
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {

        Long chatId = callbackQuery.getMessage().getChatId();

        // ответы тут по нажатию
        String callbackData = callbackQuery.getData();

        switch (currenQuestion){
            case 1:
                currenQuestion++;
                sendNextQuestion(chatId,callbackQuery.getMessage().getMessageId());
                break;
            case 2:
                currenQuestion++;
                sendNextQuestion(chatId,callbackQuery.getMessage().getMessageId());
                break;
        }

    }

    private void sendNextQuestion(Long chatId, Integer messageId) {
        EditMessageText editMessageText;
        switch (currenQuestion){
            case 2:
                editMessageText = editQuestion(chatId,messageId,questions.askQuestionTwo(chatId));
                break;
            case 3:
                editMessageText = editQuestion(chatId,messageId,questions.askQuestionThree(chatId));
                break;
            default:
                editMessageText = new EditMessageText();
                editMessageText.setChatId(chatId);
                editMessageText.setMessageId(messageId);
                editMessageText.setText("Опрос завершен.");
                break;
        }

        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    private EditMessageText editQuestion(Long chatId, Integer messageId, SendMessage originalMessage) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setText(originalMessage.getText());
        editMessageText.setReplyMarkup((InlineKeyboardMarkup) originalMessage.getReplyMarkup());
        return editMessageText;
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



