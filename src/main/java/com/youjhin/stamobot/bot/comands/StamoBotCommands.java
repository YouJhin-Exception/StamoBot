package com.youjhin.stamobot.bot.comands;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

public class StamoBotCommands {
    private StamoBotCommands() { }

    public static List<BotCommand> getCommandList() { // статика чтоб не плодить лишних
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand(BotCommandsConstants.START,"приветственное сообщение"));
        commands.add(new BotCommand(BotCommandsConstants.HELP,"описание бота и команд"));
        commands.add(new BotCommand(BotCommandsConstants.HEADACHE,"ведение дневника головной боли"));
        return commands;
    }

}
