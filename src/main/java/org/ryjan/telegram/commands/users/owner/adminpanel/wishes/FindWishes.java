package org.ryjan.telegram.commands.users.owner.adminpanel.wishes;

import org.ryjan.telegram.builders.ReplyKeyboardBuilder;
import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class FindWishes extends BaseCommand {
    private final String CACHE_KEY = "checkArticlesAnswer:";

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    protected FindWishes() {
        super("findWishes", "Найти пожелания", UserPermissions.ADMINISTRATOR);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        System.out.println("gsdgsdgsdfgsdfg");
        SendMessage message = createSendMessage(chatId);
        message.setText("📃Пожелания:");
        redisTemplate.opsForValue().set(CACHE_KEY + chatId, "wish:0");
        message.setReplyMarkup(getReplyKeyboard());
        sendMessageForCommand(bot, message);
    }

    private ReplyKeyboardMarkup getReplyKeyboard() {
        return new ReplyKeyboardBuilder()
                .addRow("🩷", "👎", "💌", "⏭️")
                .build();
    }
}
