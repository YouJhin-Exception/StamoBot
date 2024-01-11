package com.youjhin.stamobot.bot.comands;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, содержащий статический метод для получения списка команд бота.
 */
public class StamoBotCommands {

    /**
     * Приватный конструктор, чтобы предотвратить создание экземпляров класса.
     */
    private StamoBotCommands() { }

    /**
     * Получает список команд бота.
     *
     * @return Список объектов BotCommand, представляющих команды бота.
     */
    public static List<BotCommand> getCommandList() {
        List<BotCommand> commands = new ArrayList<>();

        // Добавление команд в список
        commands.add(new BotCommand(BotCommandsConstants.START, "Приветственное сообщение"));
        commands.add(new BotCommand(BotCommandsConstants.REG, "Регистрация в БД бота"));
        commands.add(new BotCommand(BotCommandsConstants.HEADACHE, "Ведение дневника головной боли"));

        return commands;
    }
}
