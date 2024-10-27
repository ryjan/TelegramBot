package org.ryjan.telegram.commands.users.owner.adminpanel;

import org.ryjan.telegram.model.users.Articles;
import org.ryjan.telegram.services.ServiceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class AdminPanelService extends ServiceBuilder {

    @Autowired
    private NextArticle nextArticle;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void processArticleAndNotifyUser(Update update) {

    }
}
