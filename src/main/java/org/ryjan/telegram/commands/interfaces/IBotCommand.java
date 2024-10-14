package org.ryjan.telegram.commands.interfaces;

import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;

public interface IBotCommand {

    void execute(String chatId, BotMain bot, CommandsHandler commandHandler);
}