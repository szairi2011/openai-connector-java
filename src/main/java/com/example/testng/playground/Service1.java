package com.example.testng.playground;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Service1 {
    public void doSomething() {
        System.out.println("Service1");
    }

    public String getResponse(String message) {
        return  message + " " + this.port;
//        return  message + " from Service1";
    }

    @Value("${server.port}")
    private String port;

    public String getPort() {
        return "Hello " + this.port;
    }
}
