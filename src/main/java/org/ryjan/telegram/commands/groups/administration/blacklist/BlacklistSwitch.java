package org.ryjan.telegram.commands.groups.administration.blacklist;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BlacklistSwitch extends BaseCommand {

    @Autowired
    ChatBlacklist chatBlacklist;

    @Autowired
    BlacklistSwitchOn blacklistSwitchOn;

    @Autowired
    BlacklistSwitchOff blacklistSwitchOff;

    public BlacklistSwitch() {
        super("/blacklist", "Включение/Отключение черного списка\nПо началу включен.", GroupPermissions.CREATOR);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler commandHandler) {
        Update update = getUpdate();

        if (blacklistService.blacklistStatus(update.getCallbackQuery().getMessage().getChatId())) {
            blacklistService.replaceBlacklistValue(Long.parseLong(chatId), "blacklist", "enabled");
            editMessage("🔒Черный список *включен*", blacklistSwitchOn.getKeyboard());
        } else {
            blacklistService.replaceBlacklistValue(Long.parseLong(chatId), "blacklist", "disabled");
            editMessage("🔓Черный список *выключен*", blacklistSwitchOff.getKeyboard());
        }
    }
}
