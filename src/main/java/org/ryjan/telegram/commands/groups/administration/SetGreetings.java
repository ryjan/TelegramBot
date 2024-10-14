package org.ryjan.telegram.commands.groups.administration;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SetGreetings extends BaseCommand {
    protected SetGreetings(String commandName, String description, GroupPermissions requiredPermission) {
        super("setGreetings", "🎃Установить приветствие", GroupPermissions.ADMIN);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler commandHandler) {
        SendMessage sendMessage = createSendMessage(chatId);


    }
}
