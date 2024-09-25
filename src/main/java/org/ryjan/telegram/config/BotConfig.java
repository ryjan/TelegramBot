package org.ryjan.telegram.config;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

public class BotConfig {
    @Value("${bot.token}")
    public static String BOT_TOKEN;
    @Value("${bot.username}")
    public static String BOT_NAME;
    @Value("${bot.version}")
    public static String BOT_VERSION;

    public static final String OWNER_ID = "";
    // API
    public static final String CHATGPT_API_URL = "";
    public static final String CHATGPT_API_TOKEN = "";
    //Transfer
    public static final BigDecimal DAILY_LIMIT = new BigDecimal("100");
}
