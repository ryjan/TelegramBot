package org.ryjan.telegram.commands.interfaces;

import org.ryjan.telegram.handler.ButtonCommandHandler;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;

public interface IBotGroupCommand {
    void execute(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler);
}