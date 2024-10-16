package org.ryjan.telegram.handler;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.builders.CommandsBuilder;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.Map;

@Component
public class CommandsHandler { // переписать под единый commandHandler, наверное
    private final Map<String, BaseCommand> groupCommands;
    private final Map<String, BaseCommand> userCommands;
    private final Map<String, BaseCommand> groupButtonCommands;
    private final Map<String, BaseCommand> userButtonCommands;

    @Autowired
    @Lazy
    private BotMain bot;

    public CommandsHandler(CommandsBuilder builder) {
        groupCommands = builder.getCommands();
        userCommands = builder.getUserCommands();
        groupButtonCommands = builder.getButtonCommands();
        userButtonCommands = builder.getUserButtonCommands();
        builder.initializeCommands();
    }

    public void handleCommand(Update update, Boolean isGroup) throws IOException {
        if (update.getCallbackQuery() != null) {
            handleCallBackQuery(update, isGroup);
        } else {
            handleTextMessage(update, isGroup);
        }
    }

    private void handleCallBackQuery(Update update, Boolean isGroup) throws IOException {
        String callbackData = update.getCallbackQuery().getData();
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        long userId = update.getCallbackQuery().getFrom().getId();

        BaseCommand command;
        if (isGroup) {
            command = groupButtonCommands.get(callbackData);
        } else {
            command = userButtonCommands.get(callbackData);
        }

        if (command == null) return;

        if (command.hasPermissionInGroup(Long.valueOf(chatId), userId)) {
            command.execute(chatId, bot, this);
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
        Long chatId = update.getMessage().getChatId();
        Long userId = update.getMessage().getFrom().getId();
        BaseCommand command = commands.get(commandKey);

        if (command == null) return;

        if (command.hasPermissionInUserChat(chatId) && update.getMessage().getChat().isUserChat() || command.hasPermissionInGroup(chatId, userId)) {
            command.execute(chatId.toString(), bot, this);
        } else {
            sendNoPermissionMessageToUser(chatId, command);
        }
    }

    private void sendNoPermissionMessage(Long chatId, BaseCommand baseGroupCommand) {
        try {
            bot.execute(new SendMessage(chatId.toString(), "✨У вас нет прав для выполнения этой команды\n" + "Нужны права: " + baseGroupCommand.getPermission()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNoPermissionMessageToUser(Long userId, BaseCommand baseGroupCommand) {
        SendMessage dm = new SendMessage();
        dm.setChatId(userId);
        dm.setText("✨У вас нет прав для выполнения команды " + baseGroupCommand.getCommandName() + "\nНужны права: " + baseGroupCommand.getPermission());
        try {
            bot.execute(dm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
