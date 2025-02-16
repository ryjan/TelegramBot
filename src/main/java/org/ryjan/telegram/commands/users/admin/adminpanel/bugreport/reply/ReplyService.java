package org.ryjan.telegram.commands.users.admin.adminpanel.bugreport.reply;

import lombok.Getter;
import lombok.Setter;
import org.ryjan.telegram.model.users.Articles;
import org.ryjan.telegram.services.ArticlesService;
import org.ryjan.telegram.services.ServiceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ReplyService extends ServiceBuilder {
    private final String CHECK_ARTICLES_ANSWER = ArticlesService.CHECK_ARTICLES_ANSWER;
    private final String CHECK_ARTICLES_LIST_ANSWER = ArticlesService.CHECK_ARTICLES_LIST_ANSWER;

    @Getter
    @Setter
    private String parsedText;
    @Getter
    @Setter
    private Articles currentArticle;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RedisTemplate<String, List<Articles>> articlesRedisTemplate;

    public void nextArticle(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        Articles article;
        List<Articles> articles;
        int number = Integer.parseInt(getAnswer(chatId).split(":")[1]);

        try {
            articles = getArticles(chatId);
            article = getArticle(chatId, number);
        } catch (IllegalArgumentException | NullPointerException e) {
            String message = e instanceof IllegalArgumentException ? "👾Пожелания закончились" : "✨Список пуст";
            messageSender.sendMessage(chatId, message);
            return;
        }

        articles.remove(number);
        setCurrentArticle(article);

        setParsedText(formatArticleMessage(article));

        String wishNumber = "wish:" + (number + 1);

        articlesRedisTemplate.opsForValue().set(CHECK_ARTICLES_LIST_ANSWER + chatId, articles);
        redisTemplate.opsForValue().set(CHECK_ARTICLES_ANSWER + chatId, wishNumber);
        messageSender.sendMessage(chatId, getParsedText());

        if (articles.isEmpty()) {
            articlesRedisTemplate.delete(CHECK_ARTICLES_LIST_ANSWER + chatId);
            redisTemplate.opsForValue().set(CHECK_ARTICLES_ANSWER + chatId, "wish:0");
        }
    }

    public String formatArticleMessage(Articles article) {
        return MessageFormat.format("✉️Отправитель: [{0}](tg://user?id={1}) \n*🕑Дата отправки: {2}*\n\n{3}",
                article.getUsername(), String.valueOf(article.getUserId()), article.getCreatedAt(), article.getText());
    }

    private Articles getArticle(String chatId, int number) {
        List<Articles> articles = getArticles(chatId);
        if (number >= articles.size()) {
            throw new IllegalArgumentException();
        }
        return articles.get(number);
    }

    private List<Articles> getArticles(String chatId) {
        if (getAnswer(chatId) != null) {
            List<Articles> articles = articlesRedisTemplate.opsForValue().get(CHECK_ARTICLES_LIST_ANSWER + chatId);
            if (articles == null) {
                articles = articlesService.findFirstTenArticles();
                articlesRedisTemplate.opsForValue().set(CHECK_ARTICLES_LIST_ANSWER + chatId, articles, 5, TimeUnit.MINUTES);
            }
            return articles;
        }
        throw new NullPointerException("articles list in getArticles is null");
    }

    private String getAnswer(String chatId) {
        return redisTemplate.opsForValue().get(CHECK_ARTICLES_ANSWER + chatId);
    }
}