package com.ndrlslz.configuration.center.spring.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/test")
    public String testEndpoint() {
        return "test";
    }
}
