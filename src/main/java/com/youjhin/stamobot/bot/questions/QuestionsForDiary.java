package com.youjhin.stamobot.bot.questions;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;


@Service
public class QuestionsForDiary {

    public int currentQuestion = 0;

    public SendMessage askQuestionOne(Long chatId) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonOne = new InlineKeyboardButton();
        buttonOne.setText("1");
        buttonOne.setCallbackData("1");

        InlineKeyboardButton buttonTwo = new InlineKeyboardButton();
        buttonTwo.setText("2");
        buttonTwo.setCallbackData("2");

        InlineKeyboardButton buttonThree = new InlineKeyboardButton();
        buttonThree.setText("3");
        buttonThree.setCallbackData("3");

        InlineKeyboardButton buttonFour = new InlineKeyboardButton();
        buttonFour.setText("4");
        buttonFour.setCallbackData("4");

        InlineKeyboardButton buttonFive = new InlineKeyboardButton();
        buttonFive.setText("5");
        buttonFive.setCallbackData("5");

        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        rowInLine.add(buttonOne);
        rowInLine.add(buttonTwo);
        rowInLine.add(buttonThree);
        rowInLine.add(buttonFour);
        rowInLine.add(buttonFive);


        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Как сильно болела голова, по шкале от 1 до 5?");

        message.setReplyMarkup(keyboardMarkup);

        setCurrentQuestion(1);

        return message;

    }

    public SendMessage askQuestionTwo(Long chatId) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonOne = new InlineKeyboardButton();
        buttonOne.setText("1");
        buttonOne.setCallbackData("Пульсирует");

        InlineKeyboardButton buttonTwo = new InlineKeyboardButton();
        buttonTwo.setText("2");
        buttonTwo.setCallbackData("Сжимает");

        InlineKeyboardButton buttonThree = new InlineKeyboardButton();
        buttonThree.setText("3");
        buttonThree.setCallbackData("Давит");

        InlineKeyboardButton buttonFour = new InlineKeyboardButton();
        buttonFour.setText("4");
        buttonFour.setCallbackData("Стреляет");

        InlineKeyboardButton buttonFive = new InlineKeyboardButton();
        buttonFive.setText("5");
        buttonFive.setCallbackData("Распирает");

        InlineKeyboardButton buttonSix = new InlineKeyboardButton();
        buttonSix.setText("6");
        buttonSix.setCallbackData("Сверлит");

        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        rowInLine.add(buttonOne);
        rowInLine.add(buttonTwo);
        rowInLine.add(buttonThree);
        rowInLine.add(buttonFour);
        rowInLine.add(buttonFive);
        rowInLine.add(buttonSix);


        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);


        message.setText("Характер головной боли?:\n\n" +
                "1. Пульсирует\n" +
                "2. Сжимает\n" +
                "3. Давит\n" +
                "4. Стреляет\n" +
                "5. Распирает\n" +
                "6. Сверлит");


        message.setReplyMarkup(keyboardMarkup);

        setCurrentQuestion(2);

        return message;

    }

    public SendMessage askQuestionThree(Long chatId) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonOne = new InlineKeyboardButton();
        buttonOne.setText("1");
        buttonOne.setCallbackData("Лобная");

        InlineKeyboardButton buttonTwo = new InlineKeyboardButton();
        buttonTwo.setText("2");
        buttonTwo.setCallbackData("Височная");

        InlineKeyboardButton buttonThree = new InlineKeyboardButton();
        buttonThree.setText("3");
        buttonThree.setCallbackData("Теменная");

        InlineKeyboardButton buttonFour = new InlineKeyboardButton();
        buttonFour.setText("4");
        buttonFour.setCallbackData("Затылочная");

        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        rowInLine.add(buttonOne);
        rowInLine.add(buttonTwo);
        rowInLine.add(buttonThree);
        rowInLine.add(buttonFour);

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);


        message.setText("В какой области болит?:\n\n" +
                "1. Лобная\n" +
                "2. Височная\n" +
                "3. Теменная\n" +
                "4. Затылочная\n");

        message.setReplyMarkup(keyboardMarkup);

        setCurrentQuestion(3);

        return message;

    }

    public SendMessage askQuestionFour(Long chatId) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonOne = new InlineKeyboardButton();
        buttonOne.setText("1");
        buttonOne.setCallbackData("Справа");

        InlineKeyboardButton buttonTwo = new InlineKeyboardButton();
        buttonTwo.setText("2");
        buttonTwo.setCallbackData("Слева");

        InlineKeyboardButton buttonThree = new InlineKeyboardButton();
        buttonThree.setText("3");
        buttonThree.setCallbackData("С 2х сторон");

        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        rowInLine.add(buttonOne);
        rowInLine.add(buttonTwo);
        rowInLine.add(buttonThree);

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);


        message.setText("С какой стороны болит голова?:\n\n" +
                "1. Справа\n" +
                "2. Слева\n" +
                "3. С 2х сторон\n");

        message.setReplyMarkup(keyboardMarkup);

        setCurrentQuestion(4);

        return message;

    }

    public SendMessage askQuestionFive(Long chatId) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonOne = new InlineKeyboardButton();
        buttonOne.setText("1");
        buttonOne.setCallbackData("Тошнота или рвота");

        InlineKeyboardButton buttonTwo = new InlineKeyboardButton();
        buttonTwo.setText("2");
        buttonTwo.setCallbackData("Раздражает свет/звук");

        InlineKeyboardButton buttonThree = new InlineKeyboardButton();
        buttonThree.setText("3");
        buttonThree.setCallbackData("Онемение");

        InlineKeyboardButton buttonFour = new InlineKeyboardButton();
        buttonFour.setText("4");
        buttonFour.setCallbackData("Усиливается при физ.активности");

        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        rowInLine.add(buttonOne);
        rowInLine.add(buttonTwo);
        rowInLine.add(buttonThree);
        rowInLine.add(buttonFour);

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);


        message.setText("Имеются ли у Вас какие-либо симптомы?:\n\n" +
                "1. Тошнота или рвота\n" +
                "2. Раздражает свет/звук\n" +
                "3. Онемение\n" +
                "4. Усиливается при физичекской активности\n");

        message.setReplyMarkup(keyboardMarkup);

        setCurrentQuestion(5);

        return message;

    }

    public SendMessage askQuestionSix(Long chatId) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonOne = new InlineKeyboardButton();
        buttonOne.setText("1");
        buttonOne.setCallbackData("Терплю");

        InlineKeyboardButton buttonTwo = new InlineKeyboardButton();
        buttonTwo.setText("2");
        buttonTwo.setCallbackData("Лекарствами");

        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        rowInLine.add(buttonOne);
        rowInLine.add(buttonTwo);


        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);


        message.setText("Чем снимаете приступ?:\n\n" +
                "1. Терплю (сон, отдых, прогулка)\n" +
                "2. Лекарствами\n");

        message.setReplyMarkup(keyboardMarkup);

        setCurrentQuestion(6);

        return message;
    }

    public SendMessage askQuestionSeven(Long chatId) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonOne = new InlineKeyboardButton();
        buttonOne.setText("1");
        buttonOne.setCallbackData("Пульсирует");

        InlineKeyboardButton buttonTwo = new InlineKeyboardButton();
        buttonTwo.setText("2");
        buttonTwo.setCallbackData("Сжимает");

        InlineKeyboardButton buttonThree = new InlineKeyboardButton();
        buttonThree.setText("3");
        buttonThree.setCallbackData("Давит");

        InlineKeyboardButton buttonFour = new InlineKeyboardButton();
        buttonFour.setText("4");
        buttonFour.setCallbackData("Стреляет");

        InlineKeyboardButton buttonFive = new InlineKeyboardButton();
        buttonFive.setText("5");
        buttonFive.setCallbackData("Распирает");

        InlineKeyboardButton buttonSix = new InlineKeyboardButton();
        buttonSix.setText("6");
        buttonSix.setCallbackData("Сверлит");

        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        rowInLine.add(buttonOne);
        rowInLine.add(buttonTwo);
        rowInLine.add(buttonThree);
        rowInLine.add(buttonFour);
        rowInLine.add(buttonFive);
        rowInLine.add(buttonSix);


        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        rowsInLine.add(rowInLine);

        keyboardMarkup.setKeyboard(rowsInLine);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);


        message.setText("Есть ли профилактика приступов?:\n\n" +
                "1. Моноклональные антитела\n" +
                "2. Cefaly\n" +
                "3. БОС\n" +
                "4. Массаж\n" +
                "5. Гимнастика\n" +
                "6. Прогулки\n" +
                "7. Сон\n" +
                "8. Нет");


        message.setReplyMarkup(keyboardMarkup);

        setCurrentQuestion(7);

        return message;

    }

    public int getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(int currentQuestion) {
        this.currentQuestion = currentQuestion;
    }
}
