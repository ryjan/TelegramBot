package org.ryjan.telegram.commands.users.owner.ownerpanel.users;

import lombok.Getter;
import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.TimeUnit;

@Component
public class OwnerFindUser extends BaseCommand {
    private final String CACHE_KEY = "owner_state:";
    private String chatId;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisTemplate<String, User> redisGroupsTemplate;

    public String userId;

    protected OwnerFindUser() {
        super("Find user", "Find user", UserPermissions.OWNER);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        this.chatId = chatId;
        SendMessage message = createSendMessage(chatId);
        message.setText("Write the username or userId");
        redisTemplate.opsForValue().set(CACHE_KEY + chatId, "waiting_message", 15, TimeUnit.MINUTES);
        sendMessageForCommand(message);
    }

    public void sendMessageWithKeyboard(Update update) {
        String ownerState = redisTemplate.opsForValue().get(CACHE_KEY + chatId);

        assert ownerState != null;
        if ("waiting_message".equals(ownerState)) {
            SendMessage message = createSendMessage(chatId);
            userId = update.getMessage().getText().trim().replace("@", "").toLowerCase();
            User user;

            try {
                user = userService.findUser(userId);
            } catch (NumberFormatException e) {
                message.setText("Write the correct username or userId. Try again.");
                sendMessageForCommand(message);
                redisGroupsTemplate.delete(CACHE_KEY + chatId);
                return;
            }
            message.setText(String.format("ID: *%s*%nUsername: *%s*%nUser group: *%s*%nCreated at: *%s*",
                    user.getId(), user.getUsername(), user.getUserGroup(), user.getCreatedAt()));
            message.enableMarkdown(true);

            redisGroupsTemplate.delete(CACHE_KEY + chatId);
            sendMessageForCommand(message);
        }
    }
}
