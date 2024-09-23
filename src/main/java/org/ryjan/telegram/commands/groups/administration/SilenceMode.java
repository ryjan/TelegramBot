package org.ryjan.telegram.commands.groups.administration;

import org.ryjan.telegram.commands.groups.BaseGroupCommand;
import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class SilenceMode extends BaseGroupCommand {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Autowired
    private GroupService groupService;

    protected SilenceMode() {
        super("/silence", "🤫Режим тишины", Permission.ADMIN);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler) {
        SendMessage message = createSendMessage(chatId);
        String[] parts = getParts(getCommandName(), 1);

        if (parts.length != 1) {
            message.setText("🥲Введите /silence <количество_минут>");
            sendMessageForCommand(bot, message);
            return;
        }

        try {
            int minutes = Integer.parseInt(parts[0]);
            long silenceTime = System.currentTimeMillis() + ((long) minutes * 60 * 1000);
            groupService.addChatSettings(Long.valueOf(chatId), "silenceModeEndTime", String.valueOf(silenceTime));
            groupService.addChatSettings(Long.valueOf(chatId), "silenceModeActive", "enabled");

        } catch (IllegalArgumentException e) {
            message.setText("🥲Корректно введите количество минут");
            sendMessageForCommand(bot, message);
        }
    }
}
