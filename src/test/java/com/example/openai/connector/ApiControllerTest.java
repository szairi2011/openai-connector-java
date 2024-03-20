package com.example.openai.connector;

import com.example.openai.connector.controllers.ApiController;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
@SpringBootTest(classes = OpenaiConnectorDemoApplication.class, properties = "spring.profiles.active=test")
//@Test(enabled = false)
public class ApiControllerTest extends AbstractTestNGSpringContextTests {

    private MockMvc mockMvc;
    private OpenAIClientWrapper openAIClientWrapper;

    @BeforeMethod
    public void setUp() {
        openAIClientWrapper = Mockito.mock(OpenAIClientWrapper.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new ApiController(openAIClientWrapper)).build();

    }

    @Test
    public void testGetAiResponse_returnsOk() throws Exception {

        // Arrange :: initialize the mock object
        given(openAIClientWrapper.send("Hello, GET request!")).willReturn("Hello, GET request!");

        // Act
        ResultActions result = mockMvc.perform(get("/api/prompt/Hello%2C+GET+request!"));

        // Assert
                result.andExpect(status().isOk())
                .andExpect(content().string("Hello, GET request!"))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void postAiResponse_returnsOk() throws Exception {

        // Arrange :: initialize the mock object
        given(openAIClientWrapper.send("Hello, POST request!"))
                .willReturn("Hello, POST request!");

        System.out.println("The mocked response: from OpenAIClientWrapper: " + openAIClientWrapper.send("Hello, POST request!"));

        // Act

        ResultActions result = mockMvc.perform(post("/api/prompt")
                        .contentType("application/json")
                        .content("{\"message\": \"Hello, POST request!\"}"));
        // Assert
        result.andExpect(status().isOk())
                .andExpect(content().string("Hello, POST request!"))
                .andDo(MockMvcResultHandlers.print());
    }
}