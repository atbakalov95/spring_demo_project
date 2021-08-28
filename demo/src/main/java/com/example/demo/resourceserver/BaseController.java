package com.example.demo.resourceserver;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @RequestMapping(value = "/hello")
    public String sayHello() {
        return "Hello authorized user!";
    }
}
