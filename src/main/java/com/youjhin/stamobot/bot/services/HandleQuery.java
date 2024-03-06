package com.youjhin.stamobot.bot.services;

import com.youjhin.stamobot.bot.StamoBot;
import com.youjhin.stamobot.bot.questions.QuestionsForDiary;
import com.youjhin.stamobot.bot.model.HeadDiary;
import com.youjhin.stamobot.bot.model.HeadDiaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Сервисный класс для обработки запросов, связанных с опросом.
 */
@Slf4j
@Service
public class HandleQuery {

    private static final int MAX_QUESTION_NUMBER = 7;
    private static final int MAX_ANSWERS_NEED = 8;

    @Autowired
    private HeadDiaryRepository diaryRepo;

    @Autowired
    private QuestionsForDiary questions;

    @Autowired
    private BotServices botServices;


    // для каждого пользователя по уникальным chatId свои ответы и текущий вопрос
    private final Map<Long, Integer> currentQuestions = new HashMap<>();
    private final Map<Long, List<String>> userAnswers = new HashMap<>();

    /**
     * Обрабатывает callback-запрос, полученный от пользователя.
     *
     * @param bot           Экземпляр бота Telegram.
     * @param callbackQuery Полученный callback-запрос.
     */
    public void handleCallbackQuery(StamoBot bot, CallbackQuery callbackQuery) {

        Long chatId = callbackQuery.getMessage().getChatId();

        // Получаем текущий вопрос для пользователя или устанавливаем первый, если его нет
        int currentQuestion = currentQuestions.getOrDefault(chatId, 1);

        // Используем CompletableFuture для асинхронной обработки callback-запроса
        CompletableFuture.runAsync(() -> {
            // Ответы собираются при нажатии кнопок
            String callbackData = callbackQuery.getData();

            // Получаем список ответов пользователя или создаем новый, если его нет
            List<String> answers = userAnswers.computeIfAbsent(chatId, k -> new ArrayList<>());
            answers.add(callbackData);

            sendNextQuestion(bot, chatId, callbackQuery.getMessage().getMessageId(), currentQuestion);
        });
    }

    /**
     * Отправляет следующий вопрос опроса в зависимости от номера текущего вопроса.
     *
     * @param bot       Экземпляр бота Telegram.
     * @param chatId    Идентификатор чата.
     * @param messageId Идентификатор сообщения.
     */
    private void sendNextQuestion(StamoBot bot, Long chatId, Integer messageId, int currentQuestion) {
        EditMessageText editMessageText;

        if (currentQuestion >= 1 && currentQuestion <= MAX_QUESTION_NUMBER) {
            // Переход к следующему вопросу и редактирование сообщения
            currentQuestion++;
            currentQuestions.put(chatId, currentQuestion);
            editMessageText = editQuestion(chatId, messageId, questions.askQuestionByNumber(chatId, currentQuestion));
        } else {
            // Опрос завершен, отправка сообщения об окончании, сохранение опроса и сброс переменных
            editMessageText = new EditMessageText();
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);
            editMessageText.setText("Опрос завершен. \uD83D\uDE31");
            currentQuestions.remove(chatId);

            saveSurvey(chatId, bot);

            userAnswers.remove(chatId);
        }

        try {
            bot.execute(editMessageText);
        } catch (TelegramApiException e) {
            log.error("Ошибка в отправке следующего вопроса" + e.getMessage());
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
    private void saveSurvey(Long chatId, StamoBot bot) {
        List<String> answers = userAnswers.get(chatId);

        if (answers != null && answers.size() == MAX_ANSWERS_NEED) {
            HeadDiary diary = new HeadDiary();

            diary.setChatId(chatId);
            diary.setDateOfFilling(new Timestamp(System.currentTimeMillis()));
            diary.setHowMuchHurtScale(answers.get(0));
            diary.setNatureHeadache(answers.get(1));
            diary.setAreaHurt(answers.get(2));
            diary.setWerePain(answers.get(3));
            diary.setSymptoms(answers.get(4));
            diary.setRelieveAttack(answers.get(5));
            diary.setPreventionHeadache(answers.get(6));
            diary.setTakeMedication(answers.get(7));

            diaryRepo.save(diary);
        } else {
            // Обработка случая, когда не все ответы получены (это может произойти, если опрос завершился раньше)
            // Добавлено логирование и отправка уведомления
            log.error("Не все ответы получены для сохранения опроса. ChatId: " + chatId);
            botServices.sendMessage(bot, chatId, "Извините, не удалось сохранить опрос. Не все ответы получены. Пожалуйста, повторите опрос.");
        }
    }

}