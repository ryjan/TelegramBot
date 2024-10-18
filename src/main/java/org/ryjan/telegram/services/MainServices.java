package org.ryjan.telegram.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class MainServices {

    private final BotService botService;
    private final BlacklistService blacklistService;
    private final ChatSettingsService chatSettingsService;
    private final GroupService groupService;
    private final UserService userService;
    private final BankService bankService;
    private final MessageService messageService;
    private final ArticlesService articlesService;

    @Autowired
    @Lazy
    public MainServices(BotService botService, BlacklistService blacklistService,
                        ChatSettingsService chatSettingsService, GroupService groupService,
                        UserService userService, BankService bankService, MessageService messageService,
                        ArticlesService articlesService) {
        this.botService = botService;
        this.blacklistService = blacklistService;
        this.chatSettingsService = chatSettingsService;
        this.groupService = groupService;
        this.userService = userService;
        this.bankService = bankService;
        this.messageService = messageService;
        this.articlesService = articlesService;
    }

    public BotService getBotService() {
        return botService;
    }

    public BlacklistService getBlacklistService() {
        return blacklistService;
    }

    public ChatSettingsService getChatSettingsService() {
        return chatSettingsService;
    }

    public GroupService getGroupService() {
        return groupService;
    }

    public UserService getUserService() {
        return userService;
    }

    public BankService getBankService() {
        return bankService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public ArticlesService getArticlesService() {
        return articlesService;
    }
}
