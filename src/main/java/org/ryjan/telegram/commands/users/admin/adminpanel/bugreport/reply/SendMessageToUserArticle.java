package org.ryjan.telegram.commands.users.admin.adminpanel.bugreport.reply;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.users.Articles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.TimeUnit;

@Component
public class SendMessageToUserArticle extends BaseCommand {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private NextArticle nextArticle;

    private String chatId;
    private BotMain botMain;
    private CommandsHandler commandsHandler;

    protected SendMessageToUserArticle() {
        super("💌", "Отправить сообщение пользователю", UserPermissions.ADMINISTRATOR);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        this.chatId = chatId;
        this.botMain = bot;
        this.commandsHandler = handler;

        SendMessage message = createSendMessage(chatId);
        message.setText("Введите сообщение пользователю:");
        sendMessageForCommand(message);

        redisTemplate.opsForValue().set("admin_state:" + chatId, "waiting_message", 5, TimeUnit.MINUTES);
    }

    public void processArticleAndNotifyUser(Update update) {
        if (update.hasMessage() && update.getMessage().getChat().isUserChat()) {
            String userState = redisTemplate.opsForValue().get("admin_state:" + update.getMessage().getChatId());
            assert userState != null;

            if ("waiting_message".equals(userState)) {
                String adminMessage = update.getMessage().getText();
                Articles articles = nextArticle.getCurrentArticle();
                String userId = articles.getUserId().toString();

                SendMessage message = createSendMessage(userId);
                message.setText("✨Ответ: " + adminMessage + "\n\n" + nextArticle.getArticleParsedText());
                message.enableMarkdown(true);
                sendMessageForCommand(message);

                message.setText("✨Сообщение отправлено успешно!");
                message.setChatId(chatId);

                articles.setStatus("✨Рассмотрено");
                articlesService.update(articles);

                redisTemplate.delete("admin_state:" + update.getMessage().getChatId());
                sendMessageForCommand(message);
                nextArticle.execute(chatId, botMain, commandsHandler);
            }
        }
    }
}
