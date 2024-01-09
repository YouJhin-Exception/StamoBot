package com.youjhin.stamobot.bot.services;

import com.vdurmont.emoji.EmojiParser;
import com.youjhin.stamobot.bot.StamoBot;
import com.youjhin.stamobot.bot.questions.QuestionsForDiary;
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

    @Autowired
    private QuestionsForDiary questions;


    public void startCommand(StamoBot bot, Message message) {
        String startMsg = EmojiParser.parseToUnicode("Здравствуйте, " + message.getChat().getFirstName() + "! \uD83D\uDC4B \n\n" +
                "Я бот \uD83E\uDD16 помогающий вести дневник головной боли. \n\n" +
                "Далее этот дневник будет оправляться врачу-цефалгологу для анализа Ваших мигреней.\n" +
                "Для начала зарегистрируйтесь через команду /reg \n"+
                "Далее, для того чтобы начать ввести Ваш дневник воспользуйтесь командой /head в меню");

        sendMessage(bot, message.getChatId(), startMsg);
    }

    public void registerUser(StamoBot bot, String firstName, String lastName, String age, Message message) {

        if (userRepo.findById(message.getChatId()).isEmpty()) {

            registerInDb(firstName,lastName,age,message);

            String answer = EmojiParser.parseToUnicode("Регистрация прошла успешно! ✅");

            sendMessage(bot, message.getChatId(), answer);

            log.info("зарегистрированный пользователь: " + message.getChatId());

        }else sendMessage(bot, message.getChatId(), "Вы уже зарегистрированны");
    }


    private void registerInDb(String realName, String realLastName, String age, Message message){
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

    public void headacheCommand(StamoBot bot,Long chatId) {
        executeMessage(bot,questions.askQuestionByNumber(chatId,1));
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
        executeMessage(bot,sm);
    }



    public void executeMessage(StamoBot bot, SendMessage message){
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error("ERROR_TEXT " + e.getMessage());
        }
    }
}




