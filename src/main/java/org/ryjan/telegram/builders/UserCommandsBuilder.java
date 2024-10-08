package org.ryjan.telegram.builders;

import jakarta.annotation.PostConstruct;
import org.ryjan.telegram.commands.users.user.SendCoins;
import org.ryjan.telegram.commands.users.owner.SetCoins;
import org.ryjan.telegram.commands.users.user.button.bugreport.UserBugReport;
import org.ryjan.telegram.commands.users.user.button.bugreport.UserSendReportReply;
import org.ryjan.telegram.commands.users.user.button.bugreport.UserSendWishReply;
import org.ryjan.telegram.commands.users.utils.BaseUserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserCommandsBuilder {

    @Autowired
    private SendCoins sendCoins;

    @Autowired
    private SetCoins setCoins;

    @Autowired
    private UserBugReport userBugReport;

    @Autowired
    private UserSendReportReply userSendReportReply;

    @Autowired
    private UserSendWishReply userSendWishReply;

    private Map<String, BaseUserCommand> commands = new HashMap<>();
    private Map<String, BaseUserCommand> buttonCommands = new HashMap<>();

    @PostConstruct
    public void initializeCommands() {
        initializeSlashCommands();
        initializeButtonCommands();
    }

    private void initializeSlashCommands() {
        commands.put(setCoins.getCommandName(), setCoins);
        commands.put(sendCoins.getCommandName(), sendCoins);
        commands.put(userBugReport.getCommandName(), userBugReport);
        commands.put(userSendReportReply.getCommandName().split(" ")[0], userSendReportReply);
    }

    private void initializeButtonCommands() {

    }

    public Map<String, BaseUserCommand> getCommands() {
        return commands;
    }

    public void setCommands(Map<String, BaseUserCommand> commands) {
        this.commands = commands;
    }

    public Map<String, BaseUserCommand> getButtonCommands() {
        return buttonCommands;
    }

    public void setButtonCommands(Map<String, BaseUserCommand> buttonCommands) {
        this.buttonCommands = buttonCommands;
    }
}
