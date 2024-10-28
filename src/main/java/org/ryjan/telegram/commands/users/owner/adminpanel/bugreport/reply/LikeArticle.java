package org.ryjan.telegram.commands.users.owner.adminpanel.bugreport.reply;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.users.Articles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

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
        SendMessage message = createSendMessage(articles.getUserId());
        message.setText("Ваше обращение было 🩷Одобрено :)\n\n" + nextArticle.getArticleParsedText());
        message.enableMarkdown(true);
        sendMessageForCommand(bot, message);
        nextArticle.execute(chatId, bot, handler);
    }
}
