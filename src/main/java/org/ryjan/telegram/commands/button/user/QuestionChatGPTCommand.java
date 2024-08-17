package org.ryjan.telegram.commands.button.user;

import org.ryjan.telegram.commands.interfaces.IBotCommand;
import org.ryjan.telegram.commands.utils.ButtonCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.services.ChatGPTService;
import org.ryjan.telegram.utils.UpdateContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class QuestionChatGPTCommand implements IBotCommand {
    private final ChatGPTService chatGPTService = new ChatGPTService();

    @Override
    public void execute(String chatId, BotMain bot, ButtonCommandHandler buttonCommandHandler) {
        Update update = UpdateContext.getInstance().getUpdate();

        bot.sendMessage(chatId, "Введите вопрос, который хотите задать:");

        SendMessage sendMessage = new SendMessage();

        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            String slashCommand = chatGPTService.askQuestion(message.replace("/askchatgpt", "").trim());
            String buttonCommand = chatGPTService.askQuestion(message);
            String response = message.contains("/askchatgpt") ? slashCommand : buttonCommand;

            sendMessage.setChatId(chatId);
            sendMessage.setText(response);
        }

        try {
            bot.execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* @Override
    public void execute(String chatId, BotMain bot, ButtonCommandHandler buttonCommandHandler) {
        Update update = UpdateContext.getInstance().getUpdate();

        String message = update.getMessage().getText().trim();
        String response = message.contains("/askChatGPT-4 ") ? chatGPTService.askQuestion(message.replace("/askChatGPT-4", "").trim()) : chatGPTService.askQuestion(message);
        bot.sendMessage(chatId, "Введите вопрос, который хотите задать:");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(response);

        try {
            bot.execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    */
}
