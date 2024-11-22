package org.ryjan.telegram.commands.groups.level;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.interfaces.Permissions;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.users.User;
import org.ryjan.telegram.services.ServiceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class XpService extends ServiceBuilder {

    @Autowired
    private RedisTemplate<String, User> userRedisTemplate;

    public void chatXpListener(String userId, String message) {
        User user = userRedisTemplate.opsForValue().get(userId);
        if (user == null) {
            user = userService.findUser(userId);
        }
        double xp = calculateXp(message);
        user.setXp(user.getXp() + xp);
        int level = getLevel(user.getLevel(), user.getXp());
        user.setLevel(level);

        userRedisTemplate.opsForValue().set(userId, user, 30, TimeUnit.MINUTES);
        userService.processAndSendUser(user);
    }

    private double xpForNextLevel(int currentLevel, double xp) {
        return xp * Math.pow(currentLevel, 1.5);
    }

    private int getLevel(int currentLevel, double currentXp) {
        int level = currentLevel;
        double xpRequired = xpForNextLevel(currentLevel, currentXp);

        while (currentXp >= xpRequired) {
            currentXp -= xpRequired;
            level++;

            xpRequired = xpForNextLevel(currentLevel, level);
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
