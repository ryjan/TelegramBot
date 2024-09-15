package org.ryjan.telegram.commands.groups.administration;

import org.ryjan.telegram.commands.groups.BaseGroupCommand;
import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.services.GroupService;
import org.ryjan.telegram.utils.UpdateContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class BlacklistSwitchOff extends BaseGroupCommand {

    @Autowired
    ChatBlacklist chatBlacklist;

    @Autowired
    GroupService groupService;

    protected BlacklistSwitchOff() {
        super("blacklistOff", "Выключить черный список", Permission.CREATOR);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler) {
        chatBlacklist.disable();
    }

    private void replaceMessageWithDone() {
        Update update = UpdateContext.getInstance().getUpdate();
        CallbackQuery callbackQuery = update.getCallbackQuery();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText("🔓Черный список выключен!");

        try {
            groupService.getBotMain().execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
