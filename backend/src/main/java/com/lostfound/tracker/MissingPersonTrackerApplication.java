package com.lostfound.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class MissingPersonTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MissingPersonTrackerApplication.class, args);
    }

}