package org.ryjan.telegram.commands.groups;

import org.ryjan.telegram.commands.interfaces.IBotGroupCommand;
import org.ryjan.telegram.handler.ButtonCommandHandler;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.utils.UpdateContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class BaseGroupCommand implements IBotGroupCommand {
    private final String commandName;
    private final String description;

    protected BaseGroupCommand(String commandName, String description) {
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

    protected SendMessage createSendMessage(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        return sendMessage;
    }

    protected SendMessage createSendMessage(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        return sendMessage;
    }

    protected void sendMessageForCommand(BotMain bot, SendMessage message) {
        try {
            bot.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String[] getParts(String command, int expectedParts) {
        return getUpdate().getMessage().getText().replace(command, "").trim().split(" ", expectedParts);
    }

    protected abstract void executeCommand(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler);

    @Override
    public void execute(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler) {
        try {
            executeCommand(chatId, bot, groupCommandHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
