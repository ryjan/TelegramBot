package org.ryjan.telegram.commands.interfaces;

import org.ryjan.telegram.handler.ButtonCommandHandler;
import org.ryjan.telegram.BotMain;
import org.ryjan.telegram.services.BotService;

import java.io.IOException;

public interface IBotCommand {
    void execute(String chatId, BotService bot, ButtonCommandHandler buttonCommandHandler) throws IOException;
}
