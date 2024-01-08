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
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


@Slf4j
@Component
public class StamoBot extends TelegramLongPollingBot {
    private String firstName;
    private String lastName;
    private String age;
    private boolean waitAnswer = false;
    private int regStep = 0;
    private int currentQuestion = 1;

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
        if (update.hasMessage()){
            handleMessage(update.getMessage());
        } else if (update.hasCallbackQuery()&& !waitAnswer) {
            handleCallbackQuery(update.getCallbackQuery());
        }


//        if (update.hasMessage() && update.getMessage().hasText() && !waitAnswer) {
//
//            var msg = update.getMessage();
//            Long chatId = msg.getChatId();
//
//            // Обработка обычных сообщений или других команд
//            switch (msg.getText()) {
//                case BotCommandsConstants.START -> {
//                    // метод приветствия
//                    botServices.startCommand(this, msg);
//
//                }
//                case BotCommandsConstants.REG -> {
//                    //регистрация пользователя в базе данных
//                    botServices.sendMessage(this, chatId, "Введите Ваше имя");
//                    waitAnswer = true;
//                    regStep = 1;
//                }
//                case BotCommandsConstants.HEADACHE -> {
//
//                    botServices.headacheCommand(this,chatId);
//
//                }
//
//                default -> botServices.unknownCommand(this, msg);
//            }
//        }else if (update.hasMessage() && update.getMessage().hasText() && waitAnswer) {
//
//            var msg = update.getMessage();
//            Long chatId = msg.getChatId();
//
//
//            if (regStep == 1) {
//                firstName = msg.getText();
//                botServices.sendMessage(this, chatId, "Введите Вашу фамилию");
//                regStep = 2;
//
//            } else if (regStep == 2) {
//                lastName = msg.getText();
//                botServices.sendMessage(this, chatId, "Введите Ваш возраст ");
//                regStep = 3;
//
//            } else if (regStep == 3) {
//                age = msg.getText();
//
//                botServices.registerUser(this,firstName,lastName,age,msg);
//
//                waitAnswer = false;
//                regStep = 0;
//
//                firstName = null;
//                lastName = null;
//                age = null;
//
//            }
//        }else if (update.hasCallbackQuery() && !waitAnswer ) {
//
//
////            String call_data = update.getCallbackQuery().getData();
////            long message_id = update.getCallbackQuery().getMessage().getMessageId();
////            long chat_id = update.getCallbackQuery().getMessage().getChatId();
////
////
////            List<String> answers = new ArrayList<>();
////            answers.add(call_data);
//
//            CallbackQuery callbackQuery = update.getCallbackQuery();
//            handleCallbackQuery(callbackQuery);
//        }
//        //log.info("сообщение отправлено пользователю: " + user);
    }


    private void handleMessage(Message message){
        Long chatId = message.getChatId();
        String messageText = message.getText();

        switch (messageText){
            case BotCommandsConstants.START:
                botServices.startCommand(this,message);
                break;
            case BotCommandsConstants.REG:
                handleRegistration(chatId,message);
                break;
            case BotCommandsConstants.HEADACHE:
                botServices.headacheCommand(this,chatId);
                break;
            default:
                botServices.unknownCommand(this,message);

        }
    }

    private void handleRegistration(Long chatId, Message message){
        switch (regStep){
            case 0:
                botServices.sendMessage(this, chatId, "Введите Ваше имя");
                waitAnswer = true;
                regStep = 1;
                break;
            case 1:
                firstName = message.getText();
                botServices.sendMessage(this, chatId, "Введите Вашу фамилию");
                regStep = 2;
                break;
            case 2:
                lastName = message.getText();
                botServices.sendMessage(this, chatId, "Введите Ваш возраст");
                regStep = 3;
                break;
            case 3:
                age = message.getText();
                botServices.registerUser(this, firstName, lastName, age, message);
                resetRegistration();
                break;
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {

        Long chatId = callbackQuery.getMessage().getChatId();
        sendNextQuestion(chatId,callbackQuery.getMessage().getMessageId());

        // ответы тут по нажатию
        String callbackData = callbackQuery.getData();

//        switch (currenQuestion){
//            case 1:
//                currenQuestion++;
//                sendNextQuestion(chatId,callbackQuery.getMessage().getMessageId());
//                break;
//            case 2:
//                currenQuestion++;
//                sendNextQuestion(chatId,callbackQuery.getMessage().getMessageId());
//                break;
//            case 3:
//                currenQuestion++;
//                sendNextQuestion(chatId,callbackQuery.getMessage().getMessageId());
//                break;
//            case 4:
//                currenQuestion++;
//                sendNextQuestion(chatId,callbackQuery.getMessage().getMessageId());
//                break;
//            case 5:
//                currenQuestion++;
//                sendNextQuestion(chatId,callbackQuery.getMessage().getMessageId());
//                break;
//            case 6:
//                currenQuestion++;
//                sendNextQuestion(chatId,callbackQuery.getMessage().getMessageId());
//                break;
//            case 7:
//                currenQuestion++;
//                sendNextQuestion(chatId,callbackQuery.getMessage().getMessageId());
//                break;
//        }

    }

    private void sendNextQuestion(Long chatId, Integer messageId) {

        EditMessageText editMessageText;
        switch (currentQuestion){
            case 1,2,3,4,5,6:
                currentQuestion++;
                editMessageText = editQuestion(chatId,messageId,questions.askQuestionByNumber(chatId, currentQuestion));
                break;
            default:
                editMessageText = new EditMessageText();
                editMessageText.setChatId(chatId);
                editMessageText.setMessageId(messageId);
                editMessageText.setText("Опрос завершен. \uD83D\uDE31");
                currentQuestion = 1;
                break;





//        EditMessageText editMessageText;
//        switch (currenQuestion){
//            case 2,3,4,5,6,7: // 1 ??
//                currenQuestion++;
//                editMessageText = editQuestion(chatId,messageId,questions.askQuestionByNumber(chatId,currenQuestion);
//                break;
//            case 3:
//                editMessageText = editQuestion(chatId,messageId,questions.askQuestionThree(chatId));
//                break;
//            case 4:
//                editMessageText = editQuestion(chatId,messageId,questions.askQuestionFour(chatId));
//                break;
//            case 5:
//                editMessageText = editQuestion(chatId,messageId,questions.askQuestionFive(chatId));
//                break;
//            case 6:
//                editMessageText = editQuestion(chatId,messageId,questions.askQuestionSix(chatId));
//                break;
//            case 7:
//                editMessageText = editQuestion(chatId,messageId,questions.askQuestionSeven(chatId));
//                break;
//            default:
//                editMessageText = new EditMessageText();
//                editMessageText.setChatId(chatId);
//                editMessageText.setMessageId(messageId);
//                editMessageText.setText("Опрос завершен. \uD83D\uDE31");
//                currenQuestion = 1;
//                break;
        }
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
    private void resetRegistration() {
        waitAnswer = false;
        regStep = 0;
        firstName = null;
        lastName = null;
        age = null;
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


    private EditMessageText editQuestion(Long chatId, Integer messageId, SendMessage originalMessage) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setText(originalMessage.getText());
        editMessageText.setReplyMarkup((InlineKeyboardMarkup) originalMessage.getReplyMarkup());
        return editMessageText;
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



