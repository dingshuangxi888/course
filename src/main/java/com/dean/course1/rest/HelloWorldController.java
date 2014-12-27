package com.dean.course1.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * HelloWorld控制器
 * Created by dean on 14/12/7.
 */
@RestController
public class HelloWorldController {

    @RequestMapping("/")
    public String index(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                cookie.setHttpOnly(true);
            }
        }
        return "My first spring rest project!";
    }
}
