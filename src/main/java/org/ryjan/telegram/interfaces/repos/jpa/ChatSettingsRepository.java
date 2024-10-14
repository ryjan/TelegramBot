package org.ryjan.telegram.interfaces.repos.jpa;

import org.ryjan.telegram.model.groups.ChatSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSettingsRepository extends JpaRepository<ChatSettings, Long> {

    ChatSettings findByGroupIdAndSettingKeyAndSettingValue(long groupId, String key, String value);
    ChatSettings findByGroupIdAndSettingKey(long groupId, String key);
}
