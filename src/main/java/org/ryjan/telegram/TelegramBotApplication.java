package org.ryjan.telegram;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.ryjan.telegram.main.BotMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.ryjan.telegram.interfaces.repos.jpa")
@EnableRedisRepositories(basePackages = "org.ryjan.telegram.interfaces.repos.redis")
@EnableCaching
@EnableScheduling
public class TelegramBotApplication {
    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotApplication.class, args);
    }

    @Bean
    public DefaultBotOptions defaultBotOptions() {
        DefaultBotOptions options = new DefaultBotOptions();

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        options.setHttpContext((HttpContext) httpClient);
        return options;
    }

    @PostConstruct
    public void getServerPort() {
        System.out.println("Server is running on port: " + 
                applicationContext.getEnvironment().getProperty("local.server.port"));
    }
}
