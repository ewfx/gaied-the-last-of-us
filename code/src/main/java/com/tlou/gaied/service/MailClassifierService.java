package com.tlou.gaied.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MailClassifierService {

    @Value("classpath:/data/mailClassifier.txt")
    Resource mailClassifier;

    @Value("classpath:/prompt/qa-prompt.st")
    private Resource qaPromptResource;

    @Value("classpath:/data/testMail.txt") Resource textResource;

    @Autowired
    private OllamaChatModel ollamaChatModel;

    public ChatResponse classifyText(String message) {

        PromptTemplate promptTemplate = new PromptTemplate(qaPromptResource);
        Map<String, Object> map = new HashMap<>();
        map.put("question", message);
        map.put("context", mailClassifier);
        map.put("context2", textResource);

        Prompt prompt = promptTemplate.create(map);
        ChatResponse chatResponse = ChatClient.builder(ollamaChatModel).build().prompt(prompt).call().chatResponse();
        return chatResponse;
    }
}
