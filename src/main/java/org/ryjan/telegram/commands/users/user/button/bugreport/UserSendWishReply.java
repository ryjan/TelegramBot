package org.ryjan.telegram.commands.users.user.button.bugreport;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class UserSendWishReply extends BaseCommand {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    protected UserSendWishReply() {
        super("📃Поделиться идеей", "📃Поделиться идеей", Permission.ANY);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, GroupCommandHandler commandHandler) {
        SendMessage message = createSendMessage(chatId);
        redisTemplate.opsForValue().set("user_state:" + chatId, "waiting_message");
        message.setText("😉Введите свое пожелание:");

        sendMessageForCommand(bot, message);
    }
}
