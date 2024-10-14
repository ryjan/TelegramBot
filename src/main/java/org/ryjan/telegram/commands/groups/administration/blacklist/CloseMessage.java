package org.ryjan.telegram.commands.groups.administration.blacklist;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.springframework.stereotype.Component;

@Component
public class CloseMessage extends BaseCommand {
    protected CloseMessage() {
        super("closeMessage", "❌Закрыть", GroupPermissions.ADMIN);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler commandHandler) {
        deleteMessageByCallbackQuery();
    }
}
