package org.ryjan.telegram.interfaces;

import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.commands.users.user.UserPermissions;

public interface Permissions {
    String getName();
    int getOrdinal();
    default boolean isAtLeast(Permissions other) {
        if (this.getClass() != other.getClass()) throw new IllegalArgumentException("Cannot compare permissions of different classes");

        return this.getOrdinal() <= other.getOrdinal();
    }
}
