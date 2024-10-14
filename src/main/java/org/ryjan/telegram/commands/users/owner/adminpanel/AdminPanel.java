package org.ryjan.telegram.commands.users.owner.adminpanel;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.handler.CommandsHandler;

import org.ryjan.telegram.main.BotMain;

public class AdminPanel extends BaseCommand {
    protected AdminPanel(String commandName, String description) {
        super("/adminpanel", "🔑Панель администратора", GroupPermissions.ADMIN);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler commandHandler) {

    }
}
