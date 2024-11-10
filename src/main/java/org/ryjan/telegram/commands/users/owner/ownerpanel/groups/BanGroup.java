package org.ryjan.telegram.commands.users.owner.ownerpanel.groups;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.GroupStatus;
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
    private final String GROUP_CACHE_KEY;

    private String chatId;

    @Autowired
    private RedisTemplate<String, Groups> redisGroupsTemplate;

    @Autowired
    private OwnerFindGroup findGroupOwner;

    protected BanGroup(GroupService groupService) {
        super("banGroup", "Ban the group", UserPermissions.TRUSTED);
        GROUP_CACHE_KEY = groupService.getOWNER_GROUP_STATE_CACHE_KEY();
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        this.chatId = chatId;
        if (getUpdate().hasCallbackQuery() && getUpdate().getCallbackQuery().getData().equals("banGroup")) {
            banGroup(true);
        } else if (getUpdate().hasCallbackQuery() && getUpdate().getCallbackQuery().getData().equals(unbanGroupCallback())) {
            banGroup(false);
        }
    }

    private void banGroup(boolean status) {
        SendMessage message = createSendMessage(chatId);
        message.enableMarkdown(true);
        String textStatus = status ? "BANNED" : "UNBANNED";
        Groups group = redisGroupsTemplate.opsForValue().get(GROUP_CACHE_KEY + chatId);
        if (status) {
            group.setStatus(GroupStatus.BANNED.getDisplayName());
            message.setText("✨Group status been has changed to *" + textStatus + "* successfully!");
            editMessage(findGroupOwner.getParsedMessageWithStatus(group.getStatus()), findGroupOwner.getKeyboard(true));
        } else {
            group.setStatus(GroupStatus.ACTIVE.getDisplayName());
            message.setText("✨Group status been has changed to *" + textStatus + "* successfully!");
            editMessage(findGroupOwner.getParsedMessageWithStatus(group.getStatus()), findGroupOwner.getKeyboard(false));
        }
        groupService.update(group);
        redisGroupsTemplate.opsForValue().set(GROUP_CACHE_KEY + chatId, group, 15, TimeUnit.MINUTES);
        sendMessageForCommand(message);
    }

    public String unbanGroupCallback() {
        return "unbanGroup";
    }
}
