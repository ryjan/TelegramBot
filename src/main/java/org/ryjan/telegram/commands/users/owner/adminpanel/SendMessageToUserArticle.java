package org.ryjan.telegram.commands.users.owner.adminpanel;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.interfaces.Permissions;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SendMessageToUserArticle extends BaseCommand {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private NextArticle nextArticle;

    private String chatId;
    private BotMain botMain;
    private CommandsHandler commandsHandler;
    private SendMessage message;

    protected SendMessageToUserArticle() {
        super("💌", "Отправить сообщение пользователю", UserPermissions.ADMINISTRATOR);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        this.chatId = chatId;
        this.botMain = bot;
        this.commandsHandler = handler;

        message = createSendMessage(chatId);
        message.setText("Введите сообщение пользователю:");
        redisTemplate.opsForValue().set("admin_state:" + chatId, "waiting_message");
    }

    public void processArticleAndNotifyUser(Update update) {
        if (update.hasMessage() && update.getMessage().getChat().isUserChat()) {
            String userState = redisTemplate.opsForValue().get("admin_state:" + update.getMessage().getChatId());
            assert userState != null;

            if ("waiting_message".equals(userState)) {
                message.setText("✨Сообщение отправлено успешно!");
                sendMessageForCommand(message);
                nextArticle.execute(chatId, botMain, commandsHandler);
            }
        }
    }
}
