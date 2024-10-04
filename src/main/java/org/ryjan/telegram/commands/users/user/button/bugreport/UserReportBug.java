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
public class UserReportBug extends BaseUserCommand {
    private final Update update = getUpdate();

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private String chatId;
    private BotMain bot;

    protected UserReportBug() {
        super("👾Сообщить о баге", "👾Сообщить о баге");
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, UserCommandHandler userCommandHandler) {
        this.chatId = chatId;
        this.bot = bot;
        SendMessage message = createSendMessage(chatId);
        redisTemplate.opsForValue().set("user_state:" + chatId, "waiting_message");
        message.setText("✨Введите свое обращение:");

        sendMessageForCommand(bot, message);
    }

    public void operationSuccessful() {
        String userState = redisTemplate.opsForValue().get("user_state:" + chatId);
        assert userState != null;
        if ("waiting_message".equals(userState)) {
            SendMessage message = createSendMessage(chatId);
            redisTemplate.delete("user_state:" + chatId);
            message.setText("⚙️Обращение успешно зарегистрированно, ожидайте ответа...");
            sendMessageForCommand(bot, message);
        }
    }
}
