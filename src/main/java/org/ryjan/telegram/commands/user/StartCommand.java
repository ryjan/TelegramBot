/* package org.ryjan.telegram.commands.user.button;

import org.ryjan.telegram.handler.ButtonCommandHandler;
import org.ryjan.telegram.commands.interfaces.IBotCommand;
import org.ryjan.telegram.database.UserDatabase;
import org.ryjan.telegram.BotMain;
import org.ryjan.telegram.commands.user.UserService;
import org.ryjan.telegram.utils.UpdateContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class StartCommand implements IBotCommand {
    @Override
    public void execute(String chatId, BotMain bot, ButtonCommandHandler commandHandler) {
        UserService userService = new UserService();
        Update update = UpdateContext.getInstance().getUpdate();
        User user = update.getMessage().getFrom();
        UserDatabase userDatabase;

        if (userService.userIsExist(user.getId())) {
            userDatabase = userService.findUser(user.getId());
            if (!userDatabase.getUserTag().equals(user.getUserName())) {
                userDatabase.setUserTag(user.getUserName());
                userService.update(userDatabase);
            }
        } else {
            userDatabase = new UserDatabase(user.getId(), user.getUserName());
            userService.update(userDatabase);
        }
        bot.sendMessage(chatId, "Выберите из меню:");
        commandHandler.sendMenu(chatId);
    }
}
 */
