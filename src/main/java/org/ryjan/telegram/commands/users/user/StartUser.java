package org.ryjan.telegram.commands.users.user;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.interfaces.Permissions;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.users.UserDatabase;
import org.ryjan.telegram.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.TimeUnit;

@Component
public class StartUser extends BaseCommand {

    protected StartUser() {
        super("/start", "✨Начать", UserPermissions.ANY);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        SendMessage message = createSendMessage(chatId);
        Message updateMessage = getUpdate().getMessage();
        long userId = updateMessage.getFrom().getId();
        UserDatabase userDatabase = userService.findUser(userId) == null ? new UserDatabase(userId, updateMessage.getFrom().getUserName())
                : userService.findUser(userId);
        userService.update(userDatabase);
        message.setText("✨Добавь бота в группу и пропиши в ней /start");
        sendMessageForCommand(bot, message);
    }
}
