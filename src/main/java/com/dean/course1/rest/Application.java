package com.dean.course1.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Arrays;

/**
 * 应用启动类
 * Created by dean on 14/12/7.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application implements ServletContextInitializer {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);

        String[] beanNames = context.getBeanDefinitionNames();

        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.getSessionCookieConfig().setHttpOnly(false);
    }
}
