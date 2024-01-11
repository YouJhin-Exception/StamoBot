package com.youjhin.stamobot.bot;


import com.youjhin.stamobot.bot.comands.BotCommandsConstants;
import com.youjhin.stamobot.bot.comands.StamoBotCommands;
import com.youjhin.stamobot.bot.services.BotServices;
import com.youjhin.stamobot.bot.services.HandleQuery;
import com.youjhin.stamobot.bot.services.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


/**
 * Класс, представляющий Telegram бота для ведения дневника головной боли.
 */
@Slf4j
@Component
public class StamoBot extends TelegramLongPollingBot {

    private String firstName;
    private String lastName;
    private String age;
    private boolean waitAnswer = false;
    private int regStep = 0;

    @Autowired
    private BotServices botServices;

    @Autowired
    private HandleQuery handleQuery;

    @Autowired
    private RegistrationService registrationService;

    @Value("${bot.name}")
    private String botName;

    /**
     * Конструктор для инициализации бота.
     *
     * @param botToken Токен бота.
     */
    public StamoBot(@Value("${bot.token}") String botToken) {
        super(botToken);
        initializeMenu();
    }

    /**
     * Обработка полученного обновления (сообщения, команды и т. д.).
     *
     * @param update Объект обновления.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText() && !waitAnswer) {
            var msg = update.getMessage();
            Long chatId = msg.getChatId();
            switch (msg.getText()) {
                case BotCommandsConstants.START -> // Метод приветствия
                        botServices.startCommand(this, msg);
                case BotCommandsConstants.REG -> {
                    // Регистрация пользователя в базе данных
                    botServices.sendMessage(this, chatId, "Введите Ваше имя");
                    waitAnswer = true;
                    regStep = 1;
                }
                case BotCommandsConstants.HEADACHE -> botServices.headacheCommand(this, chatId, msg); // вызов вопросов дневника
                default -> botServices.unknownCommand(this, msg);
            }
        } else if (update.hasMessage() && update.getMessage().hasText() && waitAnswer) {
            var msg = update.getMessage();
            Long chatId = msg.getChatId();
            if (regStep == 1) {
                firstName = msg.getText();
                botServices.sendMessage(this, chatId, "Введите Вашу фамилию");
                regStep = 2;
            } else if (regStep == 2) {
                lastName = msg.getText();
                botServices.sendMessage(this, chatId, "Введите Ваш возраст ");
                regStep = 3;
            } else if (regStep == 3) {
                age = msg.getText();
                registrationService.registerUser(this, firstName, lastName, age, msg);
                resetRegistration();
            }
        } else if (update.hasCallbackQuery() && !waitAnswer) {
            handleQuery.handleCallbackQuery(this, update.getCallbackQuery());
        }
        log.info("Сообщение отправлено пользователю: " + update.getMessage().getChat().getFirstName());
    }

    /**
     * Сброс переменных регистрации пользователя.
     */
    private void resetRegistration() {
        waitAnswer = false;
        regStep = 0;
        firstName = null;
        lastName = null;
        age = null;
    }

    /**
     * Получение имени бота.
     *
     * @return Имя бота.
     */
    @Override
    public String getBotUsername() {
        return botName;
    }

    /**
     * Инициализация меню бота.
     */
    private void initializeMenu() {
        List<BotCommand> commands = StamoBotCommands.getCommandList();
        try {
            execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Ошибка создания меню: " + e.getMessage());
        }
    }
}