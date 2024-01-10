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

/**
 * Сервисный класс для обработки запросов, связанных с опросом.
 */
@Service
public class HandleQuery {

    @Autowired
    private HeadDiaryRepository diaryRepo;

    @Autowired
    private QuestionsForDiary questions;
    private int currentQuestion = 1;
    List<String> answers = new ArrayList<>();

    /**
     * Обрабатывает callback-запрос, полученный от пользователя.
     *
     * @param bot           Экземпляр бота Telegram.
     * @param callbackQuery Полученный callback-запрос.
     */
    public void handleCallbackQuery(StamoBot bot, CallbackQuery callbackQuery) {

        // Ответы собираются при нажатии кнопок
        String callbackData = callbackQuery.getData();
        answers.add(callbackData);

        Long chatId = callbackQuery.getMessage().getChatId();
        sendNextQuestion(bot, chatId, callbackQuery.getMessage().getMessageId());
    }

    /**
     * Отправляет следующий вопрос опроса в зависимости от номера текущего вопроса.
     *
     * @param bot       Экземпляр бота Telegram.
     * @param chatId    Идентификатор чата.
     * @param messageId Идентификатор сообщения.
     */
    private void sendNextQuestion(StamoBot bot, Long chatId, Integer messageId) {
        EditMessageText editMessageText;

        if (currentQuestion >= 1 && currentQuestion <= 7) {
            // Переход к следующему вопросу и редактирование сообщения
            currentQuestion++;
            editMessageText = editQuestion(chatId, messageId, questions.askQuestionByNumber(chatId, currentQuestion));
        } else {
            // Опрос завершен, отправка сообщения об окончании, сохранение опроса и сброс переменных
            editMessageText = new EditMessageText();
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);
            editMessageText.setText("Опрос завершен. \uD83D\uDE31");
            currentQuestion = 1;

            saveSurvey(chatId);

            answers.clear();
        }

        try {
            bot.execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Редактирует сообщение с вопросом с обновленным содержанием.
     *
     * @param chatId          Идентификатор чата.
     * @param messageId       Идентификатор сообщения.
     * @param originalMessage Оригинальное сообщение для редактирования.
     * @return Отредактированное сообщение.
     */
    private EditMessageText editQuestion(Long chatId, Integer messageId, SendMessage originalMessage) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setText(originalMessage.getText());
        editMessageText.setReplyMarkup((InlineKeyboardMarkup) originalMessage.getReplyMarkup());
        return editMessageText;
    }

    /**
     * Сохраняет собранные данные опроса в базу данных.
     *
     * @param chatId Идентификатор чата.
     */
    private void saveSurvey(Long chatId) {

        HeadDiary diary = new HeadDiary();

        diary.setChatId(chatId);
        diary.setDateOfFilling(new Timestamp(System.currentTimeMillis()));
        diary.setHowMuchHurtScale(answers.getFirst());
        diary.setNatureHeadache(answers.get(1));
        diary.setAreaHurt(answers.get(2));
        diary.setWerePain(answers.get(3));
        diary.setSymptoms(answers.get(4));
        diary.setRelieveAttack(answers.get(5));
        diary.setPreventionHeadache(answers.get(6));
        diary.setTakeMedication(answers.get(7));

        diaryRepo.save(diary);
    }
}