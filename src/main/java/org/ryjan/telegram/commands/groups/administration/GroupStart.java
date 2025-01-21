package org.ryjan.telegram.commands.groups.administration;

import org.ryjan.telegram.commands.groups.*;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.builders.InlineKeyboardBuilder;
import org.ryjan.telegram.commands.groups.utils.GroupChatSettings;
import org.ryjan.telegram.commands.groups.utils.GroupPrivileges;
import org.ryjan.telegram.commands.groups.utils.GroupStatus;
import org.ryjan.telegram.commands.groups.utils.GroupSwitch;
import org.ryjan.telegram.config.RedisConfig;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.ChatSettings;
import org.ryjan.telegram.model.groups.Groups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class GroupStart extends BaseCommand {

    @Autowired
    private GroupSettings settingsGroup;
    @Autowired
    private RedisTemplate<String, ChatSettings> chatSettingsRedisTemplate;

    public GroupStart() {
        super("/start", "Начать работу бота🤙", GroupPermissions.CREATOR);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler commandHandler) {
        Update update = getUpdate();
        SendMessage message = createSendMessage(chatId);

        if (groupService.findGroup(update.getMessage().getChatId()) != null) {
            sendMessageForCommand(chatId, "Группа уже зарегистрирована😊");
            return;
        }

        String groupName = update.getMessage().getChat().getTitle();
        String creatorName = update.getMessage().getFrom().getUserName();
        Long creatorId = update.getMessage().getFrom().getId();

        Groups group = new Groups(Long.valueOf(chatId), groupName, GroupPrivileges.BASE, GroupStatus.ACTIVE.getDisplayName(), creatorId.toString(), creatorName);

        addChatSettingsToDatabase(group);

        message.setText("Бот успешно зарегистрирован🤙\n⚙️Настройки:");
        message.setReplyMarkup(settingsGroup.getKeyboard());
        message.setReplyToMessageId(update.getMessage().getMessageId());

        sendMessageForCommand(bot, message);
    }

    private void addChatSettingsToDatabase(Groups group) {
        ChatSettings chatSettingsBlacklist = chatSettingsService.addChatSettings(group, GroupChatSettings.BLACKLIST, GroupSwitch.OFF);
        chatSettingsService.addChatSettings(group, GroupChatSettings.LEVELS, GroupSwitch.ON);
        chatSettingsService.addChatSettings(group, GroupChatSettings.BLACKLIST_NOTIFICATIONS, GroupSwitch.ON);
        chatSettingsRedisTemplate.opsForValue().set(RedisConfig.CHAT_SETTINGS_CACHE_KEY
                        + GroupChatSettings.BLACKLIST.getDisplayname()
                        + group.getId(),
                chatSettingsBlacklist);
    }
}

