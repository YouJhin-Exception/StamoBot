package com.youjhin.stamobot.bot.services;

import com.vdurmont.emoji.EmojiParser;
import com.youjhin.stamobot.bot.StamoBot;
import com.youjhin.stamobot.model.User;
import com.youjhin.stamobot.model.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;

@Slf4j
@Service
public class BotServices {

    @Autowired
    private UserRepository userRepo;

    public void registerUser(StamoBot bot, Message message) {
        if (userRepo.findById(message.getChatId()).isEmpty()) {
            var chatId = message.getChatId();
            var chat = message.getChat();

            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            userRepo.save(user);

            String answer = EmojiParser.parseToUnicode("Регистрация прошла успешно! ✅");
            sendMessage(bot, chatId, answer);
            log.info("зарегистрированный пользователь: " + user);
        }
    }

    public void unknownCommand(StamoBot bot, Long chatId) {
        String answer = EmojiParser.parseToUnicode("Не удалось распознать команду! :skull:");
        String an2 = EmojiParser.parseToUnicode(":space_invader:");

        //var text = "Не удалось распознать команду!";
        sendMessage(bot, chatId, answer);
        sendMessage(bot, chatId,an2);
    }

    public void sendMessage(StamoBot bot, Long chatId, String text) {
        SendMessage sm = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text).build();

        try {
            bot.execute(sm);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения: " + e.getMessage());
        }
    }
}
