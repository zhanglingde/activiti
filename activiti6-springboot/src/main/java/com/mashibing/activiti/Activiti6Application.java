package com.mashibing.activiti;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication( exclude = SecurityAutoConfiguration.class)
public class Activiti6Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Activiti6Application.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Activiti6Application.class);
    }
}