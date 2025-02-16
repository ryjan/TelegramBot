package org.ryjan.telegram.commands.users.user;

import org.ryjan.telegram.interfaces.Permissions;

public enum UserPermissions implements Permissions {
    OWNER("OWNER"),
    TRUSTED("TRUSTED"),
    FAMILY("FAMILY"),
    ADMIN("ADMIN"),
    MODERATOR("MODERATOR"),
    USER("USER"),
    LOW_PRIORITY("LOW_PRIORITY"),
    ANY("ANY"),
    BANNED("BANNED");

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
