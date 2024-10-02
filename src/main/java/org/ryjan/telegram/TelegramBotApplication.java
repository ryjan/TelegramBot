package org.ryjan.telegram;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.ryjan.telegram.repos.jpa")
@EnableRedisRepositories(basePackages = "org.ryjan.telegram.repos.redis")
@EnableCaching
@EnableScheduling
public class TelegramBotApplication {
    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotApplication.class, args);
    }

    @PostConstruct
    public void getServerPort() {
        System.out.println("Server is running on port: " + 
                applicationContext.getEnvironment().getProperty("local.server.port"));
    }
}
