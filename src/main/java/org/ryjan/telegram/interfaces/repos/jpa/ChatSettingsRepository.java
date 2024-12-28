package org.ryjan.telegram.interfaces.repos.jpa;

import org.ryjan.telegram.model.groups.ChatSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.util.List;

public interface ChatSettingsRepository extends JpaRepository<ChatSettings, Long> {
    List<ChatSettings> findByGroupIdIn(List<Long> userId);
    List<ChatSettings> findByGroupIdInAndSettingKey(List<Long> groupId, String key);
    List<ChatSettings> findByGroupIdInAndSettingKeyIn(List<Long> groupId, List<String> key);
    List<ChatSettings> findAllByGroupIdIn(List<Long> groupId);
    ChatSettings findByGroupIdAndSettingKeyAndSettingValue(Long groupId, String key, String value);
    ChatSettings findByGroupIdAndSettingKey(Long groupId, String key);
}
