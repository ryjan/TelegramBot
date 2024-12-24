package org.ryjan.telegram.commands.groups.administration.blacklist;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Blacklist;
import org.ryjan.telegram.model.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class BlacklistUnban extends BaseCommand {

    protected BlacklistUnban() {
        super("blacklistUnban", "Разблокировать пользователя", GroupPermissions.ADMIN);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler commandHandler) {

        String leftUserId = getUpdate().getCallbackQuery().getData().split(" ")[1];
        String leftUserFirstName = getUpdate().getCallbackQuery().getData().split(" ")[2];
        blacklistUnban(chatId, Long.parseLong(leftUserId), leftUserFirstName);
    }

    private void blacklistUnban(String chatId, Long userId, String userFirstName) {
        botService.unbanUser(chatId, userId);
        User user = userService.findUser(userId);
        String adminUsername = getUpdate().getCallbackQuery().getFrom().getUserName();
        String adminFirstname = getUpdate().getCallbackQuery().getFrom().getFirstName();
        editMessage(MessageFormat.format("🤙Пользователь [{0}](https://t.me/{1}) разблокирован администратором [{2}](https://t.me/{3})",
                userFirstName, user.getUsername(), adminFirstname, adminUsername));
    }
}
