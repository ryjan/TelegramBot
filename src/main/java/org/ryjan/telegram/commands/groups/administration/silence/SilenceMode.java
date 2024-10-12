package org.ryjan.telegram.commands.groups.administration.silence;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class SilenceMode extends BaseCommand<GroupCommandHandler> {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Autowired
    private SilenceModeService silenceModeService;

    protected SilenceMode() {
        super("/silence", "🤫Режим тишины", Permission.ADMIN);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler) {
        SendMessage message = createSendMessage(chatId);
        String[] parts = getParts(getCommandName(), 1);

        if (parts[0].isEmpty() || parts[0].contains(" ")) {
            message.setText("🥲Введите /silence <количество_минут>");
            sendMessageForCommand(bot, message);
            return;
        }

        try {
            int minutes = Integer.parseInt(parts[0]);
            LocalDateTime dateTime = LocalDateTime.now().plusMinutes(minutes);
            String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy"));
            silenceModeService.enableSilenceMode(Long.valueOf(chatId), dateTime);
            message.setText("🎃Режим тишины включен\n**Время окончания: " + formattedDateTime + "**");
            message.setParseMode(ParseMode.MARKDOWN);
            sendMessageForCommand(bot, message);

        } catch (IllegalArgumentException e) {
            message.setText("❌Корректно введите кол-во минут");
            sendMessageForCommand(bot, message);
        }
    }

    public void doDeleteFunction() {
        Update update = getUpdate();
        Permission permission = getPermissionFromChat(update.getMessage().getChatId(), update.getMessage().getFrom().getId());
        if (permission.getPermission() < 2) {
            return;
        }

        deleteMessage();
    }
}
