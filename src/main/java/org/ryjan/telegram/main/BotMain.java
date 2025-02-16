package org.ryjan.telegram.main;


import com.sun.tools.javac.Main;

import org.ryjan.telegram.model.users.Articles;
import org.ryjan.telegram.services.ArticlesService;
import org.ryjan.telegram.services.BotService;

import org.ryjan.telegram.utils.UpdateContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.InputStream;
import java.util.List;

@Component
public class BotMain extends TelegramLongPollingBot {

    @Autowired
    private BotService botService;
    @Value("${bot.token}")
    private String token;
    @Value("${bot.username}")
    private String username;
    @Value("${bot.tag}")
    private String tag;

    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final String OWNER_ID = "7009707687";

    private Update update;

    public void sendMessage(String chatId, String text) throws TelegramApiException {
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
        setUpdate(update);
        UpdateContext.getInstance().setUpdate(update);
        botService.autoExecute(update);

        try {
            if (update.hasMessage() && update.getMessage().getChat().isUserChat() || update.hasCallbackQuery()
                    && update.getCallbackQuery().getMessage().isUserMessage()) {
                botService.groupCommandHandler.handleCommand(update, false);
            } else {
                botService.groupCommandHandler.handleCommand(update, true);
            }

        } catch (Exception e) {
            LOGGER.error("Error occurred while sending message(onUpdateReceived)", e);
        }
    }

    public String getBotTag() {
        return tag;
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

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }
}