package org.ryjan.telegram.commands.owner;

import org.ryjan.telegram.commands.interfaces.IBotCommand;
import org.ryjan.telegram.commands.utils.ButtonCommandHandler;
import org.ryjan.telegram.database.BankDatabase;
import org.ryjan.telegram.database.UserDatabase;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.services.UserService;
import org.ryjan.telegram.utils.UpdateContext;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.IOException;
import java.math.BigDecimal;

public class SetCoins implements IBotCommand {
    @Override
    public void execute(String chatId, BotMain bot, ButtonCommandHandler buttonCommandHandler) throws IOException {
        UserService userService = new UserService();
        Update update = UpdateContext.getInstance().getUpdate();
        User user = update.getMessage().getFrom();
        UserDatabase userDatabase = userService.findById(user.getId());
        BankDatabase bankDatabase = userDatabase.getBank();
        bankDatabase.setCoins(new BigDecimal(9999));
        userService.update(userDatabase);
    }
}
