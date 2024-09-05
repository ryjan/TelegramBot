package org.ryjan.telegram.commands.groups;

public enum Privileges {
    PREMIUM("Premium"),
    VIP("Vip"),
    BASE("Base");

    private final String privilege;

    Privileges(String privilege) {
        this.privilege = privilege;
    }

    public String getDisplayName() {
        return privilege;
    }
}
