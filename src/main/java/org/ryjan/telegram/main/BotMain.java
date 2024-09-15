package org.ryjan.telegram.main;


import com.sun.tools.javac.Main;

import org.ryjan.telegram.commands.groups.administration.ChatBlacklist;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.services.GroupService;
import org.ryjan.telegram.services.UserService;
import org.ryjan.telegram.handler.ButtonCommandHandler;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.LeaveChat;

import org.ryjan.telegram.utils.UpdateContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.User;

@SpringBootApplication
public class BotMain extends TelegramLongPollingBot {
    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private ButtonCommandHandler buttonCommandHandler;

    @Autowired
    private GroupCommandHandler groupCommandHandler;

    @Autowired
    ChatBlacklist chatBlacklist;

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
        autoExecute(update);

        if (update.hasMessage() && update.getMessage().hasText()) {
            String receivedMessage = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            User user = update.getMessage().getFrom();
        }

        System.out.println(update.hasMessage());
        UpdateContext.getInstance().setUpdate(update);

        try {
            if (update.hasMessage() && update.getMessage().getChat().isUserChat()) {
                buttonCommandHandler.handleCommand(update);
            } else {
                groupCommandHandler.handleCommand(update);
            }

        } catch (Exception e) {
            LOGGER.error("Error occurred while sending message(onUpdateReceived)", e);
        }
    }

    public ChatMember getChatMember(Long chatId, Long userId) {
        GetChatMember chatMember = new GetChatMember();
        chatMember.setChatId(chatId);
        chatMember.setUserId(userId);
        try {
            return execute(chatMember);
        } catch (Exception e) {
            LOGGER.error("Error occurred while getting chat member", e);
        }
        return null;
    }

    public void banUser(Long chatId, Long userId) {
        BanChatMember banChatMember = new BanChatMember();
        banChatMember.setChatId(chatId);
        banChatMember.setUserId(userId);

        try {
            execute(banChatMember);
        } catch (Exception e) {
            LOGGER.error("Error occurred while banning user", e);
        }
    }

    public void banUser(String chatId, Long userId) {
        BanChatMember banChatMember = new BanChatMember();
        banChatMember.setChatId(chatId);
        banChatMember.setUserId(userId);

        try {
            execute(banChatMember);
        } catch (Exception e) {
            LOGGER.error("Error occurred while banning user", e);
        }
    }

    private void handleChatMemberUpdate(ChatMemberUpdated chatMemberUpdated) {
        if (chatMemberUpdated.getNewChatMember().getStatus().equals("member")) {
            long chatId = chatMemberUpdated.getChat().getId();
            long userId = chatMemberUpdated.getFrom().getId();

            sendPrivateMessageToUser(chatId);
        }
    }

    private void sendPrivateMessageToUser(Long userId) {
        SendMessage message = new SendMessage();
        message.setChatId(userId);
        message.setText("✨Привет, воспользуйся /start в группе, чтобы начать работу!");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            LOGGER.error("Error occurred while sending message(sendPrivateMessageToUser)");
        }
    }

    private void autoExecute(Update update) {
        if (update.hasMessage() && update.getMessage().getLeftChatMember() != null) {
            String chatId = update.getMessage().getChatId().toString();
            chatBlacklist.executeCommand(chatId, update);
        }

        if (update.hasMyChatMember()) {
            handleChatMemberUpdate(update.getMyChatMember());
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