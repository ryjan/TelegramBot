package org.ryjan.telegram.commands.groups.administration.blacklist;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.utils.GroupChatSettings;
import org.ryjan.telegram.commands.groups.utils.GroupSwitch;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.builders.InlineKeyboardBuilder;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class BlacklistSwitchOff extends BaseCommand {

    @Autowired
    BlacklistChatAdministration chatBlacklist;

    protected BlacklistSwitchOff() {
        super("blacklistOff", "Выключить черный список", GroupPermissions.CREATOR);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler commandHandler) {
        blacklistService.replaceBlacklistValue(Long.parseLong(chatId), GroupChatSettings.BLACKLIST.getDisplayname(), GroupSwitch.OFF.getDisplayname());
        editMessage("🔓Черный список *выключен*", getKeyboard());
    }

    public InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder.KeyboardLayer keyboard = new InlineKeyboardBuilder.KeyboardLayer()
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("✅Включить", "blacklistOn"))
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("📃Список заблокированных", "blacklistBannedUsersList"))
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("↩️Назад", "/settings"));
        inlineKeyboardMarkup.setKeyboard(keyboard.build());

        return inlineKeyboardMarkup;
    }
}
