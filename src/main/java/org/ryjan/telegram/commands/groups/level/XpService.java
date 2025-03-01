package org.ryjan.telegram.commands.groups.level;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.config.RedisConfig;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.interfaces.Permissions;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.users.User;
import org.ryjan.telegram.services.ServiceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class XpService extends ServiceBuilder {
    private final String CACHE_KEY = RedisConfig.USER_CACHE_KEY;

    @Autowired
    private RedisTemplate<String, User> userRedisTemplate;

    @Async
    public void chatXpListener(String userId, String username, String message) {
        User user = userRedisTemplate.opsForValue().get(CACHE_KEY + userId);
        if (user == null) {
            user = userService.findUser(userId) != null ? userService.findUser(userId) : userService.createUser(new User(Long.valueOf(userId), username.toLowerCase()));
        }
        double xp = calculateXp(message);
        user.setXp(user.getXp() + xp);
        int level = getLevel(user.getLevel(), user.getXp());
        user.setLevel(level);

        userRedisTemplate.opsForValue().set(CACHE_KEY + user.getId(), user, 15, TimeUnit.MINUTES);
        userService.save(user);
    }

    public double xpForNextLevel(int currentLevel) {
        return 100 * Math.pow(currentLevel, 1.5);
    }

    private int getLevel(int currentLevel, double currentXp) {
        int level = currentLevel;
        double xpRequired = xpForNextLevel(currentLevel);

        while (currentXp >= xpRequired) {
            currentXp -= xpRequired;
            level++;

            xpRequired = xpForNextLevel(currentLevel);
        }
        return level;
    }

    private double calculateXp(String message) {
        if (message.startsWith("/")) return 0;

        double messageLengthWeight = 0.5;
        double punctuationWeight = 2.0;
        double bonusForWords = 1.0;

        int length = message.replace(" ", "").length();
        long punctuationCount = message.chars()
                .filter(c -> ",.:;!?".indexOf(c) != -1)
                .count();
        int wordCount = message.split("\\s+").length;

        return (messageLengthWeight * length + punctuationWeight * punctuationCount + bonusForWords * Math.sqrt(wordCount));
    }
}
