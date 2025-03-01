package org.ryjan.telegram.services;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class ServiceBuilder {
    protected BlacklistService blacklistService;
    protected BotService botService;
    protected ChatSettingsService chatSettingsService;
    protected GroupService groupService;
    protected UserService userService;
    protected MessageService messageService;
    protected ArticlesService articlesService;
    protected MessageSender messageSender;

    @Autowired
    public void setMainServices(MainServices mainServices) {
        this.blacklistService = mainServices.getBlacklistService();
        this.botService = mainServices.getBotService();
        this.chatSettingsService = mainServices.getChatSettingsService();
        this.groupService = mainServices.getGroupService();
        this.userService = mainServices.getUserService();
        this.messageService = mainServices.getMessageService();
        this.articlesService = mainServices.getArticlesService();
        this.messageSender = mainServices.getMessageSender();
    }
}
