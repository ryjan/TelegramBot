package org.ryjan.telegram.commands.groups.administration;

import org.ryjan.telegram.commands.groups.BaseGroupCommand;
import org.ryjan.telegram.commands.users.BaseCommand;
import org.ryjan.telegram.commands.groups.Privileges;
import org.ryjan.telegram.handler.ButtonCommandHandler;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.Groups;
import org.ryjan.telegram.services.GroupService;
import org.ryjan.telegram.utils.UpdateContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartGroupCommand extends BaseGroupCommand {
    @Autowired
    private GroupService groupService;

    public StartGroupCommand() {
        super("/start", "Начать работу бота🤙");
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler) {
        Update update = UpdateContext.getInstance().getUpdate();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        //String groupName = update.getMessage().getLeftChatMember();
        String groupName = update.getMessage().getChat().getTitle();
        Groups groups = new Groups(Long.valueOf(chatId), groupName, Privileges.BASE);

        groupService.update(groups);
        message.setText("Бот успешно зарегистрирован🤙");
        message.setReplyToMessageId(update.getMessage().getMessageId());

        sendMessageForCommand(bot, message);
    }
}
