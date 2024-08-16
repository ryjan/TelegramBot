package org.ryjan.telegram.commands.button.user;

import org.ryjan.telegram.commands.utils.ButtonCommandHandler;
import org.ryjan.telegram.commands.interfaces.IBotCommand;
import org.ryjan.telegram.main.BotMain;

public class StartCommand implements IBotCommand {

    @Override
    public void execute(String chatId, BotMain bot, ButtonCommandHandler commandHandler) {
        bot.sendMessage(chatId, "Выберите из меню:");
        commandHandler.sendMenu(chatId);
    }
}
