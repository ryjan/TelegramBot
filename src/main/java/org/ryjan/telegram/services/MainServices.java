package org.ryjan.telegram.services;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Getter
@Service
public class MainServices {

    private final BotService botService;
    private final BlacklistService blacklistService;
    private final ChatSettingsService chatSettingsService;
    private final GroupService groupService;
    private final UserService userService;
    private final MessageService messageService;
    private final ArticlesService articlesService;
    private final MessageSender messageSender;

    @Autowired
    @Lazy
    public MainServices(BotService botService, BlacklistService blacklistService,
                        ChatSettingsService chatSettingsService, GroupService groupService,
                        UserService userService, MessageService messageService,
                        ArticlesService articlesService, MessageSender messageSender) {
        this.botService = botService;
        this.blacklistService = blacklistService;
        this.chatSettingsService = chatSettingsService;
        this.groupService = groupService;
        this.userService = userService;
        this.messageService = messageService;
        this.articlesService = articlesService;
        this.messageSender = messageSender;
    }

}
