package org.ryjan.telegram.commands.groups.administration.blacklist;

import org.ryjan.telegram.commands.groups.BaseGroupCommand;
import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.commands.users.utils.InlineKeyboardBuilder;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class BlacklistSwitchOff extends BaseGroupCommand {

    @Autowired
    ChatBlacklist chatBlacklist;

    @Autowired
    GroupService groupService;

    protected BlacklistSwitchOff() {
        super("blacklistOff", "Выключить черный список", Permission.CREATOR);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler) {
        groupService.replaceBlacklistValue(Long.parseLong(chatId), "blacklist", "disabled");
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
