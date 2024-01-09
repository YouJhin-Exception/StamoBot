package com.youjhin.stamobot.bot.services;


import com.youjhin.stamobot.bot.StamoBot;
import com.youjhin.stamobot.bot.questions.QuestionsForDiary;
import com.youjhin.stamobot.model.HeadDiary;
import com.youjhin.stamobot.model.HeadDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class HandleQuery {

    @Autowired
    private HeadDiaryRepository diaryRepo;

    @Autowired
    private QuestionsForDiary questions;
    private int currentQuestion = 1;

    List<String> answers = new ArrayList<>();

    public void handleCallbackQuery(StamoBot bot, CallbackQuery callbackQuery) {

        // ответы тут по нажатию
        String callbackData = callbackQuery.getData();
        answers.add(callbackData);

        Long chatId = callbackQuery.getMessage().getChatId();
        sendNextQuestion(bot,chatId,callbackQuery.getMessage().getMessageId());

    }

    private void sendNextQuestion(StamoBot bot, Long chatId, Integer messageId) {

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


                saveSurvey(chatId);


                System.out.println(answers.toString());
                answers.clear();
                break;
        }
        try {
            bot.execute(editMessageText);
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

    private void saveSurvey(Long chatId){

        HeadDiary diary = new HeadDiary();

        diary.setChatId(chatId);
        diary.setDateOfFilling(new Timestamp(System.currentTimeMillis()));

        diaryRepo.save(diary);



    }

}
