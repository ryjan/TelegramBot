package org.ryjan.telegram.commands.groups.administration;

import org.ryjan.telegram.commands.groups.BaseGroupCommand;
import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
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
            LocalDateTime dateTime = LocalDateTime.now().plusMinutes(minutes);
            String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy"));
            groupService.addChatSettings(Long.valueOf(chatId), "silenceModeEndTime", String.valueOf(dateTime));
            groupService.addChatSettings(Long.valueOf(chatId), "silenceModeActive", "enabled");
            message.setText("🎃Режим тишины включен\nВремя окончания: " + dateTime);

        } catch (IllegalArgumentException e) {
            message.setText("🥲Корректно введите количество минут");
            sendMessageForCommand(bot, message);
        }
        sendMessageForCommand(bot, message);
    }
}
