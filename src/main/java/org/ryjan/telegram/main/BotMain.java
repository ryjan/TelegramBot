package org.ryjan.telegram.main;


import com.sun.tools.javac.Main;

import org.ryjan.telegram.model.UserDatabase;
import org.ryjan.telegram.services.UserService;
import org.ryjan.telegram.handler.ButtonCommandHandler;

import org.ryjan.telegram.utils.UpdateContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.User;

@SpringBootApplication
public class BotMain extends TelegramLongPollingBot {
    @Autowired
    private UserService userService;
    @Autowired
    private ButtonCommandHandler buttonCommandHandler;
    @Value("${bot.token}")
    private String token;
    @Value("${bot.username}")
    private String username;

    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final String OWNER_ID = "2323";

    public void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            LOGGER.error("Error occurred while sending message(sendMessage)", e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String receivedMessage = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            UpdateContext.getInstance().setUpdate(update);
            User user = update.getMessage().getFrom();
        }

        try {
            UserDatabase userDatabase = userService.findUser("Ryjan4ik");
            buttonCommandHandler.handleCommand(update);
        } catch (Exception e) {
            LOGGER.error("Error occurred while sending message(onUpdateReceived)", e);
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    public Logger getLogger() {
        return LOGGER;
    }
}