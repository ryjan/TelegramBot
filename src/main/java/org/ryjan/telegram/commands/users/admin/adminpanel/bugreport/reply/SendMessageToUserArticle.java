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
    private final ReplyService replyService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private String chatId;

    protected SendMessageToUserArticle(ReplyService replyService) {
        super("💌", "Отправить сообщение пользователю", UserPermissions.ADMIN);
        this.replyService = replyService;
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        this.chatId = chatId;

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
                Articles articles = replyService.getCurrentArticle();
                String userId = articles.getUserId().toString();

                SendMessage message = createSendMessage(userId);
                message.setText("✨Ответ: " + adminMessage + "\n\n" + replyService.getParsedText());
                message.enableMarkdown(true);
                sendMessageForCommand(message);

                message.setText("✨Сообщение отправлено успешно!");
                message.setChatId(chatId);

                articles.setStatus("✨Рассмотрено");
                articlesService.save(articles);

                redisTemplate.delete("admin_state:" + update.getMessage().getChatId());
                sendMessageForCommand(message);
                replyService.nextArticle(chatId);
            }
        }
    }
}
