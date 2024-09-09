package org.ryjan.telegram.handler;

import org.ryjan.telegram.commands.groups.administration.BlacklistSwitch;
import org.ryjan.telegram.commands.groups.administration.BlacklistSwitchOff;
import org.ryjan.telegram.commands.groups.administration.BlacklistSwitchOn;
import org.ryjan.telegram.commands.groups.administration.StartGroup;
import org.ryjan.telegram.commands.interfaces.IBotGroupCommand;
import org.ryjan.telegram.commands.users.utils.KeyboardBuilder;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class GroupCommandHandler {

    private final StartGroup startGroupCommand;
    private final BlacklistSwitch blacklistSwitch;
    private final BlacklistSwitchOn blacklistSwitchOn;
    private final BlacklistSwitchOff blacklistSwitchOff;

    private final Map<String, IBotGroupCommand> commands;
    private final Map<String, IBotGroupCommand> buttonCommands;
    private String lastMessage;

    @Autowired
    @Lazy
    private BotMain bot;

    public GroupCommandHandler(StartGroup startGroupCommand, BlacklistSwitch blacklistSwitch, BlacklistSwitchOn blacklistSwitchOn, BlacklistSwitchOff blacklistSwitchOff) {
        this.startGroupCommand = startGroupCommand;
        this.blacklistSwitch = blacklistSwitch;
        this.blacklistSwitchOn = blacklistSwitchOn;
        this.blacklistSwitchOff = blacklistSwitchOff;

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

        IBotGroupCommand command = buttonCommands.get(callbackData);

        if (command != null) {
            command.execute(chatId, bot, this);
        } else {
            bot.sendMessage(chatId, "Неизвестная команда1!");
        }
    }

    private void handleTextMessage(Update update) throws IOException {
        String chatId = update.getMessage().getChatId().toString();
        String message = update.getMessage().getText();
        lastMessage = message;
        if (message.startsWith("/")) {
            String commandKey = message.split(" ")[0].replace(bot.getBotTag(), "");
            System.out.println(bot.getBotUsername());
            System.out.println(commandKey);
            IBotGroupCommand command = commands.get(commandKey);
            System.out.println(command);

            if (command != null) {
                command.execute(chatId, bot, this);
            }
        }
    }

    private void initializeCommands() {
        commands.put(startGroupCommand.getCommandName(), startGroupCommand);
        commands.put(blacklistSwitch.getCommandName(), blacklistSwitch);

        buttonCommands.put(blacklistSwitch.getCommandName(), blacklistSwitch);
        buttonCommands.put(blacklistSwitchOn.getCommandName(), blacklistSwitchOn);
        buttonCommands.put(blacklistSwitchOff.getCommandName(), blacklistSwitchOff);
    }

    public String getLastMessage() {
        return this.lastMessage;
    }
}
