package org.ryjan.telegram.commands.users.owner.adminpanel;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.handler.UserCommandHandler;
import org.ryjan.telegram.main.BotMain;

public class AdminPanel extends BaseCommand<UserCommandHandler> {
    protected AdminPanel(String commandName, String description) {
        super("/adminpanel", "🔑Панель администратора", Permission.ADMIN);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, UserCommandHandler groupCommandHandler) {

    }
}
