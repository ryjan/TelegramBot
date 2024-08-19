package org.ryjan.telegram.controllers;

import org.ryjan.telegram.services.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.io.IOException;

@RestController
public class ChatGPTController {

    @Autowired
    private ChatGPTService chatGPTService;

    @PostMapping({"askchatgpt", "/askchatgpt"})
    public String askСhatGPT(@RequestBody String question) throws IOException {
        return chatGPTService.askQuestion(question);
    }
}
