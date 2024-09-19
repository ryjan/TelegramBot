package org.ryjan.telegram.commands.groups.administration.blacklist;

import org.ryjan.telegram.commands.groups.BaseGroupCommand;
import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Blacklist;
import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BlacklistUnban extends BaseGroupCommand {

    @Autowired
    private GroupService groupService;

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
        bot.unbanUser(chatId, userId);
        Blacklist blacklist = groupService.findBlacklist(userId);
        groupService.delete(blacklist);
        editMessage("🤙Пользователь разблокирован");
    }
}
