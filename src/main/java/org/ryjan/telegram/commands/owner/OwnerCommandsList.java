package org.ryjan.telegram.commands.owner;

import org.ryjan.telegram.commands.user.button.OwnerCommand;
import org.ryjan.telegram.commands.interfaces.IBotCommand;
import org.ryjan.telegram.handler.ButtonCommandHandler;
import org.ryjan.telegram.BotMain;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;
import java.util.HashMap;

public class OwnerCommandsList implements IBotCommand {

    @Override
    public void execute(String chatId, BotMain bot, ButtonCommandHandler buttonCommandHandler) throws IOException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        StringBuilder sb = new StringBuilder();

        HashMap<String, IBotCommand> commands = getCommands();

        for (int i = 0; i < commands.size(); i++) {
            sb.append(commands.keySet().toArray()[i]).append("\n");
        }

        message.setText(sb.toString());

        buttonCommandHandler.sendMessageForCommand(bot, message);
    }

    public HashMap<String, IBotCommand> getCommands() {
        HashMap<String, IBotCommand> commands = new HashMap<>();
        commands.put("/helpowner", new OwnerCommandsList());
        commands.put("/setcoins", new OwnerCommand());
        return commands;
    }

   /* public HashMap<HashMap<String, IBotCommand>, String> getCommandsWithComments() {
        HashMap<String, IBotCommand> commandsIBotCommands = getCommands();
        HashMap<String, String> commands = new HashMap<>();

        for (int i = 0; i < commandsIBotCommands.size(); i++) {
            String command = commandsIBotCommands.keySet().toArray()[i].toString();
            commands.put()
        }
    }
*/}
