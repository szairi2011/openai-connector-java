package com.example.openai.connector.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
public class MyConfig {
    @Autowired
    private Environment env;

//    @Bean
//    public MyService myService() {
//        MyService myService = new MyService();
//        myService.setProperty1(env.getProperty("property1"));
//        myService.setProperty2(env.getProperty("property2"));
//        // ...
//        return myService;
//    }
}
