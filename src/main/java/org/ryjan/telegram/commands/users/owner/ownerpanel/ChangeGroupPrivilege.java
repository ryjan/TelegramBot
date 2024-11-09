package org.ryjan.telegram.commands.users.owner.ownerpanel;

import lombok.Getter;
import org.ryjan.telegram.builders.InlineKeyboardBuilder;
import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.GroupPrivileges;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.interfaces.Permissions;
import org.ryjan.telegram.main.BotMain;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class ChangeGroupPrivilege extends BaseCommand {

    protected ChangeGroupPrivilege() {
        super("changeGroupPrivilege", "Change group privilege", UserPermissions.OWNER);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) {
        SendMessage message = createSendMessage(chatId);
        message.setText("Chose the privilege");
        message.setReplyMarkup(getKeyboard());
        sendMessageForCommand(message);
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

    public String getPrivilegePremiumCallBack() {
        return "setPrivileges:PREMIUM";
    }

    public String getPrivilegeVipCallBack() {
        return "setPrivileges:VIP";
    }

    public String getPrivilegeBaseCallBack() {
        return "setPrivileges:BASE";
    }
}
