package org.ryjan.telegram.services;

import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.utils.UpdateContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class MessageService extends ServiceBuilder {

    public SendMessage createSendMessage(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        return sendMessage;
    }

    public SendMessage createSendMessage(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        return sendMessage;
    }

    public void sendMessageForCommand(BotMain bot, SendMessage message) {
        try {
            bot.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editMessage(String text, Update update) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId());
        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessageText.setText(text);
        editMessageText.enableMarkdown(true);

        try {
            botService.getBot().execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void editMessage(SendMessage message, Update update) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId());
        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessageText.setText(message.getText());
        editMessageText.enableMarkdown(true);
        if (message.getReplyMarkup() != null) {
            editMessageText.setReplyMarkup((InlineKeyboardMarkup) message.getReplyMarkup());
        }

        try {
            botService.getBot().execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void editMessage(String text, InlineKeyboardMarkup inlineKeyboardMarkup, Update update) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId());
        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessageText.setText(text);
        editMessageText.enableMarkdown(true);
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        try {
            botService.getBot().execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void deleteMessage(Update update) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(update.getMessage().getChatId());
        deleteMessage.setMessageId(update.getMessage().getMessageId());

        try {
            botService.getBot().execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void deleteMessage(String chatId, Update update) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(update.getMessage().getMessageId());

        try {
            botService.getBot().execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void deleteMessage(String chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);

        try {
            botService.getBot().execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void deleteMessageByCallbackQuery(Update update) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());

        try {
            botService.getBot().execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
