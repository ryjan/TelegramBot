package org.ryjan.telegram.commands.users.user.button.bugreport;

import org.ryjan.telegram.commands.users.BaseUserCommand;
import org.ryjan.telegram.commands.users.utils.ReplyKeyboardBuilder;
import org.ryjan.telegram.handler.UserCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class UserBugReport  extends BaseUserCommand {
    protected UserBugReport() {
        super("/bugreport", "👾Рассказать о баге или поделиться идеей");
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, UserCommandHandler userCommandHandler) {
        SendMessage message = createSendMessage(chatId);
        message.setText("Выберите из списка:");
        message.setReplyMarkup(getReplyKeyboard());
        sendMessageForCommand(bot, message);
    }

    private ReplyKeyboardMarkup getReplyKeyboard() {
        return new ReplyKeyboardBuilder()
                .addRow("👾Сообщить о баге", "📃Поделиться идеей")
                .build();
    }
}
