package org.ryjan.telegram.commands.interfaces;

import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;

public interface IBotCommand {

    void execute(String chatId, BotMain bot, GroupCommandHandler commandHandler);
}