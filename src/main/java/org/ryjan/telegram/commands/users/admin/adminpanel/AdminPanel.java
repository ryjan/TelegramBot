package org.ryjan.telegram.commands.users.admin.adminpanel;

import org.ryjan.telegram.builders.InlineKeyboardBuilder;
import org.ryjan.telegram.builders.ReplyKeyboardBuilder;
import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.handler.CommandsHandler;

import org.ryjan.telegram.main.BotMain;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class AdminPanel extends BaseCommand {

    protected AdminPanel() {
        super("/adminpanel", "🔑Панель администратора", UserPermissions.ADMIN);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler commandHandler) {
        SendMessage message = createSendMessage(chatId);
        message.setReplyMarkup(getKeyboard());
        message.setText(getDescription() + ":");
        sendMessageForCommand(bot, message);
    }

    public ReplyKeyboardMarkup getKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardBuilder()
                .addRow("📃Посмотреть обращения")
                .build();
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        return replyKeyboardMarkup;
    }
}