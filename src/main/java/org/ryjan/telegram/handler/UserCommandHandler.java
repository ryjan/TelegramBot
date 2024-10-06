package org.ryjan.telegram.handler;

import org.ryjan.telegram.commands.users.SendCoins;
import org.ryjan.telegram.commands.users.SetCoins;
import org.ryjan.telegram.commands.users.user.button.OwnerCommand;
//import org.ryjan.telegram.commands.user.button.StartCommand;
import org.ryjan.telegram.commands.interfaces.IBotCommand;
//import org.ryjan.telegram.commands.users.SetCoins;
import org.ryjan.telegram.commands.users.user.button.bugreport.UserBugReport;
import org.ryjan.telegram.commands.users.user.button.bugreport.UserSendReportReply;
import org.ryjan.telegram.commands.users.utils.InlineKeyboardBuilder;
import org.ryjan.telegram.main.BotMain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserCommandHandler {

    private final SendCoins sendCoins;
    private final SetCoins setCoins;
    private final UserBugReport userBugReport;
    private final UserSendReportReply userSendReportReply;

    @Autowired
    @Lazy
    private BotMain bot;

    private final Map<String, IBotCommand>  nonButtonCommands;
    private final Map<String, IBotCommand> commands;
    private String lastMessage;

    @Autowired
    public UserCommandHandler(SendCoins sendCoins, SetCoins setCoins, UserBugReport userBugReport, UserSendReportReply userSendReportReply) {
        this.nonButtonCommands = new HashMap<>();
        this.commands = new HashMap<>();
        this.sendCoins = sendCoins;
        this.setCoins = setCoins;
        this.userBugReport = userBugReport;
        this.userSendReportReply = userSendReportReply;
        initializeCommands();
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
        nonButtonCommands.put(setCoins.getCommandName(), setCoins);
        nonButtonCommands.put(sendCoins.getCommandName(), sendCoins);
        nonButtonCommands.put(userBugReport.getCommandName(), userBugReport);
        nonButtonCommands.put(userSendReportReply.getCommandName().split(" ")[0], userSendReportReply);

        commands.put("owner", new OwnerCommand());
    }

    public boolean containsCommand(String command) {
        return commands.containsKey(command);
    }

    public String getLastMessage() {
        return this.lastMessage;
    }

}
