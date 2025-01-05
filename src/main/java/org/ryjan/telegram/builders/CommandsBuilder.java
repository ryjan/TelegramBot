package org.ryjan.telegram.builders;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.administration.GroupSettings;
import org.ryjan.telegram.commands.groups.administration.InlineGetGroupId;
import org.ryjan.telegram.commands.groups.administration.silence.SilenceMode;
import org.ryjan.telegram.commands.groups.administration.GroupStart;
import org.ryjan.telegram.commands.groups.administration.blacklist.*;
import org.ryjan.telegram.commands.groups.user.GroupBugReport;
import org.ryjan.telegram.commands.groups.user.GroupGetRank;
import org.ryjan.telegram.commands.groups.user.GroupInfo;
import org.ryjan.telegram.commands.users.admin.adminpanel.bugreport.reply.*;
import org.ryjan.telegram.commands.users.owner.SetCoins;
import org.ryjan.telegram.commands.users.admin.adminpanel.AdminPanel;
import org.ryjan.telegram.commands.users.admin.adminpanel.bugreport.wishes.FindWishes;
import org.ryjan.telegram.commands.users.owner.ownerpanel.*;
import org.ryjan.telegram.commands.users.owner.ownerpanel.groups.*;
import org.ryjan.telegram.commands.users.owner.ownerpanel.users.OwnerFindUser;
import org.ryjan.telegram.commands.users.owner.ownerpanel.users.OwnerUserSettings;
import org.ryjan.telegram.commands.users.StartUser;
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
    private GroupStart startGroupCommand;
    @Autowired
    private StartUser startUserCommand;
    @Autowired
    private GroupSettings settingsGroup;
    @Autowired
    private InlineGetGroupId inlineGetGroupIdCommand;
    @Autowired
    private BlacklistSwitch blacklistSwitch;
    @Autowired
    private BlacklistSwitchOn blacklistSwitchOn;
    @Autowired
    private BlacklistSwitchOff blacklistSwitchOff;
    @Autowired
    private BlacklistNotifications blacklistNotifications;
    @Autowired
    private BlacklistUnban blacklistUnban;
    @Autowired
    private BlacklistBannedUsersList blacklistBannedUsers;
    @Autowired
    private SilenceMode silenceMode;
    @Autowired
    private CloseMessage closeMessage;
    @Autowired
    private GroupInfo groupInfo;
    // UserCommands
    @Autowired
    private SetCoins setCoins;
    @Autowired
    private UserBugReport userBugReport;
    @Autowired
    private GroupBugReport groupBugReport;
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
    private OwnerFindGroup findGroupOwner;
    @Autowired
    private OwnerFindUser findUserOwner;
    @Autowired
    private OwnerGroupSettings ownerGroupSettings;
    @Autowired
    private OwnerUserSettings ownerUserSettings;
    @Autowired
    private SendMessageToUserArticle sendMessageToUserArticle;
    @Autowired
    private SetPrivileges setPrivileges;
    @Autowired
    private BanGroup banGroup;
    @Autowired
    private GroupGetRank groupGetRank;

    private Map<String, BaseCommand> commands = new HashMap<>();
    private Map<String, BaseCommand> buttonCommands = new HashMap<>();
    private Map<String, BaseCommand> userCommands = new HashMap<>();
    private Map<String, BaseCommand> userButtonCommands = new HashMap<>();
    private Map<String, BaseCommand> replyCommands = new HashMap<>();
    private Map<String, Consumer<Groups>> userActionsCommands = new HashMap<>(); // for the future

    @PostConstruct
    public void initializeCommands() {
        initializeGroupButtonCommands();
        initializeUserButtonCommands();
        initializeGroupSlashCommands();
        initializeUserSlashCommands();
    }

    private void initializeGroupSlashCommands() {
        commands.put(startGroupCommand.getCommandName(), startGroupCommand);
        commands.put(blacklistSwitch.getCommandName(), blacklistSwitch);
        commands.put(settingsGroup.getCommandName(), settingsGroup);
        commands.put(silenceMode.getCommandName(), silenceMode);
        commands.put(groupBugReport.getCommandName(), groupBugReport);
        commands.put(groupGetRank.getCommandName(), groupGetRank);
        commands.put(groupInfo.getCommandName(), groupInfo);
    }

    private void initializeGroupButtonCommands() {
        buttonCommands.put(blacklistSwitch.getCommandName(), blacklistSwitch);
        buttonCommands.put("blacklistStartGroup", blacklistSwitch);

        buttonCommands.put(blacklistSwitchOn.getCommandName(), blacklistSwitchOn);
        buttonCommands.put(blacklistSwitchOff.getCommandName(), blacklistSwitchOff);
        buttonCommands.put(blacklistNotifications.getCommandName(), blacklistNotifications);

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
        replyCommands.put(findGroupOwner.getCommandName(), findGroupOwner);
        replyCommands.put(findUserOwner.getCommandName(), findUserOwner);
        replyCommands.put(ownerGroupSettings.getCommandName(), ownerGroupSettings);
        replyCommands.put(ownerUserSettings.getCommandName(), ownerUserSettings);

        userButtonCommands.put(changeGroupPrivilege.getCommandName(), changeGroupPrivilege);
        userButtonCommands.put(changeGroupPrivilege.getPrivilegePremiumCallBack(), setPrivileges);
        userButtonCommands.put(changeGroupPrivilege.getPrivilegeVipCallBack(), setPrivileges);
        userButtonCommands.put(changeGroupPrivilege.getPrivilegeBaseCallBack(), setPrivileges);

        userButtonCommands.put(banGroup.getCommandName(), banGroup);
        userButtonCommands.put(banGroup.unbanGroupCallback(), banGroup);


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
