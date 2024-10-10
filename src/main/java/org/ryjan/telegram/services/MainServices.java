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

    @Autowired
    @Lazy
    public MainServices(BotService botService, BlacklistService blacklistService,
                        ChatSettingsService chatSettingsService, GroupService groupService,
                        UserService userService) {
        this.botService = botService;
        this.blacklistService = blacklistService;
        this.chatSettingsService = chatSettingsService;
        this.groupService = groupService;
        this.userService = userService;
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
}
