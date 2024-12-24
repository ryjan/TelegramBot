package org.ryjan.telegram.commands.groups.administration.blacklist;

import lombok.Getter;
import lombok.Setter;
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

@Getter
@Setter
@Component
public class BlacklistChatAdministration extends BaseCommand {

    private String leftUserFirstName;
    private String leftUserUsername;
    private Long leftUserId;

    protected BlacklistChatAdministration() {
        super("chatBlacklist", "Черный список", GroupPermissions.CREATOR);
    }

    @Override
    public void executeCommand(String chatId, BotMain bot, CommandsHandler commandHandler) {
        Update update = getUpdate();

        Long groupId = update.getMessage().getChat().getId();
        System.out.println(blacklistService.isBlacklistEnabled(groupId));
        System.out.println(blacklistService.isBlacklistEnabled(groupId));
        System.out.println(blacklistService.isBlacklistEnabled(groupId));
        System.out.println(blacklistService.isBlacklistEnabled(groupId));
        System.out.println(blacklistService.isBlacklistEnabled(groupId));
        if (!blacklistService.isBlacklistEnabled(groupId)) {
            return;
        }

        setLeftUserId(update.getMessage().getLeftChatMember().getId());
        setLeftUserUsername(update.getMessage().getLeftChatMember().getUserName()); // добавить кнопку разбанить
        setLeftUserFirstName(update.getMessage().getLeftChatMember().getFirstName());

        botService.banUser(chatId, leftUserId);

        SendMessage message = createSendMessage(chatId);
        message.setText(MessageFormat.format("🥱Пользователь [{0}](https://t.me/{1}) покинул чат", leftUserFirstName, leftUserUsername));
        message.setParseMode(ParseMode.MARKDOWN);
        message.setReplyMarkup(getKeyboard("blacklistUnban " + leftUserId + " " + leftUserFirstName));
        sendMessageForCommand(bot, message);
    }

    private InlineKeyboardMarkup getKeyboard(String callBackDataName) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder.KeyboardLayer keyboard = new InlineKeyboardBuilder.KeyboardLayer()
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("😶‍🌫️Разблокировать", callBackDataName));
        inlineKeyboardMarkup.setKeyboard(keyboard.build());

        return inlineKeyboardMarkup;
    }
}
