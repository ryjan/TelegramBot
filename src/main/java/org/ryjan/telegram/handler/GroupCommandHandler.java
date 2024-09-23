package org.ryjan.telegram.handler;

import org.ryjan.telegram.commands.groups.BaseGroupCommand;
import org.ryjan.telegram.commands.groups.administration.*;
import org.ryjan.telegram.commands.groups.administration.blacklist.*;
import org.ryjan.telegram.commands.interfaces.IBotGroupCommand;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class GroupCommandHandler {

    private final StartGroup startGroupCommand;
    private final SettingsGroup settingsGroup;
    private final BlacklistSwitch blacklistSwitch;
    private final BlacklistSwitchOn blacklistSwitchOn;
    private final BlacklistSwitchOff blacklistSwitchOff;
    private final BlacklistUnban blacklistUnban;
    private final BlacklistBannedUsersList blacklistBannedUsers;
    private final CloseMessage closeMessage;

    private final Map<String, BaseGroupCommand> commands;
    private final Map<String, BaseGroupCommand> buttonCommands;
    private String lastMessage;

    @Autowired
    @Lazy
    private BotMain bot;

    public GroupCommandHandler(StartGroup startGroupCommand, BlacklistSwitch blacklistSwitch, BlacklistSwitchOn blacklistSwitchOn, BlacklistSwitchOff blacklistSwitchOff,
                               SettingsGroup settingsGroup, BlacklistUnban blacklistUnban, BlacklistBannedUsersList blacklistBannedUsers, CloseMessage closeMessage) {
        this.startGroupCommand = startGroupCommand;
        this.settingsGroup = settingsGroup;
        this.blacklistSwitch = blacklistSwitch;
        this.blacklistSwitchOn = blacklistSwitchOn;
        this.blacklistSwitchOff = blacklistSwitchOff;
        this.blacklistUnban = blacklistUnban;
        this.blacklistBannedUsers = blacklistBannedUsers;
        this.closeMessage = closeMessage;

        this.commands = new HashMap<>();
        this.buttonCommands = new HashMap<>();

        initializeCommands();
    }

    public void handleCommand(Update update) throws IOException {
        if (update.getCallbackQuery() != null) {
            handleCallBackQuery(update);
        } else {
            handleTextMessage(update);
        }
    }

    private void handleCallBackQuery(Update update) throws IOException {
        String callbackData = update.getCallbackQuery().getData();
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        long userId = update.getCallbackQuery().getFrom().getId();

        BaseGroupCommand command = buttonCommands.get(callbackData);

        if (command == null) return;

        if (command.hasPermission(Long.valueOf(chatId), userId)) {
            command.execute(chatId, bot, this);
        }
    }

    private void handleTextMessage(Update update) throws IOException {
        Long chatId = update.getMessage().getChatId();
        String message = update.getMessage().getText();
        Long userId = update.getMessage().getFrom().getId();
        lastMessage = message;

        if (message == null || !message.startsWith("/")) return;

        String commandKey = message.split(" ")[0].replace(bot.getBotTag(), "");
        BaseGroupCommand command = commands.get(commandKey);

        if (command == null) return;
        System.out.println(command.hasPermission(chatId, userId));
        if (command.hasPermission(chatId, userId)) {
            command.execute(chatId.toString(), bot, this);
        } else {
            sendNoPermissionMessageToUser(userId, command);
        }

    }

    private void sendNoPermissionMessage(Long chatId, BaseGroupCommand baseGroupCommand) {
        try {
            bot.execute(new SendMessage(chatId.toString(), "✨У вас нет прав для выполнения этой команды\n" + "Нужны права: " + baseGroupCommand.getPermission()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNoPermissionMessageToUser(Long userId, BaseGroupCommand baseGroupCommand) {
        SendMessage dm = new SendMessage();
        dm.setChatId(7009707687L);
        dm.setText("✨У вас нет прав для выполнения команды " + baseGroupCommand.getCommandName() + "\nНужны права: " + baseGroupCommand.getPermission());
        try {
            bot.execute(dm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeCommands() {
        commands.put(startGroupCommand.getCommandName(), startGroupCommand);
        commands.put(blacklistSwitch.getCommandName(), blacklistSwitch);
        commands.put(settingsGroup.getCommandName(), settingsGroup);

        buttonCommands.put(blacklistSwitch.getCommandName(), blacklistSwitch);
        buttonCommands.put("blacklistStartGroup", blacklistSwitch);

        buttonCommands.put(blacklistSwitchOn.getCommandName(), blacklistSwitchOn);
        buttonCommands.put(blacklistSwitchOff.getCommandName(), blacklistSwitchOff);

        buttonCommands.put(blacklistUnban.getCommandName(), blacklistUnban);

        buttonCommands.put(settingsGroup.getCommandName(), settingsGroup);
        buttonCommands.put(blacklistBannedUsers.getCommandName(), blacklistBannedUsers);
        //buttonCommands.put("settingsStartGroup", settingsGroup);

        buttonCommands.put(closeMessage.getCommandName(), closeMessage);
    }

    public String getLastMessage() {
        return this.lastMessage;
    }
}
