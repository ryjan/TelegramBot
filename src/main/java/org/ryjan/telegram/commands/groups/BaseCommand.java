package org.ryjan.telegram.commands.groups;

import lombok.Getter;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.commands.interfaces.IBotCommand;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.interfaces.Permissions;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.services.*;
import org.ryjan.telegram.utils.UpdateContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Service
public abstract class BaseCommand implements IBotCommand {

    @Getter
    private final String commandName;
    @Getter
    private final String description;
    @Getter
    private final Permissions requiredPermission;
    @Getter
    private final Integer requiredLevel;

    protected UserService userService;
    protected GroupService groupService;
    protected BotService botService;
    protected ChatSettingsService chatSettingsService;
    protected BlacklistService blacklistService;
    protected MessageService messageService;
    protected ArticlesService articlesService;
    protected MessageSender messageSender;

    @Autowired
    public void setMainServices(MainServices mainServices) {
        this.userService = mainServices.getUserService();
        this.groupService = mainServices.getGroupService();
        this.botService = mainServices.getBotService();
        this.chatSettingsService = mainServices.getChatSettingsService();
        this.blacklistService = mainServices.getBlacklistService();
        this.messageService = mainServices.getMessageService();
        this.articlesService = mainServices.getArticlesService();
        this.messageSender = mainServices.getMessageSender();
    }

    protected BaseCommand(String commandName, String description, Permissions requiredPermission) {
        this.commandName = commandName;
        this.description = description;
        this.requiredPermission = requiredPermission;
        this.requiredLevel = 0;
    }

    protected BaseCommand(String commandName, String description, Permissions requiredPermission, Integer requiredLevel) {
        this.commandName = commandName;
        this.description = description;
        this.requiredPermission = requiredPermission;
        this.requiredLevel = requiredLevel;
    }

    public GroupPermissions getPermissionFromChat(Long chatId, Long userId) {
        return groupService.getPermissionFromChat(chatId, userId);
    }

    public boolean hasRequiredLevel(Long userId) {
        return userService.hasRequiredLevel(userId, requiredLevel);
    }

    public boolean hasPermissionInGroup(Long chatId, Long userId) {
        if (requiredPermission  == GroupPermissions.ANY) {
            return true;
        }

        ChatMember chatMember = botService.getChatMember(chatId, userId);
        String status = chatMember.getStatus();

        return switch (requiredPermission) {
            case GroupPermissions.CREATOR -> "creator".equals(status);
            case GroupPermissions.ADMIN -> "administrator".equals(status) || "creator".equals(status);
            default -> false;
        };
    }

    public boolean hasPermissionInUserChat(Long userId) {
        return userService.hasRequiredPermission(userId, requiredPermission);
    }

    protected SendMessage createSendMessage(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        return sendMessage;
    }

    protected SendMessage createSendMessage(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        return sendMessage;
    }

    protected void sendMessageForCommand(SendMessage message) {
        try {
            botService.getBot().execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void sendMessageForCommand(String chatId, String message) {
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

    protected void sendMessageForCommand(BotMain bot, SendMessage message) {
        try {
            bot.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void editMessage(String text) {
        messageService.editMessage(text, getUpdate());
    }

    protected void editMessage(SendMessage message) {
        messageService.editMessage(message, getUpdate());
    }

    protected void editMessage(String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        messageService.editMessage(text, inlineKeyboardMarkup, getUpdate());
    }

    protected void deleteMessage() {
        messageService.deleteMessage(getUpdate());
    }

    protected void deleteMessage(String chatId) {
        messageService.deleteMessage(chatId, getUpdate());
    }

    protected void deleteMessage(String chatId, int messageId) {
        messageService.deleteMessage(chatId, messageId);
    }

    protected void deleteMessageByCallbackQuery() {
        messageService.deleteMessageByCallbackQuery(getUpdate());
    }

    protected String[] getParts(String command, int expectedParts) {
        return getUpdate().getMessage().getText().replace(command, "").trim().split(" ", expectedParts);
    }

    protected abstract void executeCommand(String chatId, BotMain bot, CommandsHandler handler) throws Exception;

    @Override
    public void execute(String chatId, BotMain bot, CommandsHandler commandHandler) {
        try {
            executeCommand(chatId, bot, commandHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Update getUpdate() {
        return UpdateContext.getInstance().getUpdate();
    }
}
