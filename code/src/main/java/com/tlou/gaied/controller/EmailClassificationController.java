package com.tlou.gaied.controller;

import com.tlou.gaied.service.MailClassifierService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/classifier")
public class EmailClassificationController {

    @Autowired
    MailClassifierService mailClassifierService;


    private final OllamaChatModel ollamaChatModel;

    public EmailClassificationController(ChatClient chatClient, ChatModel chatModel,
                                         OllamaChatModel ollamaChatModel) {

        this.ollamaChatModel = ollamaChatModel;
    }



    @GetMapping("/classifyRequest")
    public String completion(@RequestParam(value = "message",
                                     defaultValue = "What is DealName, amount and dates and also determine the request type and sub request type ?") String message
                             ) {

        ChatResponse chatResponse=  mailClassifierService.classifyText(message);

        return chatResponse.getResult().getOutput().getText();
    }

}
