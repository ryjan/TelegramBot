package org.ryjan.telegram.commands.groups.administration;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SetGreetings extends BaseCommand {
    protected SetGreetings(String commandName, String description, Permission requiredPermission) {
        super("setGreetings", "🎃Установить приветствие", Permission.ADMIN);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler) {
        SendMessage sendMessage = createSendMessage(chatId);


    }
}
