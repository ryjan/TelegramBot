package org.ryjan.telegram.commands.interfaces;

import org.ryjan.telegram.handler.CommandHandler;
import org.ryjan.telegram.handler.UserCommandHandler;
import org.ryjan.telegram.main.BotMain;

import java.io.IOException;

public interface IBotUserCommand {
    void execute(String chatId, BotMain bot, CommandHandler commandHandler) throws IOException;
}
