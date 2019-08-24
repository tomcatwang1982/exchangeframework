package com.tomcatwang.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedisApplication {
    private Logger logger = LoggerFactory.getLogger(RedisApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }
}
