package org.ryjan.telegram.commands.groups.administration.blacklist;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.utils.GroupChatSettings;
import org.ryjan.telegram.commands.groups.utils.GroupSwitch;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BlacklistSwitch extends BaseCommand {
    @Autowired
    private BlacklistSwitchOn blacklistSwitchOn;
    @Autowired
    private BlacklistSwitchOff blacklistSwitchOff;

    public BlacklistSwitch() {
        super("/blacklist", "Включение/Отключение черного списка\nПо началу включен.", GroupPermissions.CREATOR);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler commandHandler) {
        Update update = getUpdate();

        if (blacklistService.isBlacklistEnabled(update.getCallbackQuery().getMessage().getChatId())) {
            blacklistService.replaceBlacklistValue(Long.parseLong(chatId), GroupChatSettings.BLACKLIST.getDisplayname(), GroupSwitch.ON.getDisplayname());
            editMessage("🔒Черный список *включен*", blacklistSwitchOn.getKeyboard());
        } else {
            blacklistService.replaceBlacklistValue(Long.parseLong(chatId), GroupChatSettings.BLACKLIST.getDisplayname(), GroupSwitch.ON.getDisplayname());
            editMessage("🔓Черный список *выключен*", blacklistSwitchOff.getKeyboard());
        }
    }
}