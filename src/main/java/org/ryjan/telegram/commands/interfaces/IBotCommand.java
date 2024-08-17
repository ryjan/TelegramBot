package org.ryjan.telegram.commands.interfaces;

import org.ryjan.telegram.commands.utils.ButtonCommandHandler;
import org.ryjan.telegram.main.BotMain;

public interface IBotCommand {
    void execute(String chatId, BotMain bot, ButtonCommandHandler buttonCommandHandler);
}
