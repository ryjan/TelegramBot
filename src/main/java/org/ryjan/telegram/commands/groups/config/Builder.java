package org.ryjan.telegram.commands.groups.config;

import org.ryjan.telegram.commands.groups.BaseGroupCommand;
import org.ryjan.telegram.commands.groups.administration.SettingsGroup;
import org.ryjan.telegram.commands.groups.administration.silence.SilenceMode;
import org.ryjan.telegram.commands.groups.administration.StartGroup;
import org.ryjan.telegram.commands.groups.administration.blacklist.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Builder {

    private StartGroup startGroupCommand;
    private SettingsGroup settingsGroup;
    private BlacklistSwitch blacklistSwitch;
    private BlacklistSwitchOn blacklistSwitchOn;
    private BlacklistSwitchOff blacklistSwitchOff;
    private BlacklistUnban blacklistUnban;
    private BlacklistBannedUsersList blacklistBannedUsers;
    private SilenceMode silenceMode;
    private CloseMessage closeMessage;

    private Map<String, BaseGroupCommand> commands = new HashMap<>();
    private Map<String, BaseGroupCommand> buttonCommands = new HashMap<>();

    public void initializeCommands() {
        initializeButtonCommands();
        initializeSlashCommands();
    }

    private void initializeSlashCommands() {
        commands.put(startGroupCommand.getCommandName(), startGroupCommand);
        commands.put(blacklistSwitch.getCommandName(), blacklistSwitch);
        commands.put(settingsGroup.getCommandName(), settingsGroup);
        //commands.put(silenceMode.getCommandName(), silenceMode);
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

    public StartGroup getStartGroupCommand() {
        return startGroupCommand;
    }

    @Autowired
    public void setStartGroupCommand(StartGroup startGroupCommand) {
        this.startGroupCommand = startGroupCommand;
    }

    public SettingsGroup getSettingsGroup() {
        return settingsGroup;
    }

    @Autowired
    public void setSettingsGroup(SettingsGroup settingsGroup) {
        this.settingsGroup = settingsGroup;
    }

    public BlacklistSwitch getBlacklistSwitch() {
        return blacklistSwitch;
    }

    @Autowired
    public void setBlacklistSwitch(BlacklistSwitch blacklistSwitch) {
        this.blacklistSwitch = blacklistSwitch;
    }

    public BlacklistSwitchOn getBlacklistSwitchOn() {
        return blacklistSwitchOn;
    }

    @Autowired
    public void setBlacklistSwitchOn(BlacklistSwitchOn blacklistSwitchOn) {
        this.blacklistSwitchOn = blacklistSwitchOn;
    }

    public BlacklistSwitchOff getBlacklistSwitchOff() {
        return blacklistSwitchOff;
    }

    @Autowired
    public void setBlacklistSwitchOff(BlacklistSwitchOff blacklistSwitchOff) {
        this.blacklistSwitchOff = blacklistSwitchOff;
    }

    public BlacklistUnban getBlacklistUnban() {
        return blacklistUnban;
    }

    @Autowired
    public void setBlacklistUnban(BlacklistUnban blacklistUnban) {
        this.blacklistUnban = blacklistUnban;
    }

    public BlacklistBannedUsersList getBlacklistBannedUsers() {
        return blacklistBannedUsers;
    }

    @Autowired
    public void setBlacklistBannedUsers(BlacklistBannedUsersList blacklistBannedUsers) {
        this.blacklistBannedUsers = blacklistBannedUsers;
    }

    public SilenceMode getSilenceMode() {
        return silenceMode;
    }

    @Autowired
    public void setSilenceMode(SilenceMode silenceMode) {
        this.silenceMode = silenceMode;
    }

    public CloseMessage getCloseMessage() {
        return closeMessage;
    }

    @Autowired
    public void setCloseMessage(CloseMessage closeMessage) {
        this.closeMessage = closeMessage;
    }

    public Map<String, BaseGroupCommand> getCommands() {
        return commands;
    }

    public void setCommands(Map<String, BaseGroupCommand> commands) {
        this.commands = commands;
    }

    public Map<String, BaseGroupCommand> getButtonCommands() {
        return buttonCommands;
    }

    public void setButtonCommands(Map<String, BaseGroupCommand> buttonCommands) {
        this.buttonCommands = buttonCommands;
    }
}
