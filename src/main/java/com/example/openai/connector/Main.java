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
import org.devlive.sdk.openai.choice.ChatChoice;
import org.devlive.sdk.openai.entity.ChatEntity;
import org.devlive.sdk.openai.entity.MessageEntity;
import org.devlive.sdk.openai.model.CompletionModel;
import org.devlive.sdk.openai.response.ChatResponse;

import okhttp3.OkHttpClient;

public class Main {
  public static void main(String[] args) {

    String apiToken = System.getenv("OPENAI_API_TOKEN");
    String OPENAI_API_TOKEN;

    if (apiToken == null) {
      try (InputStream input = Main.class
          .getClassLoader()
          .getResourceAsStream("secret.config")) {

        Properties prop = new Properties();

        // load a properties file
        prop.load(input);

        // get the property value and print it out
        OPENAI_API_TOKEN = prop.getProperty("openai.api.token");
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

        OpenAiClient client = OpenAiClient.builder()
          // .client(okHttpClient)
          // .apiHost("http://localhost:80")
          .apiKey(OPENAI_API_TOKEN)
          .timeout(100)
          .client(okHttpClient) // The proxy shoul be set the last one otherwise it will reset the other props like apikey
          .build();

        // OkHttpClient okHttpClient = new OkHttpClient.Builder()
        // .proxy(proxy)
        // .connectTimeout(10, TimeUnit.SECONDS)
        // .writeTimeout(10, TimeUnit.SECONDS)
        // .readTimeout(10, TimeUnit.SECONDS)
        // .build();

        // Test create completion
        // CompletionEntity configure = CompletionEntity.builder()
        //     // .model(CompletionModel.TEXT_DAVINCI_003)
        //     .model(CompletionModel.GPT_35_TURBO)
        //     .prompt("How to create a completion")
        //     .temperature(2D)
        //     .build();


        List<MessageEntity> messages = new ArrayList<>();
        MessageEntity message = MessageEntity.builder()
          .role("user")
          // .content("Generate java method that returns the sum of three numbers")
          .content("Generate a SpringBoot rest controller java class with two methods one for handling GET requests and the second for Post requests. The get will use a url-encoded request prameter called message that will hold a request message")
          .build();

        messages.add(message);

        ChatEntity configure = ChatEntity.builder()
            // .model(CompletionModel.TEXT_DAVINCI_003)
            .model(CompletionModel.GPT_35_TURBO)
            .messages(messages)
            // .temperature(2D)
            .build();

        // System.out.println(client.createCompletion(configure).getChoices().size());

        ChatResponse responses =  client.createChatCompletion(configure);
        if (null != responses) {
          for (ChatChoice resp : responses.getChoices()) {
            System.out.println(resp.getMessage().getContent());
          }
        }
        // System.out.println(client.createChatCompletion(configure).getChoices().size());


      } catch (Exception e) {
        e.printStackTrace();
      }

    }

    // System.out.println(response.getChoices().get(0).getText());
  }
}