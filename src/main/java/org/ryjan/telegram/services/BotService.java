package org.ryjan.telegram.services;

import org.ryjan.telegram.commands.groups.administration.blacklist.ChatBlacklist;
import org.ryjan.telegram.commands.groups.administration.silence.SilenceModeService;
import org.ryjan.telegram.commands.users.user.button.bugreport.BugReportService;
import org.ryjan.telegram.commands.users.user.button.bugreport.UserSendReportReply;
import org.ryjan.telegram.commands.users.user.button.bugreport.UserSendWishReply;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.handler.UserCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.UnbanChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.ryjan.telegram.main.BotMain.LOGGER;

@Service
public class BotService {

    @Autowired
    public UserCommandHandler userCommandHandler;

    @Autowired
    @Lazy
    public GroupCommandHandler groupCommandHandler;

    @Autowired
    @Lazy
    private BotMain bot;

    @Autowired
    @Lazy
    private SilenceModeService silenceModeService;

    @Autowired
    @Lazy
    private ChatBlacklist chatBlacklist;

    @Autowired
    private BugReportService bugReportService;

    @Autowired
    private UserSendReportReply userSendReportReply;

    @Autowired
    private UserSendWishReply userSendWishReply;

    public ChatMember getChatMember(Long chatId, Long userId) {
        GetChatMember chatMember = new GetChatMember();
        chatMember.setChatId(chatId);
        chatMember.setUserId(userId);
        try {
            return bot.execute(chatMember);
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
            bot.execute(banChatMember);
        } catch (Exception e) {
            LOGGER.error("Error occurred while banning user", e);
        }
    }

    public void banUser(String chatId, Long userId) {
        BanChatMember banChatMember = new BanChatMember();
        banChatMember.setChatId(chatId);
        banChatMember.setUserId(userId);

        try {
            bot.execute(banChatMember);
        } catch (Exception e) {
            LOGGER.error("Error occurred while banning user", e);
        }
    }

    public void unbanUser(String chatId, Long userId) {
        UnbanChatMember unbanChatMember = new UnbanChatMember();
        unbanChatMember.setChatId(chatId);
        unbanChatMember.setUserId(userId);

        try {
            bot.execute(unbanChatMember);
        } catch (Exception e) {
            LOGGER.error("Error occurred while unbanning user", e);
        }
    }

    public void handleChatMemberUpdate(ChatMemberUpdated chatMemberUpdated) {
        if (chatMemberUpdated.getNewChatMember().getStatus().equals("member")) {
            long chatId = chatMemberUpdated.getChat().getId();
            long userId = chatMemberUpdated.getFrom().getId();

            sendPrivateMessageToUser(chatId);
        }
    }

    public void sendPrivateMessageToUser(Long userId) {
        SendMessage message = new SendMessage();
        message.setChatId(userId);
        message.setText("✨Привет, воспользуйся /start в группе, чтобы начать работу!");

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            LOGGER.error("Error occurred while sending message(sendPrivateMessageToUser)");
        }
    }

    public void autoExecute(Update update) {
        if (update.hasMessage() && update.getMessage().getLeftChatMember() != null) {
            String chatId = update.getMessage().getChatId().toString();
            chatBlacklist.executeCommand(chatId, bot, groupCommandHandler);
            return;
        }

        if (update.hasMessage() && update.getMessage().getChat().isUserChat() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            String commandName = text.equals(userSendReportReply.getCommandName())
                    ? userSendReportReply.getCommandName()
                    : userSendWishReply.getCommandName();
            bugReportService.operationSuccessful(update, commandName);
            return;
        }

        if (update.hasMessage() && silenceModeService.isSilenceModeEnabled(update.getMessage().getChatId())) {
            silenceModeService.doDeleteFunction();
        }

        /*if (update.hasMyChatMember()) {
            handleChatMemberUpdate(update.getMyChatMember());
        }*/
    }

    public BotMain getBot() {
        return this.bot;
    }
}
