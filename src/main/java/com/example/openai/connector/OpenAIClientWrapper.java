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

import com.example.openai.connector.configurations.SpringContextHolder;
import org.devlive.sdk.openai.OpenAiClient;
import org.devlive.sdk.openai.entity.ChatEntity;
import org.devlive.sdk.openai.entity.MessageEntity;
import org.devlive.sdk.openai.model.CompletionModel;
import org.devlive.sdk.openai.response.ChatResponse;

import okhttp3.OkHttpClient;
import org.springframework.core.env.Environment;

public class OpenAIClientWrapper {

  private String OPENAI_API_TOKEN = null;
  private static OpenAIClientWrapper INSTANCE;
  private OpenAiClient client;
  Environment env;

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

    // Or else client will need to create the file src/main/resources/aopenai.config
    // #openai.api.token = <OPENAI_API_TOKEN>
    if (OPENAI_API_TOKEN == null) {
      env = SpringContextHolder.getApplicationContext().getEnvironment();
      OPENAI_API_TOKEN = env.getProperty("openai.api.token");
    }

    System.out.println("Loaded openai token from config file: " + OPENAI_API_TOKEN);
    // Use a forward proxy

    // Set up an http proxy using either an en variable, e.g. case of K8s deployment, or a config property e.g. if to run locally
    String USE_PROXY = System.getenv("USE_PROXY");
    String PROXY_HOST = System.getenv("PROXY_HOST");
    String PROXY_PORT = System.getenv("PROXY_PORT");

    if (USE_PROXY == null) { // If env vars are not set we fall back to use con props if they exist
      USE_PROXY = env.getProperty("proxy.use");
      PROXY_HOST = env.getProperty("proxy.host");
      PROXY_PORT = env.getProperty("proxy.port");
    }

    if (null != USE_PROXY && USE_PROXY.equals("true") ) {

      InetSocketAddress proxyAddress = new InetSocketAddress(PROXY_HOST, Integer.parseInt(PROXY_PORT));

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
              .client(okHttpClient) // The proxy should be set the last one otherwise it will reset the other props
              // like apikey
              .build();

      System.err.println("An OpenAI client instance has been initialized using proxy ");
    }
    else { // If no proxy settings required
      client = OpenAiClient.builder()
              .apiKey(OPENAI_API_TOKEN)
              .timeout(100)
              .build();

      System.err.println("An OpenAI client instance has been initialized to connect without a proxy ");
    }

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
