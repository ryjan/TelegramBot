package org.ryjan.telegram.services;

import org.ryjan.telegram.config.RedisConfig;
import org.ryjan.telegram.kafka.ArticlesProducer;
import org.ryjan.telegram.model.users.Articles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

@Service
public class ArticlesService extends ServiceBuilder {
    public static final String CHECK_ARTICLES_ANSWER = "checkArticlesAnswer:";
    public static final String CHECK_ARTICLES_LIST_ANSWER = "checkArticlesListAnswer:";

    @Autowired
    private RedisTemplate<String, List<Articles>> redisTemplate;
    @Autowired
    private ArticlesProducer articlesProducer;

    public List<Articles> findFirstTenArticles() {
        CompletableFuture<Void> future = articlesProducer.findFirstTenArticles();

        try {
            future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException("Failed to fetch article from Kafka", e);
        }
        return redisTemplate.opsForValue().get(RedisConfig.ARTICLES_REVIEW_CACHE_KEY);
    }

    public void save(Articles articles) {
        articlesProducer.sendArticle(articles);
    }

    public void delete(Articles articles) {
        articlesProducer.sendToDeleteArticle(articles);
    }

}
