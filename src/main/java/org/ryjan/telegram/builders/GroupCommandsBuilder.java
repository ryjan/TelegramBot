package org.ryjan.telegram.builders;

import jakarta.annotation.PostConstruct;
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
public class GroupCommandsBuilder {

    @Autowired
    private StartGroup startGroupCommand;

    @Autowired
    private SettingsGroup settingsGroup;

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

    private Map<String, BaseGroupCommand> commands = new HashMap<>();
    private Map<String, BaseGroupCommand> buttonCommands = new HashMap<>();

    @PostConstruct
    public void initializeCommands() {
        initializeButtonCommands();
        initializeSlashCommands();
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
