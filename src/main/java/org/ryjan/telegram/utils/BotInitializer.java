package org.ryjan.telegram.utils;

import org.ryjan.telegram.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInitializer implements ApplicationRunner {
    @Autowired
    private BotMain botMain;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        botMain.getLogger().info("Starting Bot");
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(botMain);
            botMain.getLogger().info("Bot started successfully!");
        } catch (TelegramApiException e) {
            botMain.getLogger().info("Error occurred while initializing Bot", e);
        }
    }
}
