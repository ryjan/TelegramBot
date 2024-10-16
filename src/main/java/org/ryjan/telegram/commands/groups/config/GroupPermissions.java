package org.ryjan.telegram.commands.groups.config;

import org.ryjan.telegram.interfaces.Permissions;

public enum GroupPermissions implements Permissions {
    CREATOR(0),
    ADMIN(1),
    ANY(2);

    private final int permission;

    GroupPermissions(int permission){
        this.permission = permission;
    }

    @Override
    public String getName() {
        return String.valueOf(permission);
    }

    @Override
    public int getOrdinal() {
        return ordinal();
    }

    public int getPermission(){
        return permission;
    }

}
