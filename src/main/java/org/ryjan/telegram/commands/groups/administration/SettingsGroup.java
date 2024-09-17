package org.ryjan.telegram.commands.groups.administration;

import org.ryjan.telegram.commands.groups.BaseGroupCommand;
import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.commands.users.utils.KeyboardBuilder;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class SettingsGroup extends BaseGroupCommand {

    protected SettingsGroup() {
        super("/settings", "⚙️Настройки", Permission.ADMIN);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler) {
        if (!getUpdate().hasCallbackQuery()) {
            SendMessage message = createSendMessage(chatId);
            message.setText("⚙️Настройки");
            message.setReplyMarkup(getKeyboard());
            sendMessageForCommand(bot, message);
        } else {
            editMessage("⚙️Настройки", getKeyboard());
        }
    }

    private InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        KeyboardBuilder.KeyboardLayer keyboard = new KeyboardBuilder.KeyboardLayer()
                .addRow(new KeyboardBuilder.ButtonRow()
                        .addButton("🔒Черный список", "blacklistStartGroup")
                        .addButton("Проврокк", "aboba"));
        inlineKeyboardMarkup.setKeyboard(keyboard.build());

        return inlineKeyboardMarkup;
    }
}
