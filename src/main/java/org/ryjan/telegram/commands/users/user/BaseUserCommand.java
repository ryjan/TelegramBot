package org.ryjan.telegram.commands.users.user;

import org.ryjan.telegram.commands.interfaces.IBotCommand;
import org.ryjan.telegram.utils.Translation;
import org.ryjan.telegram.services.UserService;
import org.ryjan.telegram.model.users.UserDatabase;
import org.ryjan.telegram.handler.UserCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.utils.UpdateContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public abstract class BaseUserCommand extends Translation implements IBotCommand {
    private final String commandName;
    private final String description;

    @Autowired
    protected UserService userService;


    protected BaseUserCommand(String commandName, String description) {
        this.commandName = commandName;
        this.description = description;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getDescription() {
        return description;
    }

    protected Update getUpdate() {
        return UpdateContext.getInstance().getUpdate();
    }

    protected UserDatabase getFromUserDatabase() {
        return userService.findUser(getUpdate().getMessage().getFrom().getId());
    }

    protected UserDatabase findUserDatabase(String username) {
        return userService.findUser(username);
    }

    protected boolean userIsExist(String username) {
        return userService.findUser(username) != null;
    }

    protected UserDatabase getToUserDatabase(long id) {
        return null;
    }

    protected Long getChatId() {
        return getUpdate().getMessage().getFrom().getId();
    }

    protected String getUsername() {
        return getUpdate().getMessage().getFrom().getUserName();
    }

    protected SendMessage createSendMessage() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(getChatId());
        return sendMessage;
    }

    protected SendMessage createSendMessage(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        return sendMessage;
    }

    protected String[] getParts(String command, int expectedParts) {
        return getUpdate().getMessage().getText().replace(command, "").trim().split(" ", expectedParts);
    }

    protected void sendMessageForCommand(BotMain bot, SendMessage message) {
        try {
            bot.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void executeCommand(String chatId, BotMain bot, UserCommandHandler userCommandHandler);

    @Override
    public void execute(String chatId, BotMain bot, UserCommandHandler userCommandHandler) {
        try {
            executeCommand(chatId, bot, userCommandHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
