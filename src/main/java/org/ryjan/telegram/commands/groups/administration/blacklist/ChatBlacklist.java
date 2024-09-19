package org.ryjan.telegram.commands.groups.administration.blacklist;

import org.ryjan.telegram.commands.groups.BaseGroupCommand;
import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.commands.users.utils.KeyboardBuilder;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Blacklist;
import org.ryjan.telegram.services.GroupService;
import org.ryjan.telegram.utils.UpdateContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.text.MessageFormat;

@Component
public class ChatBlacklist extends BaseGroupCommand {

    @Autowired
    private GroupService groupService;

    @Autowired
    @Lazy
    private BotMain botMain;

    private boolean isEnabled = true;
    private long leftUserId;

    protected ChatBlacklist() {
        super("chatBlacklist", "Черный список", Permission.CREATOR);
    }

    @Override
    public void executeCommand(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler) {
        Update update = getUpdate();

        long groupId = update.getMessage().getChat().getId();

        if (!groupService.blacklistStatus(groupId)) {
            //System.out.println("is not exists()()");
            return;
        }

        String groupName = update.getMessage().getChat().getTitle();
        setLeftUserId(update.getMessage().getLeftChatMember().getId());
        String leftUserUsername = update.getMessage().getLeftChatMember().getUserName(); // добавить кнопку разбанить
        String leftUserFirstName = update.getMessage().getLeftChatMember().getFirstName();

        //botMain.banUser(chatId, leftUserId);
        Blacklist blacklist = new Blacklist(groupName, leftUserId, leftUserUsername);
        groupService.addToBlacklist(groupId, blacklist);

        SendMessage message = createSendMessage(chatId);
        message.setText(MessageFormat.format("🥱Пользователь [{0}](https://t.me/{1}) покинул чат", leftUserFirstName, leftUserUsername));
        message.setParseMode(ParseMode.MARKDOWN);
        message.setReplyMarkup(getKeyboard());
        sendMessageForCommand(bot, message);
        System.out.println(getLeftUserId());
    }

    private InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        KeyboardBuilder.KeyboardLayer keyboard = new KeyboardBuilder.KeyboardLayer()
                .addRow(new KeyboardBuilder.ButtonRow()
                        .addButton("😶‍🌫️Разблокировать", "blacklistUnban"));
        inlineKeyboardMarkup.setKeyboard(keyboard.build());

        return inlineKeyboardMarkup;
    }

    public void enable() {
        isEnabled = true;
    }

    public void disable() {
        isEnabled = false;
    }

    public long getLeftUserId() {
        return leftUserId;
    }

    public void setLeftUserId(long leftUserId) {
        this.leftUserId = leftUserId;
    }
}
