package org.ryjan.telegram.commands.utils;

import org.ryjan.telegram.commands.button.user.OwnerCommand;
import org.ryjan.telegram.commands.button.user.QuestionChatGPTCommand;
import org.ryjan.telegram.commands.button.user.StartCommand;
import org.ryjan.telegram.commands.interfaces.IBotCommand;
import org.ryjan.telegram.commands.owner.SetCoins;
import org.ryjan.telegram.main.BotMain;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ButtonCommandHandler { // сделать IBotCommand абстрактным классом

    private final BotMain bot;
    private final Map<String, IBotCommand>  nonButtonCommands;
    private final Map<String, IBotCommand> commands;

    // сделать List buttons и сделать все по удобному
    public ButtonCommandHandler(BotMain bot) {
        this.bot = bot;
        this.nonButtonCommands = new HashMap<>();
        this.commands = new HashMap<>();

        nonButtonCommands.put("/start", new StartCommand());
        nonButtonCommands.put("/owner", new OwnerCommand());
        nonButtonCommands.put("/askchatgpt", new QuestionChatGPTCommand());
        nonButtonCommands.put("/setcoins", new SetCoins());

        commands.put("owner", new OwnerCommand());
        commands.put("askchatgpt", new QuestionChatGPTCommand());
    }

    public void sendMenu(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите опцию: ");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        KeyboardBuilder.KeyboardLayer keyboard = new KeyboardBuilder.KeyboardLayer()
                        .addRow(new KeyboardBuilder.ButtonRow()
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

            IBotCommand command = commands.get(callbackData);

            if (command != null) {
                command.execute(chatId, bot, this);
            } else {
                bot.sendMessage(chatId, "Неизвестная команда!");
            }
        } else {
            String chatId = update.getMessage().getChatId().toString();
            String message = update.getMessage().getText();
            IBotCommand command = nonButtonCommands.get(message);

            if (command != null) {
                command.execute(chatId, bot, this);
            } else {
                bot.sendMessage(chatId, "Неизвестная команда!");
            }
        }
    }

    public boolean containsCommand(String command) {
        return commands.containsKey(command);
    }

}
