package org.ryjan.telegram.commands.groups.administration.silence;

import org.ryjan.telegram.commands.groups.utils.GroupChatSettings;
import org.ryjan.telegram.commands.groups.utils.GroupSwitch;
import org.ryjan.telegram.model.groups.ChatSettings;
import org.ryjan.telegram.services.ChatSettingsService;
import org.ryjan.telegram.services.MainServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class SilenceModeService {
    private final String SILENCE_MODE_KEY = GroupChatSettings.SILENCE_MODE.getDisplayname();
    private final String SILENCE_MODE_END_TIME_KEY = GroupChatSettings.SILENCE_MODE_END_TIME.getDisplayname();
    private final ChatSettingsService chatSettingsService;

    @Autowired
    @Lazy
    private SilenceMode silenceMode;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public SilenceModeService(MainServices mainServices) {
        this.chatSettingsService = mainServices.getChatSettingsService();
    }

    public boolean isSilenceModeEnabled(Long groupId) {
        String cachedValue = redisTemplate.opsForValue().get(SILENCE_MODE_KEY + groupId);
        if (cachedValue != null) {
            return GroupSwitch.ON.getDisplayname().equals(cachedValue);
        }

        ChatSettings chatSettings = chatSettingsService.findSilenceMode(groupId);
        if (chatSettings != null && chatSettings.getSettingValue().equals(GroupSwitch.ON.getDisplayname())) {
            redisTemplate.opsForValue().set(SILENCE_MODE_KEY + groupId, GroupSwitch.ON.getDisplayname(), 1, TimeUnit.HOURS);
            return true;
        }

        redisTemplate.opsForValue().set(SILENCE_MODE_KEY + groupId, GroupSwitch.OFF.getDisplayname(), 1, TimeUnit.HOURS);
        return false;
    }

    public void enableSilenceMode(Long groupId, LocalDateTime endTime) {
        chatSettingsService.addChatSettings(groupId, GroupChatSettings.SILENCE_MODE, GroupSwitch.ON);
        chatSettingsService.addChatSettings(groupId, GroupChatSettings.SILENCE_MODE_END_TIME, String.valueOf(endTime));
        redisTemplate.opsForValue().set(SILENCE_MODE_KEY + groupId, GroupSwitch.ON.getDisplayname(), 1, TimeUnit.HOURS);
        redisTemplate.opsForValue().set(SILENCE_MODE_END_TIME_KEY + groupId, String.valueOf(endTime), 1, TimeUnit.HOURS);
    }

    public void disableSilenceMode(Long groupId) {
        chatSettingsService.addChatSettings(groupId, GroupChatSettings.SILENCE_MODE, GroupSwitch.OFF);
        chatSettingsService.addChatSettings(groupId, GroupChatSettings.SILENCE_MODE_END_TIME, "ended"); // просто удалить chatSettings с postgres
        redisTemplate.opsForValue().set(SILENCE_MODE_KEY + groupId, GroupSwitch.OFF.getDisplayname(), 1, TimeUnit.HOURS);
        redisTemplate.delete(SILENCE_MODE_END_TIME_KEY + groupId);
    }

    public void doDeleteFunction() {
        silenceMode.doDeleteFunction();
    }
}
