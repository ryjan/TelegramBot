package org.ryjan.telegram.repos;

import org.ryjan.telegram.model.groups.ChatSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSettingsRepository extends JpaRepository<ChatSettings, Long> {

    ChatSettings findByIdAndSettingKeyAndSettingValue(Long id, String key, String value);
}
