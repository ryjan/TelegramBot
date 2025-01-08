package org.ryjan.telegram.kafka;

import org.ryjan.telegram.model.users.Articles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ArticlesProducer {
    public static final String SEND_ARTICLES_TOPIC = "send-articles-topic";
    public static final String SEND_TO_DELETE_ARTICLES_TOPIC = "send-to-delete-articles-topic";
    public static final String FIND_FIRST_TEN_ARTICLES_TOPIC = "find-first-ten-articles-topic";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private ArticlesConsumer articlesConsumer;

    public void sendArticle(Articles article) {
        kafkaTemplate.send(SEND_ARTICLES_TOPIC, article);
    }

    public void sendToDeleteArticle(Articles article) {
        kafkaTemplate.send(SEND_TO_DELETE_ARTICLES_TOPIC, article);
    }

    public CompletableFuture<Void> findFirstTenArticles() {
        CompletableFuture<Void> future = articlesConsumer.registerFuture();
        kafkaTemplate.send(FIND_FIRST_TEN_ARTICLES_TOPIC, "request");
        return future;
    }
}
