package org.ryjan.telegram.commands.groups;

public enum GroupPrivileges {
    PREMIUM("PREMIUM"),
    VIP("VIP"),
    BASE("BASE");

    private final String privilege;

    GroupPrivileges(String privilege) {
        this.privilege = privilege;
    }

    public String getDisplayName() {
        return privilege;
    }
}
