package org.ryjan.telegram.interfaces;

import org.ryjan.telegram.main.BotMain;

public interface IBotCommand {
    void execute(String charId, BotMain bot);
}
