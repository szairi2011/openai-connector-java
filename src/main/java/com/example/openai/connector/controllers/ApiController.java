package com.example.openai.connector.controllers;

import org.springframework.web.bind.annotation.*;

import com.example.openai.connector.OpenAIClientWrapper;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/prompt/{message}")
    public String getAiResponse(@PathVariable String message ) {
        OpenAIClientWrapper client = OpenAIClientWrapper.getInstance();
        String response = client.send(message);
        // return "Hello, GET request!";
        return response;
    }

    @PostMapping("/prompt")
    public String postAiResponse(@RequestBody String name) {
        // TODO implement
        return "Hello, " + name + "! POST request!";
    }
}
