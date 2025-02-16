package org.ryjan.telegram.services;

import org.ryjan.telegram.utils.UpdateContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class MessageSender extends ServiceBuilder {
    public void sendMessage(SendMessage message) {
        try {
            botService.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        sendMessage.enableMarkdown(true);
        try {
            botService.getBot().execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editLastMessage(String text) {
        messageService.editMessage(text, getUpdate());
    }

    public void editLastMessage(SendMessage message) {
        messageService.editMessage(message, getUpdate());
    }

    public void editLastMessage(String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        messageService.editMessage(text, inlineKeyboardMarkup, getUpdate());
    }

    public void deleteMessage() {
        messageService.deleteMessage(getUpdate());
    }

    public void deleteMessage(String chatId) {
        messageService.deleteMessage(chatId, getUpdate());
    }

    public void deleteMessage(String chatId, int messageId) {
        messageService.deleteMessage(chatId, messageId);
    }

    public void deleteMessageByCallbackQuery() {
        messageService.deleteMessageByCallbackQuery(getUpdate());
    }

    private Update getUpdate() {
        return UpdateContext.getInstance().getUpdate();
    }
}
