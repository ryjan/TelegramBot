package org.ryjan.telegram.commands.users.admin.adminpanel.bugreport;

import org.ryjan.telegram.builders.ReplyKeyboardBuilder;
import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.users.admin.adminpanel.bugreport.reply.NextArticle;
import org.ryjan.telegram.commands.users.admin.adminpanel.bugreport.reply.ReplyService;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.services.ArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class FindWishes extends BaseCommand {
    private final String CACHE_KEY = ArticlesService.CHECK_ARTICLES_ANSWER;
    private final ReplyService replyService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    protected FindWishes(ReplyService replyService) {
        super("findWishes", "Найти пожелания", UserPermissions.ADMIN);
        this.replyService = replyService;
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        SendMessage message = createSendMessage(chatId);
        message.setText("📃Пожелания:");
        redisTemplate.opsForValue().set(CACHE_KEY + chatId, "wish:0");
        message.setReplyMarkup(getReplyKeyboard());
        sendMessageForCommand(bot, message);
        replyService.nextArticle(chatId);
    }

    private ReplyKeyboardMarkup getReplyKeyboard() {
        return new ReplyKeyboardBuilder()
                .addRow("🩷", "👎", "💌", "⏭️")
                .build();
    }
}
