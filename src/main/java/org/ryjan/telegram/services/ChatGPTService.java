package org.ryjan.telegram.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ryjan.telegram.config.BotConfig;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatGPTService {
    private final String API_URL = BotConfig.CHATGPT_API_URL;
    private final String API_KEY = BotConfig.CHATGPT_API_TOKEN;

    public String askQuestion(String question) {
        String testQuestion = "How to use chatgpt4?";

        ObjectMapper mapper = new ObjectMapper();

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(Map.of("role", "user", "content", question))
        );
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }


}
