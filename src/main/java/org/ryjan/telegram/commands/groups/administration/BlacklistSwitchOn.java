package org.ryjan.telegram.commands.groups.administration;

import org.ryjan.telegram.commands.groups.BaseGroupCommand;
import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;

@Component
public class BlacklistSwitchOn extends BaseGroupCommand {

    @Autowired
    ChatBlacklist chatBlacklist;

    protected BlacklistSwitchOn() {
        super("blacklistOn", "Включить черный список", Permission.CREATOR);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler) {
        SendMessage message = createSendMessage(chatId);
        chatBlacklist.enable();
        message.setText("🔒Черный список включен!" );
        sendMessageForCommand(bot, message);
    }
}
