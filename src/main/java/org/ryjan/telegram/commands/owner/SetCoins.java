package org.ryjan.telegram.commands.owner;

import org.ryjan.telegram.commands.interfaces.IBotCommand;
import org.ryjan.telegram.commands.utils.ButtonCommandHandler;
import org.ryjan.telegram.database.BankDatabase;
import org.ryjan.telegram.database.UserDatabase;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.services.UserService;
import org.ryjan.telegram.utils.UpdateContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.IOException;
import java.math.BigDecimal;

public class SetCoins implements IBotCommand {
    @Override
    public void execute(String chatId, BotMain bot, ButtonCommandHandler buttonCommandHandler) throws IOException {
        UserService userService = new UserService();
        Update update = UpdateContext.getInstance().getUpdate();
        String receivedMessage = update.getMessage().getText();

        String messageToSend = buttonCommandHandler.getLastMessage().replace("/setcoins", "").trim();

        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        User user = update.getMessage().getFrom();
        UserDatabase userDatabase = userService.findById(user.getId());
        BankDatabase bankDatabase = userDatabase.getBank();

        BigDecimal coins = BigDecimal.valueOf(Long.parseLong(messageToSend));

        bankDatabase.setCoins(coins);
        userService.update(userDatabase);

        message.setText("User'у @" + user.getUserName() + " выставлено " + coins + " монет.");

        try {
            bot.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

       // BigDecimal coins = BigDecimal.valueOf(Long.parseLong(receivedMessage));
       // bankDatabase.setCoins(coins);
       // userService.update(userDatabase);
    }
}
