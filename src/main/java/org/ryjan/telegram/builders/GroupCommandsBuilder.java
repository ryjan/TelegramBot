package org.ryjan.telegram.builders;

import jakarta.annotation.PostConstruct;
import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.administration.Settings;
import org.ryjan.telegram.commands.groups.administration.silence.SilenceMode;
import org.ryjan.telegram.commands.groups.administration.Start;
import org.ryjan.telegram.commands.groups.administration.blacklist.*;
import org.ryjan.telegram.commands.users.owner.SetCoins;
import org.ryjan.telegram.commands.users.user.button.bugreport.UserBugReport;
import org.ryjan.telegram.commands.users.user.button.bugreport.UserSendReportReply;
import org.ryjan.telegram.commands.users.user.button.bugreport.UserSendWishReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GroupCommandsBuilder {

    @Autowired
    private Start startGroupCommand;

    @Autowired
    private Settings settingsGroup;

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

    //UserCommands
    @Autowired
    private SetCoins setCoins;

    @Autowired
    private UserBugReport userBugReport;

    @Autowired
    private UserSendReportReply userSendReportReply;

    @Autowired
    private UserSendWishReply userSendWishReply;

    private Map<String, BaseCommand> commands = new HashMap<>();
    private Map<String, BaseCommand> buttonCommands = new HashMap<>();
    private Map<String, BaseCommand> userCommands = new HashMap<>();
    private Map<String, BaseCommand> userButtonCommands = new HashMap<>();

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
        buttonCommands.put(blacklistBannedUsers.getCommandName(), blacklistBannedUsers);
        //buttonCommands.put("settingsStartGroup", settingsGroup);

        buttonCommands.put(closeMessage.getCommandName(), closeMessage);
        //buttonCommands.put(silenceMode.getCommandName(), silenceMode);
    }

    private void initializeUserSlashCommands() {
        userCommands.put(setCoins.getCommandName(), setCoins);
        //commands.put(sendCoins.getCommandName(), sendCoins);
        userCommands.put(userBugReport.getCommandName(), userBugReport);
        userCommands.put(userSendReportReply.getCommandName().split(" ")[0], userSendReportReply);
        userCommands.put(userSendWishReply.getCommandName().split(" ")[0], userSendWishReply);
    }

    private void initializeUserButtonCommands() {

    }

    public Map<String, BaseCommand> getCommands() {
        return commands;
    }

    public void setCommands(Map<String, BaseCommand> commands) {
        this.commands = commands;
    }

    public Map<String, BaseCommand> getButtonCommands() {
        return buttonCommands;
    }

    public void setButtonCommands(Map<String, BaseCommand> buttonCommands) {
        this.buttonCommands = buttonCommands;
    }

    public Map<String, BaseCommand> getUserCommands() {
        return userCommands;
    }

    public void setUserCommands(Map<String, BaseCommand> userCommands) {
        this.userCommands = userCommands;
    }

    public Map<String, BaseCommand> getUserButtonCommands() {
        return userButtonCommands;
    }

    public void setUserButtonCommands(Map<String, BaseCommand> userButtonCommands) {
        this.userButtonCommands = userButtonCommands;
    }
}
