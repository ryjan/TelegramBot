package org.ryjan.telegram.commands.groups.administration;

import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Blacklist;
import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ChatBlacklist{

    @Autowired
    private GroupService groupService;

    @Autowired
    @Lazy
    private BotMain botMain;

    private boolean isEnabled = true;

    public void executeCommand(String chatId, Update update) {
        if (!isEnabled) {
            return;
        }

        String groupName = update.getMessage().getChat().getTitle();
        long groupId = update.getMessage().getChat().getId();
        long leftUserId = update.getMessage().getLeftChatMember().getId();
        String leftUserUsername = update.getMessage().getLeftChatMember().getUserName();

        //botMain.banUser(chatId, leftUserId);
        Blacklist blacklist = new Blacklist(groupName, leftUserId, leftUserUsername, LocalDateTime.now());

        groupService.addToBlacklist(groupId, blacklist);
    }

    public void enable() {
        isEnabled = true;
    }

    public void disable() {
        isEnabled = false;
    }
}
