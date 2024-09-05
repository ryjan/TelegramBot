package org.ryjan.telegram.commands.groups;

import org.ryjan.telegram.commands.interfaces.IBotGroupCommand;
import org.ryjan.telegram.handler.ButtonCommandHandler;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

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

    protected void sendMessageForCommand(BotMain bot, SendMessage message) {
        try {
            bot.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
