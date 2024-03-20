package com.example.openai.connector.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.openai.connector.OpenAIClientWrapper;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
//    @Autowired()
    OpenAIClientWrapper client;

    /**
     * This constructor is used by the TestNG framework while mocking the OpenAIClientWrapper for unit testing offline.
     *
     * @param openAIClientWrapper the mock OpenAIClientWrapper object
     */
    public ApiController(OpenAIClientWrapper openAIClientWrapper) {
        client = OpenAIClientWrapper.getInstance();
    }

    // Empty constructor
    public ApiController() {
    }

    @GetMapping("/prompt/{message}")
    public String getAiResponse(@PathVariable String message ) {
        OpenAIClientWrapper client = OpenAIClientWrapper.getInstance();
        String response = client.send(message);
        if (response == null)
         return "Hello, GET request!";
        return response;
    }

    @PostMapping("/prompt")
    public String postAiResponse(@RequestBody Map<String, String> json) {
        String prompt = json.get("message");
        OpenAIClientWrapper client = OpenAIClientWrapper.getInstance();
        String response = client.send(prompt);
        return response;
    }
}
