package com.youjhin.stamobot.bot;


import com.youjhin.stamobot.bot.comands.BotCommandsConstants;
import com.youjhin.stamobot.bot.comands.StamoBotCommands;
import com.youjhin.stamobot.bot.services.BotServices;
import com.youjhin.stamobot.bot.services.HandleQuery;
import com.youjhin.stamobot.bot.services.RegistrationService;
import com.youjhin.stamobot.bot.model.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Класс StamoBot представляет собой Telegram-бота, расширяющего TelegramLongPollingBot.
 * Реализует обработку полученных обновлений и управление регистрацией пользователей.
 */
@Slf4j
@Component
public class StamoBot extends TelegramLongPollingBot {

    // Пул потоков для обработки обновлений
    private final ExecutorService executor = Executors.newCachedThreadPool();

    // Коллекция для хранения сессий пользователей
    private final Map<Long, UserSession> userSessions = new HashMap<>();

    private final BotServices botServices;
    private final HandleQuery handleQuery;
    private final RegistrationService registrationService;

    @Value("${bot.name}")
    private String botName;

    /**
     * Конструктор для инициализации бота.
     *
     * @param botToken Токен бота.
     */
    public StamoBot(@Value("${bot.token}") String botToken, BotServices botServices, HandleQuery handleQuery, RegistrationService registrationService) {
        super(botToken);
        initializeMenu();
        this.botServices = botServices;
        this.handleQuery = handleQuery;
        this.registrationService = registrationService;
    }

    /**
     * Обработка полученного обновления (сообщения, команды и т. д.).
     *
     * @param update Объект обновления.
     */
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            executor.submit(() -> {
                Long userId = update.getMessage().getFrom().getId();
                UserSession session = userSessions.computeIfAbsent(userId, k -> new UserSession());
                handleUpdate(update, session);

            });

        } else if (update.hasCallbackQuery()) {

            handleQuery.handleCallbackQuery(this, update.getCallbackQuery());
        }

        log.info("Сообщение отправлено пользователю: " + update.getMessage().getChat().getFirstName());
    }

    // Дополнительная обработка обновлений в зависимости от текстового сообщения
    private void handleUpdate(Update update, UserSession session) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (update.getMessage().getText()) {
                case BotCommandsConstants.START -> {
                    botServices.startCommand(this, update.getMessage());

                }
                case BotCommandsConstants.REG -> {
                    botServices.sendMessage(this, update.getMessage().getChatId(), "Введите Ваше имя");
                    session.setWaitAnswer(true);
                    session.setRegStep(1);

                }
                case BotCommandsConstants.HEADACHE -> {

                    CompletableFuture.runAsync(() -> botServices.headacheCommand(this, update.getMessage().getChatId(), update.getMessage()), executor);

                }
                default -> {
                    if (session.isWaitAnswer()) {
                        // обработка ответов в процессе регистрации
                        handleRegistrationAnswer(update, session);
                    } else {
                        botServices.unknownCommand(this, update.getMessage());
                    }
                }

            }
        }
    }
    // Обработка ответов в процессе регистрации
    private void handleRegistrationAnswer(Update update, UserSession session) {
        var msg = update.getMessage();
        Long chatId = msg.getChatId();

        try {
            switch (session.getRegStep()) {
                case 1:
                    session.setFirstName(msg.getText());
                    botServices.sendMessage(this, chatId, "Введите Вашу фамилию");
                    session.setRegStep(2);
                    break;
                case 2:
                    session.setLastName(msg.getText());
                    botServices.sendMessage(this, chatId, "Введите Ваш возраст");
                    session.setRegStep(3);
                    break;
                case 3:
                    session.setAge(msg.getText());
                    registrationService.registerUser(this, session.getFirstName(), session.getLastName(),
                            session.getAge(), msg);
                    resetRegistration(session);
                    break;
                default:
                    // Обработка ошибки: Неизвестный шаг регистрации
                    log.error("Неизвестный шаг регистрации: " + session.getRegStep());
            }
        } catch (Exception e) {
            // Обработка ошибки: Логирование и/или отправка уведомления об ошибке
            log.error("Ошибка обработки регистрации пользователя", e);
            botServices.sendMessage(this, chatId, "Произошла ошибка при обработке вашей регистрации. Пожалуйста, повторите попытку позже.");
        }
    }

    /**
     * Сброс переменных регистрации пользователя.
     */
    private void resetRegistration(UserSession session) {
        session.setWaitAnswer(false);
        session.setRegStep(0);
        session.setFirstName(null);
        session.setLastName(null);
        session.setAge(null);
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