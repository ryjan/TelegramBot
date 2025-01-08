package org.ryjan.telegram.kafka;

import org.ryjan.telegram.config.RedisConfig;
import org.ryjan.telegram.interfaces.repos.jpa.ArticlesRepository;
import org.ryjan.telegram.model.users.Articles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class ArticlesConsumer {
    private static final String REVIEW_ARTICLES_KEY = "REVIEW_ARTICLES_REQUEST";
    private final Map<String, CompletableFuture<Void>> futureMap = new ConcurrentHashMap<>();

    @Autowired
    private ArticlesRepository articlesRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @KafkaListener(topics = ArticlesProducer.SEND_ARTICLES_TOPIC, groupId = ArticlesProducer.SEND_ARTICLES_TOPIC,
            containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeArticles(List<Articles> articles) {
        articlesRepository.saveAll(articles);
    }

    @KafkaListener(topics = ArticlesProducer.SEND_TO_DELETE_ARTICLES_TOPIC, groupId = ArticlesProducer.SEND_TO_DELETE_ARTICLES_TOPIC,
            containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeToDeleteArticles(List<Articles> articles) {
        articlesRepository.deleteAll(articles);
    }

    @KafkaListener(topics = ArticlesProducer.FIND_FIRST_TEN_ARTICLES_TOPIC, groupId = ArticlesProducer.FIND_FIRST_TEN_ARTICLES_TOPIC,
            containerFactory = "kafkaListenerContainerFactory")
    public void consumeFindFirstTenArticles() {
        List<Articles> articles = articlesRepository.findFirst10ByStatusOrderByIdAsc("review");

        redisTemplate.opsForValue().set(RedisConfig.ARTICLES_REVIEW_CACHE_KEY, articles, 3, TimeUnit.MINUTES);

        CompletableFuture<Void> future = futureMap.remove(REVIEW_ARTICLES_KEY);
        if (future != null) {
            future.complete(null);
        }
    }

    public CompletableFuture<Void> registerFuture() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        futureMap.put(REVIEW_ARTICLES_KEY, future);
        return future;
    }
}
