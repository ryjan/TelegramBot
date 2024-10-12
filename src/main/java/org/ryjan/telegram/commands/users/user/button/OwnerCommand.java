package org.ryjan.telegram.commands.users.user.button;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.handler.CommandHandler;
import org.ryjan.telegram.handler.UserCommandHandler;
import org.ryjan.telegram.commands.interfaces.IBotUserCommand;
import org.ryjan.telegram.main.BotMain;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class OwnerCommand extends BaseCommand<UserCommandHandler> {

    protected OwnerCommand(String commandName, String description, Permission requiredPermission) {
        super(commandName, description, requiredPermission);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, UserCommandHandler handler) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Создатель : @Ryjan4ik");
    }
}
