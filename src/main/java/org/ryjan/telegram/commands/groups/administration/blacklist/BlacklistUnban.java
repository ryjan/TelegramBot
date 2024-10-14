package org.ryjan.telegram.commands.groups.administration.blacklist;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Blacklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class BlacklistUnban extends BaseCommand {

    @Autowired
    private ChatBlacklist chatBlacklist;

    protected BlacklistUnban() {
        super("blacklistUnban", "Разблокировать пользователя", GroupPermissions.ADMIN);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler commandHandler) {
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
