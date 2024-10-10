package org.ryjan.telegram.commands.groups.administration.silence;

import org.ryjan.telegram.model.groups.ChatSettings;
import org.ryjan.telegram.services.ChatSettingsService;
import org.ryjan.telegram.services.GroupService;
import org.ryjan.telegram.services.MainServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class SilenceModeService {

    @Autowired
    @Lazy
    private SilenceMode silenceMode;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final String SILENCE_MODE_KEY = "silenceMode";
    private final String SILENCE_MODE_END_TIME_KEY = "silenceModeEndTime";
    private final ChatSettingsService chatSettingsService;

    @Autowired
    public SilenceModeService(MainServices mainServices) {
        this.chatSettingsService = mainServices.getChatSettingsService();
    }

    public boolean isSilenceModeEnabled(Long groupId) {
        String cachedValue = redisTemplate.opsForValue().get(SILENCE_MODE_KEY + groupId);
        if (cachedValue != null) {
            return "enabled".equals(cachedValue);
        }

        ChatSettings chatSettings = chatSettingsService.findChatSettings(groupId, SILENCE_MODE_KEY);
        if (chatSettings != null && chatSettings.getSettingValue().equals("enabled")) {
            redisTemplate.opsForValue().set(SILENCE_MODE_KEY + groupId, "enabled", 1, TimeUnit.HOURS);
            return true;
        }

        redisTemplate.opsForValue().set(SILENCE_MODE_KEY + groupId, "disabled", 1, TimeUnit.HOURS);
        return false;
    }

    public void enableSilenceMode(Long groupId, LocalDateTime endTime) {
        chatSettingsService.addChatSettings(groupId, SILENCE_MODE_KEY, "enabled");
        chatSettingsService.addChatSettings(groupId, SILENCE_MODE_END_TIME_KEY, String.valueOf(endTime));
        redisTemplate.opsForValue().set(SILENCE_MODE_KEY + groupId, "enabled", 1, TimeUnit.HOURS);
        redisTemplate.opsForValue().set(SILENCE_MODE_END_TIME_KEY + groupId, String.valueOf(endTime), 1, TimeUnit.HOURS);
    }

    public void disableSilenceMode(Long groupId) {
        chatSettingsService.addChatSettings(groupId, SILENCE_MODE_KEY, "disabled");
        chatSettingsService.addChatSettings(groupId, SILENCE_MODE_END_TIME_KEY, "ended");
        redisTemplate.opsForValue().set(SILENCE_MODE_KEY + groupId, "disabled", 1, TimeUnit.HOURS);
        redisTemplate.delete(SILENCE_MODE_END_TIME_KEY + groupId);
    }

    public void doDeleteFunction() {
        silenceMode.doDeleteFunction();
    }
}
