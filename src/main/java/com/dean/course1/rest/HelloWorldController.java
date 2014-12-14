package com.dean.course1.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloWorld控制器
 * Created by dean on 14/12/7.
 */
@RestController
public class HelloWorldController {

    @RequestMapping("/")
    public String index() {
        return "My first spring rest project!";
    }
}
