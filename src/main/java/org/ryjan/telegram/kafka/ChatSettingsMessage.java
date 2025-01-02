package org.ryjan.telegram.kafka;

import lombok.Getter;
import lombok.Setter;
import org.ryjan.telegram.commands.groups.utils.GroupChatSettings;

@Getter
@Setter
public class ChatSettingsMessage {
    private Long id;
    private GroupChatSettings groupChatSettings;

    public ChatSettingsMessage(Long id, GroupChatSettings groupChatSettings) {
        this.id = id;
        this.groupChatSettings = groupChatSettings;
    }
}
