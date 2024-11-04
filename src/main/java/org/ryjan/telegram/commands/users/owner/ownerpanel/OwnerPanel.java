package org.ryjan.telegram.commands.users.owner.ownerpanel;

import org.ryjan.telegram.builders.ReplyKeyboardBuilder;
import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class OwnerPanel extends BaseCommand {

    protected OwnerPanel() {
        super("/ownerpanel", "👾Панель создателя", UserPermissions.OWNER);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        SendMessage message = createSendMessage(chatId);
        message.setText(getDescription() + ":");
        message.setReplyMarkup(getKeyboard());
        sendMessageForCommand(bot, message);
    }

    public ReplyKeyboardMarkup getKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardBuilder()
                .addRow("Group settings", "User settings")
                .build();
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        return replyKeyboardMarkup;
    }
}
