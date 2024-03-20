package com.example.openai.connector;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OpenAIClientWrapperTest {

  @Test
  public void send_withValidPrompt_returnsExpectedResponse() {
    // Arrange
    String prompt = "Generate java method that returns the sum of three numbers";
    String expectedResponse = "public int sum(int a, int b, int c) {\n  return a + b + c;\n}";

    OpenAIClientWrapper openAIClientWrapper = OpenAIClientWrapper.getInstance();

    // Act
    String response = openAIClientWrapper.send(prompt);

    // Assert
    assertEquals(expectedResponse, response);
  }

}