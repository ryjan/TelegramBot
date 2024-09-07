package org.ryjan.telegram.commands.groups.administration;

import org.ryjan.telegram.commands.groups.BaseGroupCommand;
import org.ryjan.telegram.commands.users.utils.KeyboardBuilder;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class BlacklistSwitch extends BaseGroupCommand {

    @Autowired
    ChatBlacklist chatBlacklist;

    public BlacklistSwitch() {
        super("/blacklist", "Включение/Отключение черного списка\nПо началу включен.");
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler) {
        Update update = getUpdate();
        SendMessage message = createSendMessage(chatId);
        message.setText("🔒Черный список");
        message.setReplyMarkup(getKeyboard());
        sendMessageForCommand(bot, message);

        if (update.getCallbackQuery() != null) {
            if (update.getCallbackQuery().getData().equals("off")) {
                chatBlacklist.disable();
                message.setText("🔓Черный список выключен!");
                sendMessageForCommand(bot, message);
            }
        } else if (update.getCallbackQuery().getData().equals("on")) {
            chatBlacklist.enable();
            message.setText("🔒Черный список включен!");
            sendMessageForCommand(bot, message);
        }
    }

    private InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        KeyboardBuilder.KeyboardLayer keyboard = new KeyboardBuilder.KeyboardLayer()
                .addRow(new KeyboardBuilder.ButtonRow()
                        .addButton("✅Включить", "on")
                        .addButton("❌Выключить", "off"));
        inlineKeyboardMarkup.setKeyboard(keyboard.build());

        return inlineKeyboardMarkup;
    }
}
