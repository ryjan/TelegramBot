package org.ryjan.telegram.commands.users.owner.ownerpanel;

import org.ryjan.telegram.builders.InlineKeyboardBuilder;
import org.ryjan.telegram.commands.groups.BaseCommand;
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

    public String groupId;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisTemplate<String, Groups> redisGroupsTemplate;

    private String chatId;

    protected FindGroupOwner() {
        super("Find group", "Set group privileges", UserPermissions.OWNER);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        this.chatId = chatId;
        SendMessage message = createSendMessage(chatId);
        message.setText("Write the group ID");
        redisTemplate.opsForValue().set(CACHE_KEY + chatId, "waiting_message");
        sendMessageForCommand(message);
    }

    public void sendKeyboard(Update update) {
        String ownerState = redisTemplate.opsForValue().get(CACHE_KEY + chatId);

        assert ownerState != null;
        if ("waiting_message".equals(ownerState)) {
            SendMessage message = createSendMessage(chatId);
            groupId = update.getMessage().getText();
            Groups group = redisGroupsTemplate.opsForValue().get(GROUP_CACHE_KEY + chatId);

            if (group == null) {
                group = groupService.findGroup(Long.valueOf(groupId));
                redisGroupsTemplate.opsForValue().set(GROUP_CACHE_KEY + chatId, group, 1, TimeUnit.HOURS);
                if (group == null) {
                    message.setText("Group is not found.");
                    sendMessageForCommand(message);
                    return;
                }
            }

            message.setText(String.format("Group id: %s\nGroup name: %s\nCreator id: %s\nCreator username: %s\nCurrent privilege: %s\nCreated at: %s",
                    group.getId(), group.getGroupName(), group.getCreatorId(), group.getCreatorUsername(),
                    group.getPrivileges(), group.getCreatedAt()));
            message.setReplyMarkup(getKeyboard());
            sendMessageForCommand(message);
        }
    }

    private InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder.KeyboardLayer keyboard = new InlineKeyboardBuilder.KeyboardLayer()
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("Change group privilege", "changeGroupPrivilege"));
        inlineKeyboardMarkup.setKeyboard(keyboard.build());

        return inlineKeyboardMarkup;
    }
}
