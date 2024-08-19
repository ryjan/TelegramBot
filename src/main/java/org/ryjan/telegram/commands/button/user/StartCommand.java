package org.ryjan.telegram.commands.button.user;

import org.ryjan.telegram.commands.utils.ButtonCommandHandler;
import org.ryjan.telegram.commands.interfaces.IBotCommand;
import org.ryjan.telegram.database.UserDatabase;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.services.UserService;
import org.ryjan.telegram.utils.UpdateContext;
import org.ryjan.telegram.utils.UserGroup;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Objects;

@Component
public class StartCommand implements IBotCommand {
    @Override
    public void execute(String chatId, BotMain bot, ButtonCommandHandler commandHandler) {
        UserService userService = new UserService();
        Update update = UpdateContext.getInstance().getUpdate();
        User user = update.getMessage().getFrom();

        if (userService.userIsExist(user.getId())) {
            UserDatabase userDatabase = userService.findById(user.getId());
            if (!userDatabase.getUserTag().equals(user.getUserName())) {
                userDatabase.setUserTag(user.getUserName());
                userService.update(userDatabase);
            }
        }
        bot.sendMessage(chatId, "Выберите из меню:");
        commandHandler.sendMenu(chatId);
    }
}
