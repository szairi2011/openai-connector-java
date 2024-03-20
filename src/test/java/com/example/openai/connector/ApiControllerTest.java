package com.example.openai.connector;

import com.example.openai.connector.controllers.ApiController;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
public class ApiControllerTest {

    private MockMvc mockMvc;
    private OpenAIClientWrapper openAIClientWrapper;


    @Test
    public void testGetAiResponse_returnsOk() throws Exception {
        openAIClientWrapper = Mockito.mock(OpenAIClientWrapper.class);
//        mockMvc = MockMvcBuilders.standaloneSetup(new ApiController(openAIClientWrapper)).build();
//
//        given(openAIClientWrapper.send("Hello, GET request!")).willReturn("Hello, GET request!");

        mockMvc.perform(get("/api/prompt/Hello%2C+GET+request!"))
                .andExpect(status().isOk());
    }


    @Test
    public void postAiResponse_returnsOk() throws Exception {
        OpenAIClientWrapper openAIClientWrapper = Mockito.mock(OpenAIClientWrapper.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new ApiController(openAIClientWrapper)).build();

        given(openAIClientWrapper.send("Hello, POST request!"))
                .willReturn("Hello, POST request!");

        mockMvc.perform(post("/api/prompt")
                        .contentType("application/json")
                        .content("{\"message\": \"Hello, POST request!\"}"))
                .andExpect(status().isOk())
                .andReturn();
    }
}