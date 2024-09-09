package org.ryjan.telegram.commands.groups;

public enum GroupPrivileges {
    PREMIUM("Premium"),
    VIP("Vip"),
    BASE("Base");

    private final String privilege;

    GroupPrivileges(String privilege) {
        this.privilege = privilege;
    }

    public String getDisplayName() {
        return privilege;
    }
}
