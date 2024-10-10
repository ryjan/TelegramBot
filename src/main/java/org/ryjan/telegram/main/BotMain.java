package org.ryjan.telegram.main;


import com.sun.tools.javac.Main;

import org.ryjan.telegram.commands.groups.administration.blacklist.ChatBlacklist;
import org.ryjan.telegram.commands.groups.administration.silence.SilenceModeService;
import org.ryjan.telegram.commands.users.user.button.bugreport.BugReportService;
import org.ryjan.telegram.commands.users.user.button.bugreport.UserSendReportReply;
import org.ryjan.telegram.commands.users.user.button.bugreport.UserSendWishReply;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.services.BotService;
import org.ryjan.telegram.services.GroupService;
import org.ryjan.telegram.services.UserService;
import org.ryjan.telegram.handler.UserCommandHandler;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;

import org.ryjan.telegram.utils.UpdateContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.UnbanChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.User;

@SpringBootApplication
public class BotMain extends TelegramLongPollingBot {

    @Autowired
    BotService botService;

    @Value("${bot.token}")
    private String token;
    @Value("${bot.username}")
    private String username;
    @Value("${bot.tag}")
    private String tag;

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
        UpdateContext.getInstance().setUpdate(update);
        botService.autoExecute(update);

        try {
            if (update.hasMessage() && update.getMessage().getChat().isUserChat()) {
                botService.userCommandHandler.handleCommand(update);
            } else {
                botService.groupCommandHandler.handleCommand(update);
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
}