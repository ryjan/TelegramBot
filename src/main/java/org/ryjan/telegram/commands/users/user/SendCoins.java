/*package org.ryjan.telegram.commands.users.user;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.commands.users.user.transfers.TransferService;
import org.ryjan.telegram.model.users.UserDatabase;

import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.utils.UpdateContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component("userSendCoins")
public class SendCoins extends BaseCommand<UserCommandHandler> {

    @Autowired
    TransferService transferService;

    public SendCoins() {
        super("/sendcoins", "Отправить пользователю свои монеты🪙", Permission.ANY);
    }

    @Override
    public void executeCommand(String chatId, BotMain bot, UserCommandHandler userCommandHandler) {
        Update update = UpdateContext.getInstance().getUpdate();
        UserDatabase fromUser = getFromUserDatabase();
        UserDatabase toUser;
        SendMessage message = createSendMessage();

        String[] parts = getParts(getCommandName(), 2);
        if (parts.length != 2 || !parts[0].startsWith("@")) {
            message.setText("Введена неверная команда!\nПример: /sendcoins @Ryjan4ik 999");
            sendMessageForCommand(bot, message);
            return;
        }
        String username = parts[0];
        String amountString = parts[1];
        toUser = findUserDatabase(username.substring(1));
        if (toUser == null) {
            message.setText(userNotFound(username));
            sendMessageForCommand(bot, message);
            return;
        }

        try {
            BigDecimal amount = new BigDecimal(amountString);
            String result = transferService.transferCoins(getUsername(), toUser.getUserTag(), amount.setScale(2, RoundingMode.HALF_UP));
            message.setText(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendMessageForCommand(bot, message);
    }
}
 */
