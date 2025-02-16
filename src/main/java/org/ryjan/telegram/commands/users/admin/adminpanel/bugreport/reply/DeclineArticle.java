package org.ryjan.telegram.commands.users.admin.adminpanel.bugreport.reply;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.users.Articles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class DeclineArticle extends BaseCommand {
    private final ReplyService replyService;

    protected DeclineArticle(ReplyService replyService) {
        super("👎", "Отклонить обращение", UserPermissions.ADMIN);
        this.replyService = replyService;
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        Articles articles = replyService.getCurrentArticle();
        articles.setStatus("👎Отклонено");
        articlesService.save(articles);
        SendMessage message = createSendMessage(articles.getUserId());
        message.setText("Ваше обращение было 💔Отклонено :(\n\n" + replyService.getParsedText());
        message.enableMarkdown(true);
        messageSender.sendMessage(message);
        replyService.nextArticle(chatId);
    }
}