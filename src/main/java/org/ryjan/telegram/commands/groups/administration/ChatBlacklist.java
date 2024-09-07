package org.ryjan.telegram.commands.groups.administration;

import org.ryjan.telegram.commands.groups.BaseGroupCommand;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Blacklist;
import org.ryjan.telegram.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;

@Component
public class ChatBlacklist{

    @Autowired
    private GroupService groupService;

    @Autowired
    @Lazy
    private BotMain botMain;

    private boolean isEnabled = true;

    protected void executeCommand(String chatId, Update update) {

        String groupName = update.getMessage().getChat().getTitle();
        long groupId = update.getMessage().getChat().getId();
        long leftUserId = update.getMessage().getLeftChatMember().getId();
        String leftUserUsername = update.getMessage().getLeftChatMember().getUserName();

        botMain.banUser(chatId, leftUserId);
        Blacklist blacklist = new Blacklist(groupId, groupName, leftUserId, leftUserUsername, LocalDateTime.now());
        groupService.update(blacklist);
    }

    public boolean enable() {
        return isEnabled = true;
    }

    public boolean disable() {
        return isEnabled = false;
    }
}
