package org.ryjan.telegram.commands.groups.administration.blacklist;

import org.ryjan.telegram.commands.groups.BaseGroupCommand;
import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.commands.users.utils.KeyboardBuilder;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Blacklist;
import org.ryjan.telegram.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class BlacklistBannedUsersList extends BaseGroupCommand {
    @Autowired
    GroupService groupService;

    protected BlacklistBannedUsersList() {
        super("blacklistBannedUsersList", "✨Проверить список заблокированных пользователей", Permission.ADMIN);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler) {
        List<Blacklist> blacklistList = groupService.findAllBlacklists(Long.parseLong(chatId));

        if (blacklistList.isEmpty()) {
            editMessage("🎃Список пуст", getKeyboard());
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < blacklistList.size(); i++) {
            Blacklist blacklist = blacklistList.get(i);
            String formattedDate = blacklist.getCreatedAt().format(DateTimeFormatter.ofPattern("dd\\-MM\\-yyyy HH:mm:ss"));
            sb.append(MessageFormat.format("🎃Пользователь [{0}](https://t.me/{1}) был добавлен *{2}*", blacklist.getUserFirstname(), blacklist.getUsername(),
                    formattedDate))
                    .append("\n");
        }

        editMessage(sb.toString(), getKeyboard());
    }

    public InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        KeyboardBuilder.KeyboardLayer keyboard = new KeyboardBuilder.KeyboardLayer()
                .addRow(new KeyboardBuilder.ButtonRow()
                        .addButton("↩️Назад", "/blacklist"));
        inlineKeyboardMarkup.setKeyboard(keyboard.build());

        return inlineKeyboardMarkup;
    }
}
