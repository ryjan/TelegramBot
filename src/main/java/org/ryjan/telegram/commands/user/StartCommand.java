package org.ryjan.telegram.commands.user;

import org.ryjan.telegram.commands.ButtonCommandHandler;
import org.ryjan.telegram.interfaces.IBotCommand;
import org.ryjan.telegram.main.BotMain;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class StartCommand implements IBotCommand {

    @Override
    public void execute(String chatId, BotMain bot) {
        bot.sendMessage(chatId, "Меню для какой-то хуйни :) :");

        ButtonCommandHandler buttonCommandHandler = new ButtonCommandHandler(bot);
        buttonCommandHandler.sendMenu(chatId);
    }
}
