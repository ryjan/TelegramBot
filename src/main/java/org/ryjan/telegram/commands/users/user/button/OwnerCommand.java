package org.ryjan.telegram.commands.users.user.button;

import org.ryjan.telegram.handler.UserCommandHandler;
import org.ryjan.telegram.commands.interfaces.IBotCommand;
import org.ryjan.telegram.main.BotMain;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class OwnerCommand implements IBotCommand {

    @Override
    public void execute(String chatId, BotMain bot, UserCommandHandler commandHandler) {
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
