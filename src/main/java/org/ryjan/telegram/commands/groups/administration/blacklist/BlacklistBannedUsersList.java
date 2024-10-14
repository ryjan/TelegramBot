package org.ryjan.telegram.commands.groups.administration.blacklist;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.builders.InlineKeyboardBuilder;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Blacklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class BlacklistBannedUsersList extends BaseCommand {

    @Autowired
    private RedisTemplate<String, List<Blacklist>> redisTemplate;

    protected BlacklistBannedUsersList() {
        super("blacklistBannedUsersList", "✨Проверить список заблокированных пользователей", GroupPermissions.ADMIN);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler commandHandler) {
        List<Blacklist> blacklistList = getBlacklist(chatId);

        if (blacklistList.isEmpty()) {
            editMessage("🎃Список пуст", getKeyboard());
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < blacklistList.size(); i++) {
            Blacklist blacklist = blacklistList.get(i);
            String formattedDate = blacklist.getCreatedAt().replace("-", "\\-");
            sb.append(MessageFormat.format("🎃Пользователь [{0}](https://t.me/{1}) был добавлен *{2}*", blacklist.getUserFirstname(), blacklist.getUsername(),
                    formattedDate))
                    .append("\n");
        }

        editMessage(sb.toString(), getKeyboard());
    }

    public InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder.KeyboardLayer keyboard = new InlineKeyboardBuilder.KeyboardLayer()
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("↩️Назад", "/blacklist"));
        inlineKeyboardMarkup.setKeyboard(keyboard.build());

        return inlineKeyboardMarkup;
    }

    private List<Blacklist> getBlacklist(String chatId) {
        String blacklistCacheKey = "groupBlacklist:" + chatId;
        List<Blacklist> blacklistList = redisTemplate.opsForValue().get(blacklistCacheKey);
        System.out.println(redisTemplate.opsForValue().get(blacklistCacheKey) == null);

        if (blacklistList == null) {
            blacklistList = blacklistService.findAllBlacklists(Long.parseLong(chatId));
            redisTemplate.opsForValue().set(blacklistCacheKey, blacklistList, 30, TimeUnit.MINUTES);
        }

        for (Blacklist blacklist : blacklistList) {
            System.out.println(blacklist.getUsername());
        }

        return blacklistList;
    }
}
