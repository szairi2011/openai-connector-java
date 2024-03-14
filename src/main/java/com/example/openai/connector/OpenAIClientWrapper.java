package com.example.openai.connector;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.devlive.sdk.openai.OpenAiClient;
import org.devlive.sdk.openai.entity.ChatEntity;
import org.devlive.sdk.openai.entity.MessageEntity;
import org.devlive.sdk.openai.model.CompletionModel;
import org.devlive.sdk.openai.response.ChatResponse;

import okhttp3.OkHttpClient;

public class OpenAIClientWrapper {

  private String OPENAI_API_TOKEN = null;
  private static OpenAIClientWrapper INSTANCE;

  private OpenAiClient client;

  public static OpenAIClientWrapper getInstance() {
    if (INSTANCE == null)
      INSTANCE = new OpenAIClientWrapper();

    return INSTANCE;
  }

  private OpenAIClientWrapper() {
    init();
  }

  private OpenAiClient init() {

    // Get the api token from an env variable if it is set up
    OPENAI_API_TOKEN = System.getenv("OPENAI_API_TOKEN");

    // Or else client will need to create the file src/main/resources/secret.config
    // #openai.api.token = <OPENAI_API_TOKEN>
    if (OPENAI_API_TOKEN == null) {
      try (InputStream input = Main.class
          .getClassLoader()
          .getResourceAsStream("secret.config")) {

        Properties prop = new Properties();

        // load a properties file
        prop.load(input);

        // get the property value and print it out
        OPENAI_API_TOKEN = prop.getProperty("openai.api.token");

      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    System.out.println("Loaded openai token from config file: " + OPENAI_API_TOKEN);
    // Use a forward proxy

    InetSocketAddress proxyAddress = new InetSocketAddress("localhost", 3128);

    Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddress);

    // Create a proxy selector
    ProxySelector proxySelector = new ProxySelector() {
      @Override
      public List<Proxy> select(URI uri) {
        return List.of(proxy);
      }

      @Override
      public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        // Handle connection failures if needed
      }
    };

    // Configure OkHttpClient with the proxy selector
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    builder.proxySelector(proxySelector);
    OkHttpClient okHttpClient = builder
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build();

    client = OpenAiClient.builder()
        .apiKey(OPENAI_API_TOKEN)
        .timeout(100)
        .client(okHttpClient) // The proxy shoul be set the last one otherwise it will reset the other props
                              // like apikey
        .build();

    System.err.println("An OpenAI client instance has been initialized ... ");

    return client;
  }

  public String send(String prompt) {

    String response = "";

    List<MessageEntity> messages = new ArrayList<>();
    MessageEntity message = MessageEntity.builder()
        .role("user")
        // .content("Generate java method that returns the sum of three numbers")
        .content(prompt)
        .build();

    messages.add(message);

    ChatEntity configure = ChatEntity.builder()
        // .model(CompletionModel.TEXT_DAVINCI_003)
        .model(CompletionModel.GPT_35_TURBO)
        .messages(messages)
        // .temperature(2D)
        .build();

    // System.out.println(client.createCompletion(configure).getChoices().size());

    ChatResponse responses = client.createChatCompletion(configure);

    if (null != responses) {
      response = responses
          .getChoices()
          .get(0)
          .getMessage()
          .getContent();
    }

    System.out.println("openai returned the response: " + response);

    return response;
  }

}
