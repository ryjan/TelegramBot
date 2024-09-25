package org.ryjan.telegram.commands.groups;

import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.commands.interfaces.IBotGroupCommand;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.services.GroupService;
import org.ryjan.telegram.utils.UpdateContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public abstract class BaseGroupCommand implements IBotGroupCommand {
    private final String commandName;
    private final String description;
    private final Permission requiredPermission;

    @Autowired
    GroupService groupService;

    protected BaseGroupCommand(String commandName, String description, Permission requiredPermission) {
        this.commandName = commandName;
        this.description = description;
        this.requiredPermission = requiredPermission;
    }

    public boolean hasPermission(Long chatId, Long userId) {
        if (requiredPermission  == Permission.ANY) {
            return true;
        }

        ChatMember chatMember = groupService.getBotMain().getChatMember(chatId, userId);
        String status = chatMember.getStatus();

        return switch (requiredPermission) {
            case CREATOR -> "creator".equals(status);
            case ADMIN -> "administrator".equals(status) || "creator".equals(status);
            default -> false;
        };
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

    protected void sendMessageForCommand(BotMain bot, SendMessage message) {
        try {
            bot.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void editMessage(String text) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(getUpdate().getCallbackQuery().getMessage().getChatId());
        editMessageText.setMessageId(getUpdate().getCallbackQuery().getMessage().getMessageId());
        editMessageText.setText(text);
        editMessageText.setParseMode(ParseMode.MARKDOWN);

        try {
            groupService.getBotMain().execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    protected void editMessage(String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(getUpdate().getCallbackQuery().getMessage().getChatId());
        editMessageText.setMessageId(getUpdate().getCallbackQuery().getMessage().getMessageId());
        editMessageText.setText(text);
        editMessageText.setParseMode(ParseMode.MARKDOWNV2);
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        try {
            groupService.getBotMain().execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    protected void deleteMessage() {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(getUpdate().getMessage().getChatId());
        deleteMessage.setMessageId(getUpdate().getMessage().getMessageId());

        try {
            groupService.getBotMain().execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    protected void deleteMessage(String chatId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(getUpdate().getMessage().getMessageId());

        try {
            groupService.getBotMain().execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    protected void deleteMessage(String chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);

        try {
            groupService.getBotMain().execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    protected void deleteMessageByCallbackQuery() {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(getUpdate().getCallbackQuery().getMessage().getChatId());
        deleteMessage.setMessageId(getUpdate().getCallbackQuery().getMessage().getMessageId());

        try {
            groupService.getBotMain().execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    protected String[] getParts(String command, int expectedParts) {
        return getUpdate().getMessage().getText().replace(command, "").trim().split(" ", expectedParts);
    }

    protected abstract void executeCommand(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler);

    @Override
    public void execute(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler) {
        try {
            executeCommand(chatId, bot, groupCommandHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCommandName() {
        return commandName;
    }

    public String getDescription() {
        return description;
    }

    public Permission getPermission() {
        return requiredPermission;
    }

    protected Update getUpdate() {
        return UpdateContext.getInstance().getUpdate();
    }
}
