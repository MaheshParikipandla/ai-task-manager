package com.mahi.aitaskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

@SpringBootApplication(exclude = FlywayAutoConfiguration.class)
public class AiTaskManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiTaskManagerApplication.class, args);
    }
}
