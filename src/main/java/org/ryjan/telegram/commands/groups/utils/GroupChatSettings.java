package org.ryjan.telegram.commands.groups.utils;

import lombok.Getter;

@Getter
public enum GroupChatSettings {
    BLACKLIST("❌Blacklist"),
    BLACKLIST_NOTIFICATIONS("🔔BlacklistNotifications"),
    SILENCE_MODE("🤫SilenceMode"),
    SILENCE_MODE_END_TIME("⏳SilenceModeEndTime"),
    LEVELS("🎫Levels");

    private final String displayname;

    GroupChatSettings(String displayname) {
        this.displayname = displayname;
    }
}