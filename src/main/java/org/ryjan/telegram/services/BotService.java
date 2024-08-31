package org.ryjan.telegram.services;

import org.ryjan.telegram.BotMain;
import org.ryjan.telegram.handler.ButtonCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.awt.*;
import java.io.IOException;

@Service
public class BotService {
    @Autowired
    private ButtonCommandHandler buttonCommandHandler;
    @Autowired
    private BotMain botMain;

    public void handleUpdate(Update update) throws IOException {
        buttonCommandHandler.handleCommand(update);
    }

    public void handleExecute(SendMessage message) {
        try {
            botMain.execute(message);
        } catch (TelegramApiException e) {
            botMain.getLogger().error("Error occurred while sending message(sendMessage)", e);
        }
    }

    public void handleSendMessage(String chatId, String text) {
        botMain.sendMessage(chatId, text);
    }

}
