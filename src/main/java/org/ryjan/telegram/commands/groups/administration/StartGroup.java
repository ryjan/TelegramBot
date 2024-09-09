package org.ryjan.telegram.commands.groups.administration;

import org.ryjan.telegram.commands.groups.BaseGroupCommand;
import org.ryjan.telegram.commands.groups.GroupPrivileges;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.ChatSettings;
import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

@Component
public class StartGroup extends BaseGroupCommand {

    @Autowired
    private GroupService groupService;

    public StartGroup() {
        super("/start", "Начать работу бота🤙");
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler) {
        Update update = getUpdate();
        SendMessage message = createSendMessage(chatId);
        //String groupName = update.getMessage().getLeftChatMember();
        // Добавить проверку на админа
        ChatMember chatMember = bot.getChatMember(Long.valueOf(chatId), update.getMessage().getFrom().getId());

        if (!chatMember.getStatus().equals("creator")) {
            message.setText("Только создатель чата может запустить бота😎");
            sendMessageForCommand(bot, message);
            return;
        }

        if (groupService.groupIsExist(update.getMessage().getChatId())) {
            message.setText("Группа уже зарегистрирована😊");
            sendMessageForCommand(bot, message);
            return;
        }

        String groupName = update.getMessage().getChat().getTitle();
        Groups group = new Groups(Long.valueOf(chatId), groupName, GroupPrivileges.BASE);
        ChatSettings chatSettings = new ChatSettings();
        chatSettings.setGroups(group);
        chatSettings.addSetting("aboba", "abo");

        groupService.update(group, chatSettings);
        message.setText("Бот успешно зарегистрирован🤙");
        message.setReplyToMessageId(update.getMessage().getMessageId());

        sendMessageForCommand(bot, message);
    }
}
