package com.youjhin.stamobot.bot.questions;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.List;


@Service
public class QuestionsForDiary {
    public SendMessage askQuestionByNumber(Long chatId, int questionNumber) {
        InlineKeyboardMarkup keyboardMarkup;

        switch (questionNumber) {
            case 1:
                keyboardMarkup = createKeyboard(
                        createRow(
                                createButton("1", "1"),
                                createButton("2", "2"),
                                createButton("3", "3"),
                                createButton("4", "4"),
                                createButton("5", "5")
                        )
                );
                return createSendMessageWithKeyboard(chatId, "Как сильно болела голова, по шкале от 1 до 5?", keyboardMarkup);

            case 2:
                keyboardMarkup = createKeyboard(
                        createRow(
                                createButton("Пульсирует", "Пульсирует"),
                                createButton("Сжимает", "Сжимает"),
                                createButton("Давит", "Давит")
                        ),
                        createRow(
                                createButton("Стреляет", "Стреляет"),
                                createButton("Распирает", "Распирает"),
                                createButton("Сверлит", "Сверлит")
                        )
                );
                return createSendMessageWithKeyboard(chatId, "Характер головной боли?", keyboardMarkup);

            case 3:
                keyboardMarkup = createKeyboard(
                        createRow(
                                createButton("Лобная", "Лобная"),
                                createButton("Височная", "Височная")
                        ),
                        createRow(
                                createButton("Теменная", "Теменная"),
                                createButton("Затылочная", "Затылочная")
                        )
                );
                return createSendMessageWithKeyboard(chatId, "В какой области болит?", keyboardMarkup);
            case 4:
                keyboardMarkup = createKeyboard(
                        createRow(
                                createButton("Слева", "Слева"),
                                createButton("Справа", "Справа")
                        ),
                        createRow(
                                createButton("С 2х сторон", "С 2х сторон")
                        )
                );
                return createSendMessageWithKeyboard(chatId, "С какой стороны болит голова?", keyboardMarkup);
            case 5:
                keyboardMarkup = createKeyboard(
                        createRow(
                                createButton("Тошнота или рвота", "Тошнота или рвота")
                        ),
                        createRow(
                                createButton("Раздражает свет/звук", "Раздражает свет/звук")
                        ),
                        createRow(
                                createButton("Онемение", "Онемение")
                        ),
                        createRow(
                                createButton("Усиливается при физ.активности", "Усиливается при физ.активности")
                        )

                );
                return createSendMessageWithKeyboard(chatId, "Имеются ли у Вас какие-либо симптомы?", keyboardMarkup);
            case 6:
                keyboardMarkup = createKeyboard(
                        createRow(
                                createButton("Терплю (сон, отдых, прогулка)", "Терплю (сон, отдых, прогулка)")
                        ),
                        createRow(
                                createButton("Лекарствами", "Лекарствами")
                        )

                );
                return createSendMessageWithKeyboard(chatId, "Чем снимаете приступ?", keyboardMarkup);
            case 7:
                keyboardMarkup = createKeyboard(
                        createRow(
                                createButton("Моноклональные антитела", "Моноклональные антитела")
                        ),
                        createRow(
                                createButton("Cefaly", "Cefaly"),
                                createButton("БОС", "БОС"),
                                createButton("Массаж", "Массаж")
                        ),
                        createRow(
                                createButton("Сон", "Сон"),
                                createButton("Гимнастика", "Гимнастика"),
                                createButton("Прогулки", "Прогулки")
                        ),
                        createRow(
                                createButton("Нет", "Нет")
                        )

                );
                return createSendMessageWithKeyboard(chatId, "Есть ли профилактика приступов головной боли?", keyboardMarkup);

            default:
                // В случае неизвестного номера вопроса возвращаем пустоту (может переделать на пустое сообщение ?)
                return null;
        }
    }

    private InlineKeyboardButton createButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    private List<InlineKeyboardButton> createRow(InlineKeyboardButton... buttons) {
        return Arrays.asList(buttons);
    }

    @SafeVarargs // from idea
    private InlineKeyboardMarkup createKeyboard(List<InlineKeyboardButton>... rows) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(Arrays.asList(rows));
        return keyboardMarkup;
    }

    private SendMessage createSendMessageWithKeyboard(Long chatId, String text, InlineKeyboardMarkup keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(keyboard);
        return message;
    }

}
