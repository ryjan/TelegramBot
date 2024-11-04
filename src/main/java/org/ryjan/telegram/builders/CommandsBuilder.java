package org.ryjan.telegram.builders;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.GroupPrivileges;
import org.ryjan.telegram.commands.groups.administration.InlineGetGroupId;
import org.ryjan.telegram.commands.groups.administration.Settings;
import org.ryjan.telegram.commands.groups.administration.silence.SilenceMode;
import org.ryjan.telegram.commands.groups.administration.StartGroup;
import org.ryjan.telegram.commands.groups.administration.blacklist.*;
import org.ryjan.telegram.commands.users.admin.adminpanel.bugreport.reply.*;
import org.ryjan.telegram.commands.users.owner.SetCoins;
import org.ryjan.telegram.commands.users.admin.adminpanel.AdminPanel;
import org.ryjan.telegram.commands.users.admin.adminpanel.bugreport.wishes.FindWishes;
import org.ryjan.telegram.commands.users.owner.ownerpanel.*;
import org.ryjan.telegram.commands.users.user.StartUser;
import org.ryjan.telegram.commands.users.user.button.bugreport.UserBugReport;
import org.ryjan.telegram.commands.users.user.button.bugreport.UserSendWishReply;
import org.ryjan.telegram.model.groups.Groups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Getter
@Setter
@Component
public class CommandsBuilder {

    @Autowired
    private StartGroup startGroupCommand;

    @Autowired
    private StartUser startUserCommand;

    @Autowired
    private Settings settingsGroup;

    @Autowired
    private InlineGetGroupId inlineGetGroupIdCommand;

    @Autowired
    private BlacklistSwitch blacklistSwitch;

    @Autowired
    private BlacklistSwitchOn blacklistSwitchOn;

    @Autowired
    private BlacklistSwitchOff blacklistSwitchOff;

    @Autowired
    private BlacklistUnban blacklistUnban;

    @Autowired
    private BlacklistBannedUsersList blacklistBannedUsers;

    @Autowired
    private SilenceMode silenceMode;

    @Autowired
    private CloseMessage closeMessage;

    // UserCommands
    @Autowired
    private SetCoins setCoins;

    @Autowired
    private UserBugReport userBugReport;

    @Autowired
    private UserSendWishReply userSendWishReply;

    @Autowired
    private AdminPanel adminPanel;

    @Autowired
    private NextArticle nextArticle;

    @Autowired
    private LikeArticle likeArticle;

    @Autowired
    private DeclineArticle declineArticle;

    @Autowired
    private CheckArticles checkArticles;

    @Autowired
    private FindWishes findWishes;

    @Autowired
    private OwnerPanel ownerPanel;

    @Autowired
    private ChangeGroupPrivilege changeGroupPrivilege;

    @Autowired
    private FindGroupOwner findGroupOwner;

    @Autowired
    private OwnerGroupSettings ownerGroupSettings;

    @Autowired
    private SendMessageToUserArticle sendMessageToUserArticle;

    @Autowired
    private SetPrivileges setPrivileges;

    private Map<String, BaseCommand> commands = new HashMap<>();
    private Map<String, BaseCommand> buttonCommands = new HashMap<>();
    private Map<String, BaseCommand> userCommands = new HashMap<>();
    private Map<String, BaseCommand> userButtonCommands = new HashMap<>();
    private Map<String, Consumer<Groups>> userActionsCommands = new HashMap<>(); // for a future

    @PostConstruct
    public void initializeCommands() {
        initializeButtonCommands();
        initializeUserButtonCommands();
        initializeSlashCommands();
        initializeUserSlashCommands();
    }

    private void initializeSlashCommands() {
        commands.put(startGroupCommand.getCommandName(), startGroupCommand);
        commands.put(blacklistSwitch.getCommandName(), blacklistSwitch);
        commands.put(settingsGroup.getCommandName(), settingsGroup);
        commands.put(silenceMode.getCommandName(), silenceMode);
    }

    private void initializeButtonCommands() {
        buttonCommands.put(blacklistSwitch.getCommandName(), blacklistSwitch);
        buttonCommands.put("blacklistStartGroup", blacklistSwitch);

        buttonCommands.put(blacklistSwitchOn.getCommandName(), blacklistSwitchOn);
        buttonCommands.put(blacklistSwitchOff.getCommandName(), blacklistSwitchOff);

        buttonCommands.put(blacklistUnban.getCommandName(), blacklistUnban);

        buttonCommands.put(settingsGroup.getCommandName(), settingsGroup);
        buttonCommands.put(inlineGetGroupIdCommand.getCommandName(), inlineGetGroupIdCommand);
        buttonCommands.put(blacklistBannedUsers.getCommandName(), blacklistBannedUsers);
        //buttonCommands.put("settingsStartGroup", settingsGroup);

        buttonCommands.put(closeMessage.getCommandName(), closeMessage);
        //buttonCommands.put(silenceMode.getCommandName(), silenceMode);
    }

    private void initializeUserSlashCommands() {
        userCommands.put(startUserCommand.getCommandName(), startUserCommand);

        userCommands.put(setCoins.getCommandName(), setCoins);

        userBugReportCommands();
        ownerPanelCommands();
        adminPanelCommands();
    }

    private void initializeUserButtonCommands() {
        userButtonCommands.put(findWishes.getCommandName(), findWishes);
    }

    private void ownerPanelCommands() {
        userCommands.put(ownerPanel.getCommandName(), ownerPanel);
        userCommands.put(findGroupOwner.getCommandName().split(" ")[0], findGroupOwner);
        userCommands.put(ownerGroupSettings.getCommandName().split(" ")[0], ownerGroupSettings);

        userButtonCommands.put(changeGroupPrivilege.getCommandName(), changeGroupPrivilege);
        userButtonCommands.put(changeGroupPrivilege.getPrivilegePremiumCallBack(), setPrivileges);
        userButtonCommands.put(changeGroupPrivilege.getPrivilegeVipCallBack(), setPrivileges);
        userButtonCommands.put(changeGroupPrivilege.getPrivilegeBaseCallBack(), setPrivileges);


    }

    private void adminPanelCommands() {
        userCommands.put(adminPanel.getCommandName(), adminPanel);
        userCommands.put(checkArticles.getCommandName().split(" ")[0], checkArticles);
        userCommands.put(nextArticle.getCommandName(), nextArticle);
        userCommands.put(likeArticle.getCommandName(), likeArticle);
        userCommands.put(declineArticle.getCommandName(), declineArticle);
        userCommands.put(sendMessageToUserArticle.getCommandName(), sendMessageToUserArticle);
    }

    private void userBugReportCommands() {
        userCommands.put(userBugReport.getCommandName(), userBugReport);
        userCommands.put(userSendWishReply.getCommandName().split(" ")[0], userSendWishReply);
    }
}
