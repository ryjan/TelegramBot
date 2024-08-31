package org.ryjan.telegram;


import com.sun.tools.javac.Main;

import org.ryjan.telegram.commands.user.UserService;
import org.ryjan.telegram.handler.ButtonCommandHandler;
import org.ryjan.telegram.config.BotConfig;
import org.ryjan.telegram.services.BotService;
import org.ryjan.telegram.utils.UpdateContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class BotMain extends TelegramLongPollingBot {
    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.username}")
    private String botUsername;
    private static final String OWNER_ID = "2323";

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    @Autowired
    private UserService userService;
    @Autowired
    private BotService botService;

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
            botService.handleUpdate(update);
        } catch (Exception e) {
            LOGGER.error("Error occurred while sending message(onUpdateReceived)", e);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public Logger getLogger() {
        return LOGGER;
    }
}