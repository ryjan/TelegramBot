package org.ryjan.telegram.commands.users.user.button.bugreport;

import org.ryjan.telegram.commands.users.utils.BaseUserCommand;
import org.ryjan.telegram.handler.UserCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.users.Articles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UserSendReportReply extends BaseUserCommand {

    @Autowired
    private BugReportService bugReportService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private String chatId;
    private BotMain bot;

    protected UserSendReportReply() {
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

    public void operationSuccessful(Update update, String commandName) {
        String userState = redisTemplate.opsForValue().get("user_state:" + chatId);
        assert userState != null;
        if ("waiting_message".equals(userState)) {
            String text = update.getMessage().getText();
            String username = update.getMessage().getFrom().getUserName();
            Articles articles = new Articles(commandName, text, username, Long.parseLong(chatId));
            bugReportService.update(articles);
            SendMessage message = createSendMessage(chatId);
            redisTemplate.delete("user_state:" + chatId);

            message.setText("⚙️Спасибо за обращение! Ожидайте ответа.");
            sendMessageForCommand(bot, message);
        }
    }
}
