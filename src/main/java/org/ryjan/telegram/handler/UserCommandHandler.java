package org.ryjan.telegram.handler;

import org.ryjan.telegram.commands.users.utils.BaseUserCommand;
import org.ryjan.telegram.commands.interfaces.IBotCommand;
import org.ryjan.telegram.commands.users.utils.InlineKeyboardBuilder;
import org.ryjan.telegram.commands.users.utils.UserCommandsBuilder;
import org.ryjan.telegram.main.BotMain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Map;

@Component
public class UserCommandHandler {

    @Autowired
    @Lazy
    private BotMain bot;

    private final Map<String, BaseUserCommand>  commands;
    private final Map<String, BaseUserCommand> buttonCommands;
    private String lastMessage;

    @Autowired
    public UserCommandHandler(UserCommandsBuilder userCommandsBuilder) {
        this.commands = userCommandsBuilder.getCommands();
        this.buttonCommands = userCommandsBuilder.getButtonCommands();
        userCommandsBuilder.initializeCommands();
    }

    public void sendMenu(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите опцию: ");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder.KeyboardLayer keyboard = new InlineKeyboardBuilder.KeyboardLayer()
                        .addRow(new InlineKeyboardBuilder.ButtonRow()
                                .addButton("Владелец", "owner")
                                .addButton("ChatGPT-4", "askchatgpt"));
        inlineKeyboardMarkup.setKeyboard(keyboard.build());


        message.setReplyMarkup(inlineKeyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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
            lastMessage = message;
            String commandKey = message.split(" ")[0];
            IBotCommand command = commands.get(commandKey);

            if (command != null) {
                command.execute(chatId, bot, this);
            }
        }
    }

    public boolean containsCommand(String command) {
        return buttonCommands.containsKey(command);
    }

    public String getLastMessage() {
        return this.lastMessage;
    }

}
