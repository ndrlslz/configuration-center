package com.ndrlslz.configuration.center.spring.app.controller;

import com.ndrlslz.configuration.center.spring.annotation.Config;
import com.ndrlslz.configuration.center.spring.app.configuration.ChildConfiguration;
import com.ndrlslz.configuration.center.spring.app.configuration.SecondConfiguration;
import com.ndrlslz.configuration.center.spring.app.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private ChildConfiguration childConfiguration;

    @Autowired
    private SecondConfiguration secondConfiguration;

    @Config(value = "app", refresh = true)
    private String app;

    @RequestMapping("/coder")
    public Result coder() {
        Result result = new Result();
        result.setAddress(childConfiguration.getAddress());
        result.setJavaer(childConfiguration.isJavaer());
        result.setEmailAddress(childConfiguration.getEmailAddress());
        result.setAge(childConfiguration.getAge());
        result.setName(childConfiguration.getName());
        result.setApp(app);
        result.setSecondAge(secondConfiguration.getAge());
        result.setSecondName(secondConfiguration.getName());
        return result;
    }
}
