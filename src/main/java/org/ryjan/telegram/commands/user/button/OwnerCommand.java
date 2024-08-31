package org.ryjan.telegram.commands.user.button;

import org.ryjan.telegram.handler.ButtonCommandHandler;
import org.ryjan.telegram.commands.interfaces.IBotCommand;
import org.ryjan.telegram.BotMain;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class OwnerCommand implements IBotCommand {

    @Override
    public void execute(String chatId, BotMain bot, ButtonCommandHandler commandHandler) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Создатель : @Ryjan4ik");

        try {
            bot.execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
