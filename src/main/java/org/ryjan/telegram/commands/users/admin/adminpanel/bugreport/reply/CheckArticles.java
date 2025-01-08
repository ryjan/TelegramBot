package org.ryjan.telegram.commands.users.admin.adminpanel.bugreport.reply;

import org.ryjan.telegram.builders.InlineKeyboardBuilder;
import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.users.admin.adminpanel.bugreport.wishes.FindWishes;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class CheckArticles extends BaseCommand {
    @Autowired
    private FindWishes findWishes;

    protected CheckArticles() {
        super("📃Посмотреть обращения", "📃Посмотреть обращения", UserPermissions.ADMINISTRATOR);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        SendMessage message = createSendMessage(chatId);
        message.setText("Выберите:");
        message.setReplyMarkup(getKeyboard());
        sendMessageForCommand(bot, message);
    }

    public InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder.KeyboardLayer keyboard = new InlineKeyboardBuilder.KeyboardLayer()
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("📃Пожелания", findWishes.getCommandName()));
        inlineKeyboardMarkup.setKeyboard(keyboard.build());

        return inlineKeyboardMarkup;
    }
}
