package org.ryjan.telegram.commands;

import org.ryjan.telegram.commands.interfaces.IBotCommand;
import org.ryjan.telegram.database.UserDatabase;
import org.ryjan.telegram.handler.ButtonCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.services.UserService;
import org.ryjan.telegram.utils.UpdateContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class BaseCommand implements IBotCommand {
    protected UserService userService;

    protected BaseCommand() {
        this.userService = new UserService();
    }

    protected Update getUpdate() {
        return UpdateContext.getInstance().getUpdate();
    }

    protected UserDatabase getFromUserDatabase() {
        return userService.findUser(getUpdate().getMessage().getFrom().getId());
    }

    protected UserDatabase getToUserDatabase(String username) {
        return userService.findUser(username);
    }

    protected UserDatabase getToUserDatabase(long id) {
        return userService.findUser(id);
    }

    protected UserService getUserService() {
        return userService;
    }

    protected long getChatId() {
        return getUpdate().getMessage().getFrom().getId();
    }

    protected String getUsername() {
        return getUpdate().getMessage().getFrom().getUserName();
    }

    protected String userNotFound(String username) {
        return "Пользователь " + username + " не найден😥";
    }

    protected SendMessage createSendMessage() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(getChatId());
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

    protected abstract void executeCommand(String chatId, BotMain bot, ButtonCommandHandler buttonCommandHandler);
    public abstract String getCommand();
    public abstract String getDescription();

    @Override
    public void execute(String chatId, BotMain bot, ButtonCommandHandler buttonCommandHandler) {
        try {
            executeCommand(chatId, bot, buttonCommandHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
