package com.youjhin.stamobot.bot.questions;

import com.youjhin.stamobot.bot.StamoBot;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;

/**
 * Сервис для формирования вопросов для дневника головной боли и создания сообщений с клавиатурой.
 */
@Service
public class QuestionsForDiary {
    /**
     * Генерирует вопрос с клавиатурой по номеру вопроса.
     *
     * @param chatId         Идентификатор чата пользователя.
     * @param questionNumber Номер вопроса.
     * @return Сообщение с клавиатурой для ответа на вопрос.
     */
    public SendMessage askQuestionByNumber(Long chatId, int questionNumber) {
        InlineKeyboardMarkup keyboardMarkup;

        return switch (questionNumber) {
            case 1 -> {
                keyboardMarkup = createKeyboard(
                        createRow(
                                createButton("1", "1"),
                                createButton("2", "2"),
                                createButton("3", "3"),
                                createButton("4", "4"),
                                createButton("5", "5")
                        )
                );
                yield createSendMessageWithKeyboard(chatId, "Как сильно болела голова, по шкале от 1 до 5?", keyboardMarkup);
            }
            case 2 -> {
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
                yield createSendMessageWithKeyboard(chatId, "Характер головной боли?", keyboardMarkup);
            }
            case 3 -> {
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
                yield createSendMessageWithKeyboard(chatId, "В какой области болит?", keyboardMarkup);
            }
            case 4 -> {
                keyboardMarkup = createKeyboard(
                        createRow(
                                createButton("Слева", "Слева"),
                                createButton("Справа", "Справа")
                        ),
                        createRow(
                                createButton("С 2х сторон", "С 2х сторон")
                        )
                );
                yield createSendMessageWithKeyboard(chatId, "С какой стороны болит голова?", keyboardMarkup);
            }
            case 5 -> {
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
                yield createSendMessageWithKeyboard(chatId, "Имеются ли у Вас какие-либо симптомы?", keyboardMarkup);
            }
            case 6 -> {
                keyboardMarkup = createKeyboard(
                        createRow(
                                createButton("Терплю (сон, отдых, прогулка)", "Терплю (сон, отдых, прогулка)")
                        ),
                        createRow(
                                createButton("Лекарствами", "Лекарствами")
                        )

                );
                yield createSendMessageWithKeyboard(chatId, "Чем снимаете приступ?", keyboardMarkup);
            }
            case 7 -> {
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
                yield createSendMessageWithKeyboard(chatId, "Есть ли профилактика приступов головной боли?", keyboardMarkup);
            }
            case 8 -> {
                keyboardMarkup = createKeyboard(
                        createRow(
                                createButton("Метопролол (беталок, эгилок)", "Метопролол (беталок, эгилок)")
                        ),
                        createRow(
                                createButton("Амитриптилин", "Амитриптилин"),
                                createButton("Венлафиксин", "Венлафиксин")

                        ),
                        createRow(
                                createButton("Топирамат", "Топирамат"),
                                createButton("Кандесартан", "Кандесартан")
                        ),
                        createRow(
                                createButton("Нет", "Нет")
                        )

                );
                yield createSendMessageWithKeyboard(chatId, "Принимаете ли Вы один из следующих препаратов?", keyboardMarkup);
            }
            default -> throw new IllegalStateException("неверный номер вопроса: " + questionNumber);
        };
    }
    /**
     * Создает кнопку для использования в клавиатуре.
     *
     * @param text         Текст на кнопке.
     * @param callbackData Данные, которые будут переданы при нажатии на кнопку.
     * @return Созданная кнопка.
     */
    private InlineKeyboardButton createButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    /**
     * Создает строку кнопок для использования в клавиатуре.
     *
     * @param buttons Массив кнопок.
     * @return Список кнопок в одной строке.
     */
    private List<InlineKeyboardButton> createRow(InlineKeyboardButton... buttons) {
        return Arrays.asList(buttons);
    }

    /**
     * Создает клавиатуру из строк кнопок.
     *
     * @param rows Массив строк кнопок.
     * @return Созданная клавиатура.
     */
    @SafeVarargs
    private InlineKeyboardMarkup createKeyboard(List<InlineKeyboardButton>... rows) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(Arrays.asList(rows));
        return keyboardMarkup;
    }

    /**
     * Создает сообщение с клавиатурой для отправки пользователю.
     *
     * @param chatId  Идентификатор чата пользователя.
     * @param text    Текст сообщения.
     * @param keyboard Клавиатура, прикрепленная к сообщению.
     * @return Созданное сообщение с клавиатурой.
     */
    private SendMessage createSendMessageWithKeyboard(Long chatId, String text, InlineKeyboardMarkup keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(keyboard);
        return message;
    }

}
