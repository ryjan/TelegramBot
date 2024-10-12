package org.ryjan.telegram.commands.groups.administration.blacklist;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BlacklistSwitch extends BaseCommand<GroupCommandHandler> {

    @Autowired
    ChatBlacklist chatBlacklist;

    @Autowired
    BlacklistSwitchOn blacklistSwitchOn;

    @Autowired
    BlacklistSwitchOff blacklistSwitchOff;

    public BlacklistSwitch() {
        super("/blacklist", "Включение/Отключение черного списка\nПо началу включен.", Permission.CREATOR);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler) {
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
