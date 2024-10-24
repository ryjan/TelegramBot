package org.ryjan.telegram.services;

import lombok.Getter;
import org.ryjan.telegram.interfaces.repos.jpa.ArticlesRepository;
import org.ryjan.telegram.model.users.Articles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Service
public class ArticlesService extends ServiceBuilder {

    @Getter
    private final String CHECK_ARTICLES_ANSWER = "checkArticlesAnswer:";
    @Getter
    private final String CHECK_ARTICLES_LIST_ANSWER = "checkArticlesListAnswer:";
    @Getter
    private final String CHECK_ARTICLES_LIST_QUEUE = "checkArticlesListQueue:";

    private ScheduledFuture<?> scheduledFuture;

    @Autowired
    private ArticlesRepository articlesRepository;

    @Autowired
    private RedisTemplate<String, List<Articles>> redisTemplate;

    @Autowired
    private TaskScheduler taskScheduler;

    public void startScheduledTask() {
        if (scheduledFuture == null || scheduledFuture.isCancelled()) {
            scheduledFuture = taskScheduler.scheduleAtFixedRate(this::saveArticlesListToDatabase, 900000);
            System.out.println("Redis: scheduled task started");
        }
    }

    public void stopScheduledTask() {
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(true);
            System.out.println("Redis: scheduled task stopped");
        }
    }

    public void saveArticlesListToDatabase() {
        List<Articles> articlesList = redisTemplate.opsForValue().get(CHECK_ARTICLES_LIST_QUEUE);
        if (articlesList != null && !articlesList.isEmpty()) {
            articlesRepository.saveAll(articlesList);
            redisTemplate.delete(CHECK_ARTICLES_LIST_QUEUE);
        } else {
            stopScheduledTask();
        }
    }

    public void addArticleToRedisQueue(Articles article) {
        List<Articles> articlesList = redisTemplate.opsForValue().get(CHECK_ARTICLES_LIST_QUEUE);
        if (articlesList == null || articlesList.isEmpty()) {
            articlesList = new ArrayList<>();
        }
        articlesList.add(article);
        redisTemplate.opsForValue().set(CHECK_ARTICLES_LIST_QUEUE, articlesList);

        startScheduledTask();
    }

    public List<Articles> getFirstTenArticles() {
        return articlesRepository.findFirst10ByStatusOrderByIdAsc("review");
    }

    public void update(Articles articles) {
        articlesRepository.save(articles);
    }

    public void delete(Articles articles) {
        articlesRepository.delete(articles);
    }

}
