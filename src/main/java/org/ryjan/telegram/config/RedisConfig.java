package org.ryjan.telegram.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ryjan.telegram.model.groups.Blacklist;
import org.ryjan.telegram.model.groups.ChatSettings;
import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.model.users.Articles;
import org.ryjan.telegram.model.users.Bank;
import org.ryjan.telegram.model.users.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;


@Configuration
public class RedisConfig {
    public static final String USER_CACHE_KEY = "users:";
    public static final String GROUP_CACHE_KEY = "group:";
    public static final String BLACKLIST_CACHE_KEY = "blacklist:";
    public static final String CHAT_SETTINGS_CACHE_KEY = "chatSettings:";
    public static final String ARTICLES_REVIEW_CACHE_KEY = "articles_review:";

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues();
    }

    @Bean
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL
        );

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}
