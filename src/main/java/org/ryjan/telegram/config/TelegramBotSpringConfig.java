package org.ryjan.telegram.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.ryjan.telegram.main.BotMain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Configuration
public class TelegramBotSpringConfig {

    @Bean
    public DefaultBotOptions defaultBotOptions() {
        DefaultBotOptions options = new DefaultBotOptions();

        options.setRequestConfig(createRequestConfig());
        options.setProxyType(DefaultBotOptions.ProxyType.NO_PROXY);

        return options;
    }

    @Bean
    public BotMain telegramBot(DefaultBotOptions options) {
      return new BotMain(options);
    }

    private RequestConfig createRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .build();
    }
}
