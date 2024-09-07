package org.ryjan.telegram.repos;

import org.ryjan.telegram.model.ChatSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSettingsRepository extends JpaRepository<ChatSettings, Long> {
}
