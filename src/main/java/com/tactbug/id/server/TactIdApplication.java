package com.tactbug.id.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class TactIdApplication {

    public static void main(String[] args) {
        SpringApplication.run(TactIdApplication.class, args);
    }
}
