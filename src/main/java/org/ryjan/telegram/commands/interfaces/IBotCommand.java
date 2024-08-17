package org.ryjan.telegram.commands.interfaces;

import org.ryjan.telegram.handler.ButtonCommandHandler;
import org.ryjan.telegram.main.BotMain;

import java.io.IOException;

public interface IBotCommand {
    void execute(String chatId, BotMain bot, ButtonCommandHandler buttonCommandHandler) throws IOException;
}
