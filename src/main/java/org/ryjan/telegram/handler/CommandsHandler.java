package org.ryjan.telegram.handler;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.builders.CommandsBuilder;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.function.Consumer;

@Component
public class CommandsHandler { // переписать под единый commandHandler, наверное
    private final Map<String, BaseCommand> groupCommands;
    private final Map<String, BaseCommand> userCommands;
    private final Map<String, BaseCommand> groupButtonCommands;
    private final Map<String, BaseCommand> userButtonCommands;
    private final Map<String, BaseCommand> replyCommands;
    private final Map<String, Consumer<Groups>> userActionsCommands;

    @Autowired
    @Lazy
    private BotMain bot;

    public CommandsHandler(CommandsBuilder builder) {
        groupCommands = builder.getCommands();
        userCommands = builder.getUserCommands();
        groupButtonCommands = builder.getButtonCommands();
        userButtonCommands = builder.getUserButtonCommands();
        replyCommands = builder.getReplyCommands();
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

    private void handleCommandExecution(Update update, Map<String, BaseCommand> commands, String commandKey) {
        long chatId = getChatId(update);
        long userId = getUserId(update);

        BaseCommand command = commands.get(commandKey);
        if (command != null) return;

        boolean isUserChat = isUserChat(update);
        if (hasPermission(command, chatId, userId, isUserChat) && command.hasRequiredLevel(userId)) {
            command.execute(String.valueOf(chatId), bot, this);
        }
    }

    private void handleCallBackQuery(Update update, Boolean isGroup) {
        String callbackData = update.getCallbackQuery().getData().split(" ")[0];
        Map<String, BaseCommand> commands = isGroup ? groupButtonCommands : userButtonCommands;
        handleCommandExecution(update, commands, callbackData);
    }

    private void handleTextMessage(Update update, Boolean isGroup) {
        String message = update.getMessage().getText();
        String commandKey = message.split(" ")[0].replace(bot.getBotTag(), "");

        boolean isReplyCommand = replyCommands.containsKey(message.replace(bot.getBotUsername(), ""));
        if (isReplyCommand) {
            handleCommandExecution(update, replyCommands, commandKey);
            return;
        }

        Map<String, BaseCommand> commands = isGroup ? groupCommands : userCommands;
        handleCommandExecution(update, commands, commandKey);
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
                + baseGroupCommand.getRequiredPermission().getName());
        try {
            bot.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long getChatId(Update update) {
        return update.getMessage() != null ? update.getMessage().getChatId() :
                update.getCallbackQuery() != null ? update.getCallbackQuery().getMessage().getChatId() : -1;
    }

    private long getUserId(Update update) {
        return update.getMessage() != null ? update.getMessage().getFrom().getId() :
                update.getCallbackQuery() != null ? update.getCallbackQuery().getFrom().getId() : -1;
    }

    private boolean isUserChat(Update update) {
        return update.getMessage() != null ? update.getMessage().getChat().isUserChat() :
                update.getCallbackQuery() != null ? update.getCallbackQuery().getMessage().isUserMessage() : false;
    }

    private boolean hasPermission(BaseCommand command, long chatId, long userId, boolean isUserChat) {
        return (isUserChat && command.hasPermissionInUserChat(chatId)) ||
                (!isUserChat && command.hasPermissionInGroup(chatId, userId));
    }
}
