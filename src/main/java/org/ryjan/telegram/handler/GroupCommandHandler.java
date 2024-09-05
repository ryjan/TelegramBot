package org.ryjan.telegram.handler;

import org.ryjan.telegram.commands.groups.administration.StartGroupCommand;
import org.ryjan.telegram.commands.interfaces.IBotCommand;
import org.ryjan.telegram.commands.interfaces.IBotGroupCommand;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class GroupCommandHandler {

    private final StartGroupCommand startGroupCommand;

    private final Map<String, IBotGroupCommand> commands;
    private final Map<String, IBotGroupCommand> buttonCommands;
    private String lastMessage;

    @Autowired
    @Lazy
    private BotMain bot;

    public GroupCommandHandler(StartGroupCommand startGroupCommand) {
        this.startGroupCommand = startGroupCommand;
        this.commands = new HashMap<>();
        this.buttonCommands = new HashMap<>();

        initializeCommands();
    }

    public void handleCommand(Update update) throws IOException {
        if (update.getCallbackQuery() != null) {
            String callbackData = update.getCallbackQuery().getData();
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();

            IBotGroupCommand command = buttonCommands.get(callbackData);

            if (command != null) {
                command.execute(chatId, bot, this);
            } else {
                bot.sendMessage(chatId, "Неизвестная команда!");
            }
        } else {
            String chatId = update.getMessage().getChatId().toString();
            String message = update.getMessage().getText();
            lastMessage = message;
            String commandKey = message.split(" ")[0].replace(bot.getBotTag(), "");
            System.out.println(bot.getBotUsername());
            System.out.println(commandKey);
            IBotGroupCommand command = commands.get(commandKey);
            System.out.println(command);

            if (command != null) {
                command.execute(chatId, bot, this);
            } else {
                bot.sendMessage(chatId, "Неизвестная команда!");
            }
        }
    }

    private void initializeCommands() {
        commands.put(startGroupCommand.getCommandName(), startGroupCommand);
    }

    public String getLastMessage() {
        return this.lastMessage;
    }
}
