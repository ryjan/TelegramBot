package org.ryjan.telegram.commands.groups.utils;

public enum GroupStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    BANNED("BANNED");

    private final String status;

    GroupStatus(String status) {
        this.status = status;
    }

    public String getDisplayName() {
        return status;
    }
}
