package org.ryjan.telegram.commands.users.owner.ownerpanel;

import lombok.Getter;
import org.ryjan.telegram.builders.InlineKeyboardBuilder;
import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.GroupPrivileges;
import org.ryjan.telegram.commands.groups.GroupStatus;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Groups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.concurrent.TimeUnit;

@Component
public class FindGroupOwner extends BaseCommand {
    private final String CACHE_KEY = "owner_state:";
    private final String GROUP_CACHE_KEY = "owner_group_state:";

    private String chatId;
    private Groups group;
    @Getter
    private SendMessage message;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisTemplate<String, Groups> redisGroupsTemplate;

    public String groupId;

    protected FindGroupOwner() {
        super("Find group", "Set group privileges", UserPermissions.OWNER);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        this.chatId = chatId;
        message = createSendMessage(chatId);
        message.setText("Write the group ID");
        redisTemplate.opsForValue().set(CACHE_KEY + chatId, "waiting_message", 15, TimeUnit.MINUTES);
        sendMessageForCommand(message);
    }

    public void sendKeyboard(Update update) {
        String ownerState = redisTemplate.opsForValue().get(CACHE_KEY + chatId);

        assert ownerState != null;
        if ("waiting_message".equals(ownerState)) {
            SendMessage message = createSendMessage(chatId);
            groupId = update.getMessage().getText();

            if (groupId == null) {
                return;
            }

            Groups group = redisGroupsTemplate.opsForValue().get(GROUP_CACHE_KEY + chatId);

            if (group == null) {
                group = groupService.findGroup(Long.valueOf(groupId));
                redisGroupsTemplate.opsForValue().set(GROUP_CACHE_KEY + chatId, group, 1, TimeUnit.HOURS);
                if (group == null) {
                    message.setText("Group is not found.");
                    redisGroupsTemplate.delete(CACHE_KEY + chatId);
                    sendMessageForCommand(message);
                    return;
                }
            }
            this.group = group;
            message.setText(String.format("Group id: *%s*\nGroup name: *%s*\nCreator id: *%s*\nCreator username: *%s*\nCurrent privilege: *%s*\nStatus: *%s*\nCreated at: *%s*",
                    group.getId(), group.getGroupName(), group.getCreatorId(), group.getCreatorUsername(),
                    group.getPrivileges(), group.getStatus(), group.getCreatedAt()));
            message.enableMarkdown(true);
            if (group.getStatus().equals(GroupStatus.BANNED.getDisplayName())) {
                message.setReplyMarkup(getKeyboard(true));
            } else {
                message.setReplyMarkup(getKeyboard(false));
            }
            redisGroupsTemplate.delete(CACHE_KEY + chatId);
            sendMessageForCommand(message);
        }
    }

    public InlineKeyboardMarkup getKeyboard(boolean isBanKeyboard) {
        String buttonName = isBanKeyboard ? "💢Unban group" : "💢Ban group";
        String banStatus = isBanKeyboard ? "unbanGroup" : "banGroup";

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder.KeyboardLayer keyboard = new InlineKeyboardBuilder.KeyboardLayer()
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("©️Change group privilege", "changeGroupPrivilege")
                        .addButton(buttonName, banStatus));
        inlineKeyboardMarkup.setKeyboard(keyboard.build());

        return inlineKeyboardMarkup;
    }

    public String getParsedMessageWithStatus(String status) {
        return String.format("Group id: *%s*\nGroup name: *%s*\nCreator id: *%s*\nCreator username: *%s*\nCurrent privilege: *%s*\nStatus: *%s*\nCreated at: *%s*",
                group.getId(), group.getGroupName(), group.getCreatorId(), group.getCreatorUsername(),
                group.getPrivileges(),status, group.getCreatedAt());
    }
}

