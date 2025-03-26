package com.tlou.gaied.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/classifier")
public class EmailClassificationController {


    private final OllamaChatModel ollamaChatModel;

    public EmailClassificationController(ChatClient chatClient, ChatModel chatModel,
                                         OllamaChatModel ollamaChatModel) {

        this.ollamaChatModel = ollamaChatModel;
    }


    @Value("classpath:/data/mailClassifier.txt")
    Resource mailClassifier;

    @Value("classpath:/prompt/qa-prompt.st")
    private Resource qaPromptResource;

    @Value("classpath:/data/testMail.txt") Resource textResource;

    @GetMapping("/classifyRequest")
    public String completion(@RequestParam(value = "message",
                                     defaultValue = "What is DealName, amount and dates and also determine the request type and sub request type ?") String message
                             ) {
        PromptTemplate promptTemplate = new PromptTemplate(qaPromptResource);
        Map<String, Object> map = new HashMap<>();
        map.put("question", message);
        map.put("context", mailClassifier);
        map.put("context2", textResource);

        Prompt prompt = promptTemplate.create(map);
        ChatResponse chatResponse = ChatClient.builder(ollamaChatModel).build().prompt(prompt).call().chatResponse();

        return chatResponse.getResult().getOutput().getText();
    }

}
