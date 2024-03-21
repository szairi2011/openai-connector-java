package com.example.testng.playground;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class Service1Controller {
    @Autowired
    Service1 service1;

    @GetMapping("/response/{messsage}")
    @ResponseBody
    public String getResponse(@PathVariable String messsage) {
        return service1.getResponse(messsage);
    }
}
