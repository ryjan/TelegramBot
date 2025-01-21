package org.ryjan.telegram.commands.users.owner.ownerpanel.groups;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.utils.GroupStatus;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.concurrent.TimeUnit;

@Component
public class BanGroup extends BaseCommand {
    private final String GROUP_CACHE_KEY = GroupService.OWNER_GROUP_STATE_CACHE_KEY;

    @Autowired
    private RedisTemplate<String, Groups> redisGroupsTemplate;

    @Autowired
    private OwnerFindGroup ownerFindGroup;

    protected BanGroup(GroupService groupService) {
        super("banGroup", "Ban the group", UserPermissions.TRUSTED);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        if (getUpdate().hasCallbackQuery() && getUpdate().getCallbackQuery().getData().equals("banGroup")) {
            banGroup(chatId, true);
        } else if (getUpdate().hasCallbackQuery() && getUpdate().getCallbackQuery().getData().equals(unbanGroupCallback())) {
            banGroup(chatId, false);
        }
    }

    private void banGroup(String chatId, boolean status) {
        SendMessage message = createSendMessage(chatId);
        message.enableMarkdown(true);
        String textStatus = status ? "BANNED" : "UNBANNED";
        Groups group = redisGroupsTemplate.opsForValue().get(GROUP_CACHE_KEY + ownerFindGroup.getGroupId());
        if (status) {
            group.setStatus(GroupStatus.BANNED.getDisplayName());
            message.setText("✨Group status been has changed to *" + textStatus + "* successfully!");
            editMessage(ownerFindGroup.getParsedMessageWithStatus(group.getStatus()), ownerFindGroup.getKeyboard(true));
        } else {
            group.setStatus(GroupStatus.ACTIVE.getDisplayName());
            message.setText("✨Group status been has changed to *" + textStatus + "* successfully!");
            editMessage(ownerFindGroup.getParsedMessageWithStatus(group.getStatus()), ownerFindGroup.getKeyboard(false));
        }
        groupService.save(group);
        redisGroupsTemplate.opsForValue().set(GROUP_CACHE_KEY + ownerFindGroup.getGroupId(), group, 15, TimeUnit.MINUTES);
        sendMessageForCommand(message);
    }

    public String unbanGroupCallback() {
        return "unbanGroup";
    }
}
