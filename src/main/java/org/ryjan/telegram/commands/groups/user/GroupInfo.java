package org.ryjan.telegram.commands.groups.user;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.interfaces.Permissions;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Groups;
import org.springframework.stereotype.Component;

@Component
public class GroupInfo extends BaseCommand {
    protected GroupInfo() {
        super("/info", "Get group information", GroupPermissions.ANY);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        Groups group = groupService.findGroup(Long.parseLong(chatId));
        String message = String.format("✨Группа: *%s*\n🪪ID: *%s*\n💖Привелегия: *%s*\n🎭Статус: *%s*",
                group.getGroupName(), group.getId(), group.getPrivileges(), group.getStatus());
        sendMessageForCommand(chatId, message);
    }
}
