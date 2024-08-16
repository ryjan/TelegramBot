package org.ryjan.telegram.main;


import com.sun.tools.javac.Main;
import org.ryjan.telegram.commands.utils.ButtonCommandHandler;
import org.ryjan.telegram.config.BotConfig;
import org.ryjan.telegram.database.Bank;
import org.ryjan.telegram.database.User;
import org.ryjan.telegram.interfaces.UserDAO;
import org.ryjan.telegram.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.math.BigDecimal;

public class BotMain extends TelegramLongPollingBot {
    private final ButtonCommandHandler buttonCommandHandler;

    public BotMain() {
        buttonCommandHandler = new ButtonCommandHandler(this);
    }

    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final String OWNER_ID = "2323";

    public static void main(String[] args) {
        LOGGER.info("Starting Bot...");
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new BotMain());
            UserService userService = new UserService();
            User user = new User("Ryjan4ik", "Owner");
            Bank userBank = user.getBank();
            userBank.setGems(BigDecimal.valueOf(99999));
            userService.save(user);

            LOGGER.info("Bot started successfully!");
        } catch (TelegramApiException e) {
            LOGGER.error("Error occurred while initializing Bot", e);
        }
    }

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


        }

        try {
            buttonCommandHandler.handleCommand(update);
        } catch (Exception e) {
            LOGGER.error("Error occurred while sending message(onUpdateReceived)", e);
        }
    }

    @Override
    public String getBotUsername() {
        return BotConfig.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BotConfig.BOT_TOKEN;
    }
}