package org.ryjan.telegram.commands.users.owner.adminpanel.wishes;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.users.owner.adminpanel.NextArticle;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.interfaces.Permissions;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.users.Articles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;

import java.util.List;

@Component
public class LikeArticle extends BaseCommand {

    @Autowired
    private NextArticle nextArticle;

    protected LikeArticle() {
        super("🩷", "Поставить одобрение артиклю", UserPermissions.ADMINISTRATOR);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        Articles articles = nextArticle.getCurrentArticle();
        articles.setStatus("🩷Одобрено");
        articlesService.addArticleToRedisQueue(articles);
        nextArticle.execute(chatId, bot, handler);
    }
}
