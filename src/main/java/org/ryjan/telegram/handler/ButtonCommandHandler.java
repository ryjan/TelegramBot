package org.ryjan.telegram.handler;

import org.ryjan.telegram.commands.user.SendCoins;
import org.ryjan.telegram.commands.user.button.OwnerCommand;
import org.ryjan.telegram.commands.user.button.QuestionChatGPTCommand;
//import org.ryjan.telegram.commands.user.button.StartCommand;
import org.ryjan.telegram.commands.interfaces.IBotCommand;
import org.ryjan.telegram.commands.owner.OwnerCommandsList;
//import org.ryjan.telegram.commands.owner.SetCoins;
import org.ryjan.telegram.commands.utils.KeyboardBuilder;
import org.ryjan.telegram.BotMain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ButtonCommandHandler { // сделать IBotCommand абстрактным классом
    private final BotMain bot;
    private final Map<String, IBotCommand>  nonButtonCommands;
    private final Map<String, IBotCommand> commands;
    private String lastMessage;

    // сделать List buttons и сделать все по удобному
    @Autowired
    public ButtonCommandHandler(BotMain bot, SendCoins sendCoins) {
        this.bot = bot;
        this.nonButtonCommands = new HashMap<>();
        this.commands = new HashMap<>();
        nonButtonCommands.put(sendCoins.getCommandName(), sendCoins);

        //initializeCommands();
    }

    public ButtonCommandHandler() {
        this.nonButtonCommands = new HashMap<>();
        this.commands = new HashMap<>();
        nonButtonCommands.put(sendCoins.getCommandName(), sendCoins);
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
            lastMessage = message;
            String commandKey = message.split(" ")[0];
            IBotCommand command = nonButtonCommands.get(commandKey);

            if (command != null) {
                command.execute(chatId, bot, this);
            } else {
                bot.sendMessage(chatId, "Неизвестная команда!");
            }
        }
    }

    public void sendMessageForCommand(BotMain bot, SendMessage message) {
        try {
            bot.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeCommands() {

      //  nonButtonCommands.put("/start", new StartCommand());
        nonButtonCommands.put("/owner", new OwnerCommand());
     //   nonButtonCommands.put("/setcoins", new SetCoins());
        nonButtonCommands.put("/helpowner", new OwnerCommandsList());

        commands.put("owner", new OwnerCommand());
        commands.put("askchatgpt", new QuestionChatGPTCommand());
    }

    public boolean containsCommand(String command) {
        return commands.containsKey(command);
    }

    public String getLastMessage() {
        return this.lastMessage;
    }

}
