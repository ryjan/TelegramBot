package org.ryjan.telegram.commands.users.admin.adminpanel.bugreport.reply;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.users.admin.adminpanel.AdminPanelService;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.users.Articles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class LikeArticle extends BaseCommand {
    private final ReplyService replyService;
    private final AdminPanelService adminPanelService;

    @Autowired
    private NextArticle nextArticle;

    protected LikeArticle(ReplyService replyService, AdminPanelService adminPanelService) {
        super("🩷", "Поставить одобрение артиклю", UserPermissions.ADMIN);
        this.replyService = replyService;
        this.adminPanelService = adminPanelService;
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        Articles articles = replyService.getCurrentArticle();
        articles.setStatus("🩷Одобрено");
        articlesService.save(articles);
        SendMessage message = createSendMessage(articles.getUserId());
        message.setText("Ваше обращение было 🩷Одобрено :)\n\n" + replyService.getParsedText());
        message.enableMarkdown(true);
        sendMessageForCommand(bot, message);
        adminPanelService.sendAdminRewards(Long.valueOf(chatId));
        replyService.nextArticle(chatId);
    }
}
