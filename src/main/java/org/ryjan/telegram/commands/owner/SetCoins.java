/*package org.ryjan.telegram.commands.owner;

import org.ryjan.telegram.commands.interfaces.IBotCommand;
import org.ryjan.telegram.handler.ButtonCommandHandler;
import org.ryjan.telegram.database.BankDatabase;
import org.ryjan.telegram.database.UserDatabase;
import org.ryjan.telegram.BotMain;
import org.ryjan.telegram.commands.user.UserService;
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
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        Update update = UpdateContext.getInstance().getUpdate();
        String receivedMessage = update.getMessage().getText();

        //long id = Long.parseLong(buttonCommandHandler.getLastMessage().replace("/setcoins ", "").split(" ")[0]);
        if (receivedMessage.isEmpty() || receivedMessage.split(" ").length != 3) {
            message.setText("Команда введена неверно:  " + receivedMessage + "\n" + "Пример: /setcoins <user> <value>");
            buttonCommandHandler.sendMessageForCommand(bot, message);
            return;
        }

        String[] parts = buttonCommandHandler.getLastMessage().replace("/setcoins", "").split(" ");
        String username = buttonCommandHandler.getLastMessage().replace("/setcoins ", "").split(" ")[0];
        String coinsValue = buttonCommandHandler.getLastMessage().replace("/setcoins ", "").split(" ")[1];

        User user = update.getMessage().getFrom();
        UserDatabase owner = userService.findUser(user.getId());

        if (!owner.isOwner()) {
            message.setText("У вас недостаточно прав для выполнения команды:\n" + receivedMessage);
            buttonCommandHandler.sendMessageForCommand(bot, message);
            return;
        }

        UserDatabase userDatabase = userService.findUser(username);
        if (userDatabase == null) {
            message.setText("User'а @" + username + " не существует в базе данных Banks...");
            buttonCommandHandler.sendMessageForCommand(bot, message);
            return;
        }

        try {
            BankDatabase bankDatabase = userDatabase.getBank();
            BigDecimal coins = new BigDecimal(coinsValue);
            bankDatabase.setCoins(coins);
            userService.update(userDatabase);

            message.setText("User'у @" + userDatabase.getUserTag() + " выставлено " + coins + " монет.");
        } catch (NumberFormatException e) {
            message.setText("Неверный формат числа монет: " + coinsValue);
        }

        buttonCommandHandler.sendMessageForCommand(bot, message);

       // BigDecimal coins = BigDecimal.valueOf(Long.parseLong(receivedMessage));
       // bankDatabase.setCoins(coins);
       // userService.update(userDatabase);
    }
}

 */
