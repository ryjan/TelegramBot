package org.ryjan.telegram.commands.interfaces;

import org.ryjan.telegram.handler.CommandHandler;
import org.ryjan.telegram.main.BotMain;

public interface IBotCommand<T extends CommandHandler> {

    void execute(String chatId, BotMain bot, T handler);
}