package com.example.testng.playground;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.testng.Assert.*;

import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
//@SpringBootTest(classes = SpringBootDemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(classes = OpenaiConnectorTestsDemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "spring.profiles.active=test")
public class Service1Test extends AbstractTestNGSpringContextTests {
//public class Service1Test {
    @Autowired
    private Service1 service1;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void doSomethingTest() {
//        Service1 service1 = new Service1();
        service1.doSomething();
    }

    @Test
    public void getResponseTest() {
//        Service1 service1 = new Service1();
        String response = service1.getResponse("Hello World");
        assertEquals("Hello World 8085", response);
    }

    @Test
    public void testHome() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity("/api/response/Hello World", String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo("Hello World 8085");
    }
}