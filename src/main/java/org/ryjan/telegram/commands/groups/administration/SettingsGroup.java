package org.ryjan.telegram.commands.groups.administration;

import org.ryjan.telegram.commands.groups.BaseGroupCommand;
import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.builders.InlineKeyboardBuilder;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class SettingsGroup extends BaseGroupCommand {

    @Autowired
    private GroupService groupService;

    protected SettingsGroup() {
        super("/settings", "⚙️Настройки", Permission.ADMIN);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler) {
        Groups group = groupService.findGroup(Long.parseLong(chatId));

        if (group == null) {
            sendMessageForCommand(bot, new SendMessage(chatId, "✨/start"));
            return;
        }

        if (!getUpdate().hasCallbackQuery()) {
            SendMessage message = createSendMessage(chatId);
            message.setText("⚙️Настройки");
            message.setReplyMarkup(getKeyboard());
            sendMessageForCommand(bot, message);
        } else {
            editMessage("⚙️Настройки", getKeyboard());
        }
    }

    protected InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder.KeyboardLayer keyboard = new InlineKeyboardBuilder.KeyboardLayer()
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("🔒Черный список", "blacklistStartGroup"))
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("❌Закрыть", "closeMessage"));
        inlineKeyboardMarkup.setKeyboard(keyboard.build());

        return inlineKeyboardMarkup;
    }
}
