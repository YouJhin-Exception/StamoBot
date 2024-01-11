package com.youjhin.stamobot.bot.services;

import com.vdurmont.emoji.EmojiParser;
import com.youjhin.stamobot.bot.StamoBot;
import com.youjhin.stamobot.bot.model.User;
import com.youjhin.stamobot.bot.model.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.Timestamp;

/**
 * Сервис для регистрации пользователей.
 */
@Slf4j
@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BotServices botServices;

    /**
     * Регистрирует нового пользователя.
     *
     * @param bot        Экземпляр бота.
     * @param firstName  Имя пользователя.
     * @param lastName   Фамилия пользователя.
     * @param age        Возраст пользователя.
     * @param message    Объект сообщения от пользователя.
     */
    public void registerUser(StamoBot bot, String firstName, String lastName, String age, Message message) {
        // Проверка, не зарегистрирован ли уже пользователь с данным chatId
        if (userRepo.findById(message.getChatId()).isEmpty()) {
            // Регистрация в базе данных
            registerInDb(firstName, lastName, age, message);

            // Отправка сообщения о успешной регистрации
            String answer = EmojiParser.parseToUnicode("Регистрация прошла успешно! ✅");
            botServices.sendMessage(bot, message.getChatId(), answer);

            // Логирование успешной регистрации
            log.info("Зарегистрированный пользователь: " + message.getChatId());
        } else {
            // Отправка сообщения о том, что пользователь уже зарегистрирован
            botServices.sendMessage(bot, message.getChatId(), "Вы уже зарегистрированы");
        }
    }

    /**
     * Регистрирует пользователя в базе данных.
     *
     * @param realName      Настоящее имя пользователя.
     * @param realLastName  Настоящая фамилия пользователя.
     * @param age           Возраст пользователя.
     * @param message       Объект сообщения от пользователя.
     */
    private void registerInDb(String realName, String realLastName, String age, Message message) {
        var chatId = message.getChatId();
        var chat = message.getChat();

        // Создание объекта пользователя
        User user = new User();
        user.setChatId(chatId);
        user.setFirstName(chat.getFirstName());
        user.setLastName(chat.getLastName());
        user.setUserName(chat.getUserName());
        user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
        user.setLifeFirstName(realName);
        user.setLifeLastName(realLastName);
        user.setAge(age);

        // Сохранение пользователя в базе данных
        userRepo.save(user);
    }
}