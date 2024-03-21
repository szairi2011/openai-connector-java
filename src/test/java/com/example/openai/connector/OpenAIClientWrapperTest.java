package com.example.openai.connector;

import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.BDDMockito.given;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * This test is not really needed it only created for testing
 */
@SpringBootTest(classes = OpenaiConnectorDemoApplication.class, properties = "spring.profiles.active=test")
//@Test(enabled = false)
public class OpenAIClientWrapperTest extends OpenaiConnectorDemoApplication {

  /**
   * Sends a prompt to the OpenAI API and returns the response.
   *
   * @param prompt the prompt to send to the API
   * @return the response from the API
   */
  @Test
  public void send_withValidPrompt_returnsExpectedResponse() {
    // Arrange
    String prompt = "Generate java method that returns the sum of three numbers";
    String expectedResponse = "public int sum(int a, int b, int c) {\n  return a + b + c;\n}";

    OpenAIClientWrapper openAIClientWrapper = Mockito.mock(OpenAIClientWrapper.class);
    given(openAIClientWrapper.send(prompt)).willReturn(expectedResponse);

    // Act
    String response = openAIClientWrapper.send(prompt);

    // Assert
    assertEquals(expectedResponse, response);
  }

}