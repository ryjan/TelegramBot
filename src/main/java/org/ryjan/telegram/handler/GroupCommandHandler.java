package org.ryjan.telegram.handler;

import org.ryjan.telegram.commands.groups.BaseGroupCommand;
import org.ryjan.telegram.commands.groups.administration.BlacklistSwitch;
import org.ryjan.telegram.commands.groups.administration.BlacklistSwitchOff;
import org.ryjan.telegram.commands.groups.administration.BlacklistSwitchOn;
import org.ryjan.telegram.commands.groups.administration.StartGroup;
import org.ryjan.telegram.commands.interfaces.IBotGroupCommand;
import org.ryjan.telegram.commands.users.BaseCommand;
import org.ryjan.telegram.commands.users.utils.KeyboardBuilder;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
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

    private final Map<String, BaseGroupCommand> commands;
    private final Map<String, BaseGroupCommand> buttonCommands;
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
            bot.sendMessage(chatId, "Неизвестная команда!");
        }
    }

    private void handleTextMessage(Update update) throws IOException {
        Long chatId = update.getMessage().getChatId();
        String message = update.getMessage().getText();
        Long userId = update.getMessage().getFrom().getId();
        lastMessage = message;

        if (!message.startsWith("/")) return;

        String commandKey = message.split(" ")[0].replace(bot.getBotTag(), "");
        System.out.println(bot.getBotUsername());
        System.out.println(commandKey);
        BaseGroupCommand command = commands.get(commandKey);
        System.out.println(command);

        if (command == null) return;
        System.out.println(command.hasPermission(chatId, userId));
        if (command.hasPermission(chatId, userId)) {
            command.execute(chatId.toString(), bot, this);
        } else {
            sendNoPermissionMessage(chatId, command);
        }

    }

    private void sendNoPermissionMessage(Long chatId, BaseGroupCommand baseGroupCommand) {
        try {
            bot.execute(new SendMessage(chatId.toString(), "✨У вас нет прав для выполнения этой команды\n" + "Нужны права: " + baseGroupCommand.getPermission()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeCommands() {
        commands.put(startGroupCommand.getCommandName(), startGroupCommand);
        commands.put(blacklistSwitch.getCommandName(), blacklistSwitch);

        buttonCommands.put(blacklistSwitch.getCommandName(), blacklistSwitch);
        buttonCommands.put(blacklistSwitchOn.getCommandName(), blacklistSwitchOn);
        buttonCommands.put(blacklistSwitchOff.getCommandName(), blacklistSwitchOff);
        buttonCommands.put("blacklistStartGroup", blacklistSwitch);
    }

    public String getLastMessage() {
        return this.lastMessage;
    }
}
