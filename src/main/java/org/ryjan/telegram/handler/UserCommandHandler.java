package org.ryjan.telegram.handler;

import org.ryjan.telegram.commands.users.user.BaseUserCommand;
import org.ryjan.telegram.commands.interfaces.IBotCommand;
import org.ryjan.telegram.builders.UserCommandsBuilder;
import org.ryjan.telegram.main.BotMain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.Map;

@Component
public class UserCommandHandler {

    @Autowired
    @Lazy
    private BotMain bot;

    private final Map<String, BaseUserCommand>  commands;
    private final Map<String, BaseUserCommand> buttonCommands;

    @Autowired
    public UserCommandHandler(UserCommandsBuilder userCommandsBuilder) {
        this.commands = userCommandsBuilder.getCommands();
        this.buttonCommands = userCommandsBuilder.getButtonCommands();
        userCommandsBuilder.initializeCommands();
    }

    public void handleCommand(Update update) throws IOException {
        if (update.getCallbackQuery() != null) {
            String callbackData = update.getCallbackQuery().getData();
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();

            IBotCommand command = buttonCommands.get(callbackData);

            if (command != null) {
                command.execute(chatId, bot, this);
            } else {
                bot.sendMessage(chatId, "Неизвестная команда!");
            }
        } else {
            String chatId = update.getMessage().getChatId().toString();
            String message = update.getMessage().getText();
            //lastMessage = message;
            String commandKey = message.split(" ")[0];
            IBotCommand command = commands.get(commandKey);

            if (command != null) {
                command.execute(chatId, bot, this);
            }
        }
    }

}
