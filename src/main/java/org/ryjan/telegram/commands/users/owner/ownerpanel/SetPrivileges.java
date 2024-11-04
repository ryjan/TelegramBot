package org.ryjan.telegram.commands.users.owner.ownerpanel;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.GroupPrivileges;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.interfaces.Permissions;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Groups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class SetPrivileges extends BaseCommand {
    private final String GROUP_CACHE_KEY = "owner_group_state:";

    @Autowired
    private RedisTemplate<String, Groups> redisGroupsTemplate;

    public SetPrivileges() {
        super("setPrivilege", "Set privilege", UserPermissions.OWNER);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        SendMessage message = createSendMessage(chatId);
        Groups group = redisGroupsTemplate.opsForValue().get(GROUP_CACHE_KEY + chatId);
        CallbackQuery callbackQuery = getUpdate().getCallbackQuery();
        if (group != null) {
            String privilege = callbackQuery.getData().split(":")[1];
            group.setPrivileges(privilege);
            groupService.update(group);
            message.setText("Group privilege has updated to *" + privilege + "* successfully");
            message.enableMarkdown(true);
            sendMessageForCommand(message);
        }
    }
}
