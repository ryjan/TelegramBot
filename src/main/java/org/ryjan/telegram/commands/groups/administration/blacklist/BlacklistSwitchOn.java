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
public class BlacklistSwitchOn extends BaseCommand {

    @Autowired
    BlacklistChatAdministration chatBlacklist;

    protected BlacklistSwitchOn() {
        super("blacklistOn", "Включить черный список", GroupPermissions.CREATOR);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler commandHandler) {
        blacklistService.replaceBlacklistValue(Long.parseLong(chatId), GroupChatSettings.BLACKLIST.getDisplayname(), GroupSwitch.ON.getDisplayname());
        editMessage("🔒Черный список *включен*", getKeyboard());
    }

    public InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder.KeyboardLayer keyboard = new InlineKeyboardBuilder.KeyboardLayer()
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("❌Выключить", "blacklistOff"))
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("📃Список заблокированных", "blacklistBannedUsersList"))
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("↩️Назад", "/settings"));
        inlineKeyboardMarkup.setKeyboard(keyboard.build());

        return inlineKeyboardMarkup;
    }
}
