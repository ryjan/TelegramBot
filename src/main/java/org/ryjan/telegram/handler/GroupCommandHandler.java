package org.ryjan.telegram.handler;

import org.ryjan.telegram.commands.groups.BaseGroupCommand;
import org.ryjan.telegram.commands.groups.config.GroupCommandsBuilder;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.Map;

@Component
public class GroupCommandHandler {
    private final Map<String, BaseGroupCommand> commands;
    private final Map<String, BaseGroupCommand> buttonCommands;

    private String lastMessage;

    @Autowired
    @Lazy
    private BotMain bot;

    public GroupCommandHandler(GroupCommandsBuilder builder) {
        commands = builder.getCommands();
        buttonCommands = builder.getButtonCommands();
        builder.initializeCommands();
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

    public String getLastMessage() {
        return this.lastMessage;
    }
}
