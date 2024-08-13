package org.ryjan.telegram.commands.button.user;

import org.ryjan.telegram.commands.services.ButtonCommandHandler;
import org.ryjan.telegram.commands.interfaces.IBotCommand;
import org.ryjan.telegram.config.BotConfig;
import org.ryjan.telegram.main.BotMain;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class OwnerCommand implements IBotCommand {

    @Override
    public void execute(String charId, BotMain bot, ButtonCommandHandler commandHandler) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(charId);
        sendMessage.setText("Создатель : @Ryjan4ik");

        try {
            bot.execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
