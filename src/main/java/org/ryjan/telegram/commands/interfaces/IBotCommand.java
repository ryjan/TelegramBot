package org.ryjan.telegram.commands.interfaces;

import org.ryjan.telegram.handler.UserCommandHandler;
import org.ryjan.telegram.main.BotMain;

import java.io.IOException;

public interface IBotCommand {
    void execute(String chatId, BotMain bot, UserCommandHandler userCommandHandler) throws IOException;
}
