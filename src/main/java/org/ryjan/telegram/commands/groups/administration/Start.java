package org.ryjan.telegram.commands.groups.administration;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.GroupPrivileges;
import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.builders.InlineKeyboardBuilder;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Groups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class Start extends BaseCommand<GroupCommandHandler> {

    @Autowired
    private Settings settingsGroup;

    public Start() {
        super("/start", "Начать работу бота🤙", Permission.CREATOR); // добавить при вызове команды inline keyboard с настройками сервера
        // а лучше отдельный класс с этой командой, которая будет использоваться тут. SettingsGroup
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler) {
        Update update = getUpdate();
        SendMessage message = createSendMessage(chatId);
        //String groupName = update.getMessage().getLeftChatMember();
        // Добавить проверку на админа

        if (groupService.groupIsExist(update.getMessage().getChatId())) {
            message.setText("Группа уже зарегистрирована😊");
            sendMessageForCommand(bot, message);
            return;
        }

        String groupName = update.getMessage().getChat().getTitle();
        String creatorName = update.getMessage().getFrom().getUserName();
        Long creatorId = update.getMessage().getFrom().getId();
        Groups group = new Groups(Long.valueOf(chatId), groupName, GroupPrivileges.BASE, "registered",  creatorId.toString(), creatorName);
        chatSettingsService.addChatSettings(group, "blacklist", "disabled");
        chatSettingsService.addChatSettings(group, "aboba", "ggs");

        message.setText("Бот успешно зарегистрирован🤙\n⚙️Настройки:");
        message.setReplyMarkup(settingsGroup.getKeyboard());
        message.setReplyToMessageId(update.getMessage().getMessageId());

        sendMessageForCommand(bot, message);
    }

    private InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder.KeyboardLayer keyboard = new InlineKeyboardBuilder.KeyboardLayer()
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("🔒Черный список", "/settings"));
        inlineKeyboardMarkup.setKeyboard(keyboard.build());

        return inlineKeyboardMarkup;
    }
}

