package org.ryjan.telegram.commands.groups.administration.blacklist;

import org.ryjan.telegram.builders.InlineKeyboardBuilder;
import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.commands.groups.utils.GroupChatSettings;
import org.ryjan.telegram.commands.groups.utils.GroupSwitch;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.interfaces.Permissions;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.ChatSettings;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class BlacklistNotifications extends BaseCommand {
    protected BlacklistNotifications() {
        super("blacklistNotifications", "Blacklist notifications", GroupPermissions.CREATOR);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) throws Exception {
        ChatSettings chatSettings = chatSettingsService.findChatSettings(Long.parseLong(chatId), GroupChatSettings.BLACKLIST_NOTIFICATIONS);
        if (chatSettings.getSettingValue().equals(GroupSwitch.ON.getDisplayname())) {
            chatSettingsService.replaceSettingValue(Long.parseLong(chatId), GroupChatSettings.BLACKLIST_NOTIFICATIONS,
                    GroupSwitch.OFF.getDisplayname());
            editMessage("🔔Уведомления *выключены*", getKeyboard(false));
        } else if (chatSettings.getSettingValue().equals(GroupSwitch.OFF.getDisplayname())) {
            chatSettingsService.replaceSettingValue(Long.parseLong(chatId), GroupChatSettings.BLACKLIST_NOTIFICATIONS,
                    GroupSwitch.ON.getDisplayname());
            editMessage("🔔Уведомления *включены*", getKeyboard(true));
        }
    }

    public InlineKeyboardMarkup getKeyboard(boolean isEnabled) {
        String status = isEnabled ? "❌Выключить" : "✅Включить";
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder.KeyboardLayer keyboard = new InlineKeyboardBuilder.KeyboardLayer()
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton(status, getCommandName()))
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("↩️Назад", "/settings"));
        inlineKeyboardMarkup.setKeyboard(keyboard.build());

        return inlineKeyboardMarkup;
    }
}
