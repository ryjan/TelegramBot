package org.ryjan.telegram.handler;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.builders.CommandsBuilder;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Groups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

@Component
public class CommandsHandler { // переписать под единый commandHandler, наверное
    private final Map<String, BaseCommand> groupCommands;
    private final Map<String, BaseCommand> userCommands;
    private final Map<String, BaseCommand> groupButtonCommands;
    private final Map<String, BaseCommand> userButtonCommands;
    private final Map<String, Consumer<Groups>> userActionsCommands;

    @Autowired
    @Lazy
    private BotMain bot;

    public CommandsHandler(CommandsBuilder builder) {
        groupCommands = builder.getCommands();
        userCommands = builder.getUserCommands();
        groupButtonCommands = builder.getButtonCommands();
        userButtonCommands = builder.getUserButtonCommands();
        userActionsCommands = builder.getUserActionsCommands();
        builder.initializeCommands();
    }

    public void handleCommand(Update update, Boolean isGroup) {
        if (update.getCallbackQuery() != null) {
            handleCallBackQuery(update, isGroup);
        } else {
            handleTextMessage(update, isGroup);
        }
    }

    private void handleCallBackQuery(Update update, Boolean isGroup) {
        if (isGroup) {
            System.out.println("List group:" + userButtonCommands);
            mergedHandleCallBackQuery(update, groupButtonCommands);
        } else {
            System.out.println("List user:" + userButtonCommands);
            mergedHandleCallBackQuery(update, userButtonCommands);
        }
    }

    private void handleTextMessage(Update update, Boolean isGroup) {
        String message = update.getMessage().getText();
        String commandKey = message.split(" ")[0].replace(bot.getBotTag(), "");

        if (isGroup) {
            mergedHandleTextMessage(update, groupCommands, commandKey);
        } else {
            mergedHandleTextMessage(update, userCommands, commandKey);
        }
    }

    private void mergedHandleTextMessage(Update update, Map<String, BaseCommand> commands, String commandKey) {
        long chatId = update.getMessage().getChatId();
        long userId = update.getMessage().getFrom().getId();
        BaseCommand command = commands.get(commandKey);

        if (command == null) return;

        if (update.getMessage().getChat().isUserChat() && command.hasPermissionInUserChat(chatId) || command.hasPermissionInGroup(chatId, userId)) {
            command.execute(String.valueOf(chatId), bot, this);
        } else {
            sendNoPermissionMessageToUser(userId, command);
        }
    }

    private void mergedHandleCallBackQuery(Update update, Map<String, BaseCommand> commands) {
        String callbackData = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        long userId = update.getCallbackQuery().getFrom().getId();
        boolean isUserChat = update.hasCallbackQuery() && update.getCallbackQuery().getMessage().isUserMessage();

        BaseCommand command = commands.get(callbackData);
        System.out.println("CallbackData: " + command);
        if (command == null) return;

        if (command.hasPermissionInUserChat(chatId) && isUserChat || command.hasPermissionInGroup(chatId, userId)) {
            command.execute(String.valueOf(chatId), bot, this);
        }

    }

    /*
    private void sendNoPermissionMessage(Long chatId, BaseCommand baseGroupCommand) {
        try {
            bot.execute(new SendMessage(chatId.toString(), "✨У вас нет прав для выполнения этой команды\n"
                    + "Нужны права: " + baseGroupCommand.getPermission().getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     */

    private void sendNoPermissionMessageToUser(Long userId, BaseCommand baseGroupCommand) {
        SendMessage message = new SendMessage();
        message.setChatId(userId);
        message.setText("✨У вас нет прав для выполнения команды " + baseGroupCommand.getCommandName() + "\nНужны права: "
                + baseGroupCommand.getPermission().getName());
        try {
            bot.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
