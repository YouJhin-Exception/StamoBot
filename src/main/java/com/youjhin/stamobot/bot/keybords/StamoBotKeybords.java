package com.youjhin.stamobot.bot.keybords;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class StamoBotKeybords {

    public static ReplyKeyboardMarkup createDefaultKeyboard() {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        row.add("да");
        row.add("нет");

        keyboardRowList.add(row);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        replyKeyboardMarkup.setResizeKeyboard(true);


        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup createAlternativeKeyboard() {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();


        return replyKeyboardMarkup;
    }
}
