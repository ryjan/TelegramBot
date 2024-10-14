package org.ryjan.telegram.commands.groups.administration.blacklist;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.builders.InlineKeyboardBuilder;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Blacklist;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.text.MessageFormat;

@Component
public class ChatBlacklist extends BaseCommand {

    private String leftUserFirstName;
    private String leftUserUsername;
    private long leftUserId;

    protected ChatBlacklist() {
        super("chatBlacklist", "Черный список", GroupPermissions.CREATOR);
    }

    @Override
    public void executeCommand(String chatId, BotMain bot, CommandsHandler commandHandler) {
        Update update = getUpdate();

        long groupId = update.getMessage().getChat().getId();

        if (!blacklistService.blacklistStatus(groupId)) {
            return;
        }

        String groupName = update.getMessage().getChat().getTitle();
        setLeftUserId(update.getMessage().getLeftChatMember().getId());
        setLeftUserUsername(update.getMessage().getLeftChatMember().getUserName()); // добавить кнопку разбанить
        setLeftUserFirstName(update.getMessage().getLeftChatMember().getFirstName());

        //botMain.banUser(chatId, leftUserId);
        Blacklist blacklist = new Blacklist(groupName, leftUserId, leftUserUsername, leftUserFirstName);

        if (!blacklistService.isExistBlacklist(leftUserId)) {
            blacklistService.addToBlacklist(groupId, blacklist);
        }

        SendMessage message = createSendMessage(chatId);
        message.setText(MessageFormat.format("🥱Пользователь [{0}](https://t.me/{1}) покинул чат", leftUserFirstName, leftUserUsername));
        message.setParseMode(ParseMode.MARKDOWN);
        message.setReplyMarkup(getKeyboard());
        sendMessageForCommand(bot, message);
        System.out.println(getLeftUserId());
    }

    private InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder.KeyboardLayer keyboard = new InlineKeyboardBuilder.KeyboardLayer()
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("😶‍🌫️Разблокировать", "blacklistUnban"));
        inlineKeyboardMarkup.setKeyboard(keyboard.build());

        return inlineKeyboardMarkup;
    }

    public String getLeftUserUsername() {
        return leftUserUsername;
    }

    public void setLeftUserUsername(String leftUserUsername) {
        this.leftUserUsername = leftUserUsername;
    }

    public String getLeftUserFirstName() {
        return leftUserFirstName;
    }

    public void setLeftUserFirstName(String leftUserFirstName) {
        this.leftUserFirstName = leftUserFirstName;
    }

    public long getLeftUserId() {
        return leftUserId;
    }

    public void setLeftUserId(long leftUserId) {
        this.leftUserId = leftUserId;
    }
}
