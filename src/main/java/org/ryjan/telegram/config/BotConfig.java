package org.ryjan.telegram.config;

import org.ryjan.telegram.json.parsing.BotJsonParse;

public class BotConfig {
    public static final String BOT_TOKEN = BotJsonParse.getBotToken();
    public static final String BOT_NAME = BotJsonParse.getBotName();
    public static final String BOT_VERSION = "0.0.1";

    public static final String OWNER_ID = BotJsonParse.getOwner();
    // API
    public static final String CHATGPT_API_URL = BotJsonParse.getChatGPTUrl();
    public static final String CHATGPT_API_TOKEN = BotJsonParse.getChatGPTToken();
}
