package org.ryjan.telegram.commands.users.user.button.bugreport;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.builders.ReplyKeyboardBuilder;
import org.ryjan.telegram.handler.CommandsHandler;

import org.ryjan.telegram.main.BotMain;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class UserBugReport  extends BaseCommand {
    protected UserBugReport() {
        super("/bugreport", "👾Рассказать о баге или поделиться идеей", GroupPermissions.ANY);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler commandHandler) {
        SendMessage message = createSendMessage(chatId);
        message.setText("Выберите из списка:");
        message.setReplyMarkup(getReplyKeyboard());
        sendMessageForCommand(bot, message);
    }

    private ReplyKeyboardMarkup getReplyKeyboard() {
        return new ReplyKeyboardBuilder()
                .addRow("📃Поделиться идеей")
                .build();
    }
}
