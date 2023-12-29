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


    public void startCommand(StamoBot bot, Message message) {
        String startMsg = EmojiParser.parseToUnicode("Здравствуйте, " + message.getChat().getFirstName() + "! \uD83D\uDC4B \n\n" +
                "Я бот \uD83E\uDD16 помогающий вести дневник головной боли. \n\n" +
                "Далее этот дневник будет оправляться врачу-цефалгологу для анализа Ваших мигреней.\n" +
                "Для того чтобы начать ввести Ваш дневник воспользуйтесь командой /head в меню");

        sendMessage(bot, message.getChatId(), startMsg);
    }

    public void registerUser(StamoBot bot, Message message) {

        if (userRepo.findById(message.getChatId()).isEmpty()) {
            String realName = "Евгений";
            String realLastName = "Палевич";
            int age = 37;

            registerInDb(realName,realLastName,age,message);

            String answer = EmojiParser.parseToUnicode("Регистрация прошла успешно! ✅");

            sendMessage(bot, message.getChatId(), answer);

            log.info("зарегистрированный пользователь: " + message.getChatId());


        }else sendMessage(bot, message.getChatId(), "Вы уже зарегистрированны");


//        if (userRepo.findById(message.getChatId()).isEmpty()) {
//            var chatId = message.getChatId();
//            var chat = message.getChat();
//
//            //sendMessage(bot, chatId, "Введите ваше имя: ");
//
//            //String firstName = (получаем имя с сообщения в чате)
//
//            User user = new User();
//            user.setChatId(chatId);
//            user.setFirstName(chat.getFirstName());
//            user.setLastName(chat.getLastName());
//            user.setUserName(chat.getUserName());
//            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
//
//            //user.setLifeFirstName(firstName);
//
//            userRepo.save(user);
//
//            String answer = EmojiParser.parseToUnicode("Регистрация прошла успешно! ✅");
//
//            sendMessage(bot, chatId, answer);
//
//            log.info("зарегистрированный пользователь: " + user);
//        }


    }


    private void registerInDb(String realName, String realLastName, int age, Message message){
        var chatId = message.getChatId();
        var chat = message.getChat();

        User user = new User();
        user.setChatId(chatId);
        user.setFirstName(chat.getFirstName());
        user.setLastName(chat.getLastName());
        user.setUserName(chat.getUserName());
        user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
        user.setLifeFirstName(realName);
        user.setLifeLastName(realLastName);
        user.setAge(age);

        userRepo.save(user);
    }

    public void unknownCommand(StamoBot bot, Message message) {
        String answer = EmojiParser.parseToUnicode("Не удалось распознать команду! :skull:");
        String an2 = EmojiParser.parseToUnicode(":space_invader:");

        //sendMessageWithKey(bot, chatId, answer, StamoBotKeybords.createDefaultKeyboard());

        sendMessage(bot, message.getChatId(), answer);
        //sendMessage(bot, message.getChatId(), an2);
    }


    public void sendMessage(StamoBot bot, Long chatId, String text) {

        SendMessage sm = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .build();

        try {
            bot.execute(sm);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения: " + e.getMessage());
        }

    }


    public void headacheCommand(StamoBot stamoBot, Message message) {


    }


}




