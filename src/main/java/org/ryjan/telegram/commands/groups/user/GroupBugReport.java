package org.ryjan.telegram.commands.groups.user;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.commands.users.user.button.bugreport.UserBugReport;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class GroupBugReport extends BaseCommand {

    @Autowired
    private UserBugReport userBugReport;

    protected GroupBugReport() {
        super("/bugreport", "✨Рассказать о баге", GroupPermissions.ANY);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        SendMessage message = createSendMessage(chatId);
        message.setText("✨Зайдите в личные сообщения с " + bot.getBotTag());
        sendMessageForCommand(message);
        userBugReport.execute(String.valueOf(getUpdate().getMessage().getFrom().getId()), bot, handler);
    }
}
