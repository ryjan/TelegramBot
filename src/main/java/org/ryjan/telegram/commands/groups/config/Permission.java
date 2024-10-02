package org.ryjan.telegram.commands.groups.config;

public enum Permission{
    CREATOR(0),
    ADMIN(1),
    ANY(2);

    private final int permission;

    Permission(int permission){
        this.permission = permission;
    }

    public int getPermission(){
        return permission;
    }
}
