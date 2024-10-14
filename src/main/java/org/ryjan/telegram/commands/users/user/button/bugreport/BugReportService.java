package org.ryjan.telegram.commands.users.user.button.bugreport;

import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.users.Articles;
import org.ryjan.telegram.interfaces.repos.jpa.ArticlesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
public class BugReportService {

    @Autowired
    ArticlesRepository articlesRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    @Lazy
    private BotMain botMain;

    public void operationSuccessful(Update update, String commandName) {
        String chatId = update.getMessage().getChatId().toString();
        String userState = redisTemplate.opsForValue().get("user_state:" + update.getMessage().getChatId());
        assert userState != null;
        if ("waiting_message".equals(userState)) {
            String text = update.getMessage().getText();
            String username = update.getMessage().getFrom().getUserName();
            Articles articles = new Articles(commandName, text, username, Long.parseLong(chatId));
            update(articles);
            SendMessage message = new SendMessage();
            redisTemplate.delete("user_state:" + chatId);

            message.setChatId(chatId);
            message.setText("⚙️Спасибо за обращение! Ожидайте ответа.");

            try {
                botMain.execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<Articles> findArticlesList(Long userId) {
        return articlesRepository.findAllByUserId(userId);
    }

    public List<Articles> findArticlesList(String username) {
        return articlesRepository.findAllByUsername(username);
    }

    public void update(Articles articles) {
        articlesRepository.save(articles);
    }

    public void delete(Articles articles) {
        articlesRepository.delete(articles);
    }
}
