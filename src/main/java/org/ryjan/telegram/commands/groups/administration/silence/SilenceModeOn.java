package org.ryjan.telegram.commands.groups.administration.silence;

import org.ryjan.telegram.commands.groups.BaseGroupCommand;
import org.ryjan.telegram.commands.groups.config.Permission;
import org.ryjan.telegram.handler.GroupCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.springframework.stereotype.Component;

@Component
public class SilenceModeOn extends BaseGroupCommand {
    protected SilenceModeOn() {
        super("silenceOn", "Включить режим тишины", Permission.ADMIN);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, GroupCommandHandler groupCommandHandler) {

    }
}
