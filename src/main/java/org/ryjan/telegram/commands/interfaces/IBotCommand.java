package org.ryjan.telegram.commands.interfaces;

import org.ryjan.telegram.commands.services.ButtonCommandHandler;
import org.ryjan.telegram.main.BotMain;

public interface IBotCommand {
    void execute(String charId, BotMain bot, ButtonCommandHandler buttonCommandHandler);
}
