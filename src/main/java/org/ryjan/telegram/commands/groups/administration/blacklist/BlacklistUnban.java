package org.ryjan.telegram.commands.groups.administration.blacklist;

import org.ryjan.telegram.commands.groups.BaseGroupCommand;
import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Blacklist;
import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.services.BlacklistService;
import org.ryjan.telegram.services.BotService;
import org.ryjan.telegram.services.GroupService;
import org.ryjan.telegram.services.MainServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.MessageFormat;

@Component
public class BlacklistUnban extends BaseGroupCommand {

    @Autowired
    private ChatBlacklist chatBlacklist;

    protected BlacklistUnban() {
        super("blacklistUnban", "Разблокировать пользователя", Permission.ADMIN);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler) {
        System.out.println(chatBlacklist.getLeftUserId());
        blacklistUnban(chatId, chatBlacklist.getLeftUserId(), bot);
    }

    private void blacklistUnban(String chatId, Long userId, BotMain bot) {
        botService.unbanUser(chatId, userId);
        Blacklist blacklist = blacklistService.findBlacklist(userId);
        blacklistService.delete(blacklist);
        String adminUsername = getUpdate().getCallbackQuery().getFrom().getUserName();
        String adminFirstname = getUpdate().getCallbackQuery().getFrom().getFirstName();
        editMessage(MessageFormat.format("🤙Пользователь [{0}](https://t.me/{1}) разблокирован администратором [{2}](https://t.me/{3})",
                chatBlacklist.getLeftUserFirstName(), chatBlacklist.getLeftUserUsername(), adminFirstname, adminUsername));
    }
}
