package org.ryjan.telegram.commands.users;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.users.User;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

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
        userService.createUser(new User(userId, updateMessage.getFrom().getUserName().toLowerCase()));
        message.setText("✨Добавь бота в группу и пропиши в ней /start");
        sendMessageForCommand(bot, message);
    }
}
