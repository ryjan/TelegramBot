package org.ryjan.telegram.commands.groups.administration;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.builders.InlineKeyboardBuilder;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.concurrent.TimeUnit;

@Component
public class GroupSettings extends BaseCommand {
    protected GroupSettings() {
        super("/settings", "⚙️Настройки", GroupPermissions.ADMIN);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler commandHandler) {
        groupService.findGroup(Long.parseLong(chatId));
        SendMessage message = createSendMessage(chatId);

        if (!getUpdate().hasCallbackQuery()) {
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
                        .addButton("🔔Уведомления", "blacklistNotifications"))
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("🎃Узнать ID группы", "getGroupId"))
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("❌Закрыть", "closeMessage"));
        inlineKeyboardMarkup.setKeyboard(keyboard.build());

        return inlineKeyboardMarkup;
    }
}
