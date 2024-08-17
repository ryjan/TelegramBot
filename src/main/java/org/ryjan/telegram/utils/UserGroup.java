package org.ryjan.telegram.utils;

public enum UserGroup {
    OWNER("Owner"),
    FAMILY("Family"),
    ADMINISTRATOR("Administrator"),
    MODERATOR("Moderator"),
    USER("User"),
    POOP("Poop");

    private final String displayname;

    UserGroup(String displayname) {
        this.displayname = displayname;
    }

    public String getDisplayname() {
        return displayname;
    }
}
