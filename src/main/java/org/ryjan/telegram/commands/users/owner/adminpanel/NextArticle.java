package org.ryjan.telegram.commands.users.owner.adminpanel;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.users.Articles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.text.MessageFormat;
import java.util.List;

@Component
public class NextArticle extends BaseCommand {
    private final String FIND_WISHES_CACHE_KEY = "checkArticlesAnswer:";
    private final String CACHE_KEY = "checkArticlesListAnswer:";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisTemplate<String, List<Articles>> redisArticlesTemplate;

    protected NextArticle() {
        super("⏭️", "Следующий артикуль", UserPermissions.ADMINISTRATOR);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        SendMessage message = createSendMessage(chatId);
        String answer = redisTemplate.opsForValue().get(FIND_WISHES_CACHE_KEY + chatId);

        if (answer != null) {
            List<Articles> articles = getArticles(chatId);
            if (articles == null) {
                message.setText("✨Список пуст");
                sendMessageForCommand(bot, message);
                return;
            }
            String[] parts = answer.split(":");
            int number = Integer.parseInt(parts[1]);
            Articles article = articles.get(number);

            String text = MessageFormat.format("✉️Отправитель: [{0}](tg://user?id={1}) \n\n{2}", article.getUsername(),
                    String.valueOf(article.getUserId()), article.getText());
            message.enableMarkdown(true);
            message.setText(text);

            String wish = "wish:" + (number + 1);
            redisTemplate.opsForValue().set(FIND_WISHES_CACHE_KEY + chatId, wish);
            sendMessageForCommand(bot, message);
            if (number == 9) {
                redisArticlesTemplate.delete(CACHE_KEY + chatId);
                redisTemplate.opsForValue().set(FIND_WISHES_CACHE_KEY + chatId, "wish:0");
            }
        }
    }

    private List<Articles> getArticles(String chatId) {
        List<Articles> articles = redisArticlesTemplate.opsForValue().get(CACHE_KEY + chatId);

        if (articles == null) {
            articles = articlesService.getFirstTenArticles();
            redisArticlesTemplate.opsForValue().set(CACHE_KEY + chatId, articles);
        }
        return articles;
    }
}
