package org.ryjan.telegram.commands;

import org.ryjan.telegram.commands.user.StartCommand;
import org.ryjan.telegram.interfaces.IBotCommand;
import org.ryjan.telegram.main.BotMain;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ButtonCommandHandler { // сделать IBotCommand абстрактным классом

    private final BotMain bot;
    private final Map<String, IBotCommand> commands;
    // сделать List buttons и сделать все по удобному
    public ButtonCommandHandler(BotMain bot) {
        this.bot = bot;
        this.commands = new HashMap<>();

        commands.put("start", new StartCommand());
    }

    public void sendMenu(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите опцию: ");

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>(); // отсюда(создание кнопки)
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Нажми");
        button.setCallbackData("start"); // до сюда

        row.add(button);

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void handleCommand(Update update) {
        if (update.getCallbackQuery() != null) {
            String callbackData = update.getCallbackQuery().getData();
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();

            IBotCommand command = commands.get(callbackData);

            if (command != null) {
                command.execute(chatId, bot);
            } else {
                bot.sendMessage(chatId, "Неизвестная команда!");
            }
        } else {
            String chatId = update.getMessage().getChatId().toString();
            IBotCommand command = new StartCommand();
            command.execute(chatId, bot);
        }
    }

    public boolean containsCommand(String command) {
        return commands.containsKey(command);
    }

}
