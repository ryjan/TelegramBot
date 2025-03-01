package org.ryjan.telegram.commands.groups.utils;

import lombok.Getter;

@Getter
public enum GroupSwitch {
    ON("✔️Enabled"),
    OFF("❌Disabled");

    private final String displayname;

    GroupSwitch(String displayname) {
        this.displayname = displayname;
    }
}
