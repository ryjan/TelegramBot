package org.ryjan.telegram.commands.users.owner.ownerpanel;

import org.ryjan.telegram.builders.InlineKeyboardBuilder;
import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.GroupPrivileges;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.interfaces.Permissions;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Groups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class ChangeGroupPrivilege extends BaseCommand {
    private final String GROUP_CACHE_KEY = "owner_group_state:";
    private String chatId;

    @Autowired
    private RedisTemplate<String, Groups> redisGroupsTemplate;

    protected ChangeGroupPrivilege() {
        super("changeGroupPrivilege", "Change group privilege", UserPermissions.OWNER);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        this.chatId = chatId;
        SendMessage message = createSendMessage(chatId);
        message.setText("Chose the privilege");
        message.setReplyMarkup(getKeyboard());
        sendMessageForCommand(message);
    }

    public void executePrivilege() {
        CallbackQuery callbackQuery = getUpdate().getCallbackQuery();
        Groups group = redisGroupsTemplate.opsForValue().get(GROUP_CACHE_KEY + chatId);
        if (callbackQuery != null) {
            switch (callbackQuery.toString()) {
                case "setPrivilegesPREMIUM" -> addChangesToDatabase(group, GroupPrivileges.PREMIUM.getDisplayName());
                case "setPrivilegesVIP" -> addChangesToDatabase(group, GroupPrivileges.VIP.getDisplayName());
                case "setPrivilegesBASE" -> addChangesToDatabase(group, GroupPrivileges.BASE.getDisplayName());
            }
        }
    }

    private InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder.KeyboardLayer keyboard = new InlineKeyboardBuilder.KeyboardLayer()
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("PREMIUM", getPrivilegePremiumCallBack()))
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("VIP", getPrivilegeVipCallBack()))
                .addRow(new InlineKeyboardBuilder.ButtonRow()
                        .addButton("BASE", getPrivilegeBaseCallBack()));
        inlineKeyboardMarkup.setKeyboard(keyboard.build());

        return inlineKeyboardMarkup;
    }

    private void addChangesToDatabase(Groups group, String privilegesName) {
        group.setPrivileges(privilegesName);
        groupService.update(group);
    }

    public String getPrivilegePremiumCallBack() {
        return "setPrivilegesPREMIUM";
    }

    public String getPrivilegeVipCallBack() {
        return "setPrivilegesVIP";
    }

    public String getPrivilegeBaseCallBack() {
        return "setPrivilegesBASE";
    }
}
