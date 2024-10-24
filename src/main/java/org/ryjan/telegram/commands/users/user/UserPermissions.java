package org.ryjan.telegram.commands.users.user;

import org.ryjan.telegram.interfaces.Permissions;

public enum UserPermissions implements Permissions {
    OWNER("Owner"),
    FAMILY("Family"),
    ADMINISTRATOR("Administrator"),
    MODERATOR("Moderator"),
    USER("User"),
    LOW_PRIORITY("Low_priority"),
    ANY("Any"),
    BANNED("Banned");

    private final String displayname;

    UserPermissions(String displayname) {
        this.displayname = displayname;
    }

    @Override
    public String getName() {
        return displayname;
    }

    @Override
    public int getOrdinal() {
        return ordinal();
    }
}
