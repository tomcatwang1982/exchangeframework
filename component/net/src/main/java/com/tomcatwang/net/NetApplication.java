package com.tomcatwang.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NetApplication {

    private Logger logger = LoggerFactory.getLogger(NetApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(NetApplication.class, args);
    }
}
