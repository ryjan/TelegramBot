package org.ryjan.telegram.commands.users.user.button.bugreport;

import org.ryjan.telegram.commands.users.BaseUserCommand;
import org.ryjan.telegram.handler.UserCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UserSendWishReply extends BaseUserCommand {
    private final Update update = getUpdate();

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    protected UserSendWishReply() {
        super("📃Поделиться идеей", "📃Поделиться идеей");
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, UserCommandHandler userCommandHandler) {
        SendMessage message = createSendMessage(chatId);
        redisTemplate.opsForValue().set("user_state:" + chatId, "waiting_message");
        message.setText("😉Введите свое пожелание:");

        sendMessageForCommand(bot, message);
    }
}
