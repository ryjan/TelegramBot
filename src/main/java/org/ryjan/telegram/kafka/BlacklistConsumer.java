package org.ryjan.telegram.kafka;

import org.ryjan.telegram.config.RedisConfig;
import org.ryjan.telegram.interfaces.repos.jpa.BlacklistRepository;
import org.ryjan.telegram.interfaces.repos.jpa.UserRepository;
import org.ryjan.telegram.model.groups.Blacklist;
import org.ryjan.telegram.model.users.User;
import org.ryjan.telegram.services.ServiceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.ryjan.telegram.kafka.BlacklistProducer.FIND_BLACKLIST_TOPIC;
import static org.ryjan.telegram.kafka.BlacklistProducer.SEND_BLACKLIST_TOPIC;
import static org.ryjan.telegram.kafka.UserProducer.FIND_USER_TOPIC;

@Service
public class BlacklistConsumer extends ServiceBuilder {
    @Autowired
    private BlacklistRepository blacklistRepository;
    @Autowired
    private RedisTemplate<String, Blacklist> blacklistRedisTemplate;

    @KafkaListener(topics = SEND_BLACKLIST_TOPIC, groupId = SEND_BLACKLIST_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeBlacklists(List<Blacklist> blacklists) {
        blacklistRepository.saveAll(blacklists);
    }

    @KafkaListener(topics = FIND_BLACKLIST_TOPIC, groupId = FIND_BLACKLIST_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void consumeFindUserRequest(List<Long> userId) {
        List<Blacklist> blacklists = blacklistRepository.findByUserIdIn(userId);

        blacklistRedisTemplate.opsForValue().multiSet(blacklists.stream()
                .collect(Collectors.toMap(
                        blacklist -> RedisConfig.BLACKLIST_CACHE_KEY + blacklist.getId(),
                        blacklist -> blacklist)
                ));
    }
}
