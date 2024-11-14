package org.ryjan.telegram.commands.users.owner.ownerpanel.users;

import lombok.Getter;
import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.users.UserDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.TimeUnit;

@Component
public class OwnerFindUser extends BaseCommand {
    private final String CACHE_KEY = "owner_state:";
    private final String USER_CACHE_KEY = "owner_user_state:";

    private String chatId;
    private UserDatabase user;

    @Getter
    private SendMessage message;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisTemplate<String, UserDatabase> redisGroupsTemplate;

    public String userId;

    protected OwnerFindUser() {
        super("Find user", "Find user", UserPermissions.OWNER);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        this.chatId = chatId;
        message = createSendMessage(chatId);
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

            UserDatabase user = redisGroupsTemplate.opsForValue().get(USER_CACHE_KEY + userId);

            if (user == null) {
                try {
                    user = userService.findUser(userId);
                } catch (NumberFormatException e) {
                    message.setText("Write the correct username or userId. Try again.");
                    sendMessageForCommand(message);
                    redisGroupsTemplate.delete(CACHE_KEY + chatId);
                    return;
                }
                redisGroupsTemplate.opsForValue().set(USER_CACHE_KEY + userId, user, 10, TimeUnit.MINUTES); // дело в кэше
                if (user == null) {
                    message.setText("User is not found.");
                    redisGroupsTemplate.delete(CACHE_KEY + chatId);
                    sendMessageForCommand(message);
                    return;
                }
            }
            this.user = user;
            message.setText(String.format("ID: %s\nUsername: %s\nUser group: %s\nCreated at: %s", user.getId(), user.getUsername(), user.getUserGroup(), user.getCreatedAt()));
            message.enableMarkdown(true);
            redisGroupsTemplate.delete(CACHE_KEY + chatId);
            sendMessageForCommand(message);
        }
    }
}
