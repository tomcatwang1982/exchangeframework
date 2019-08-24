package com.tomcatwang.redis.service;

import com.tomcatwang.redis.controller.PubController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisReceiver {

    private Logger LOGGER = LoggerFactory.getLogger(RedisReceiver.class);

    @Autowired
    RedisService redisService;

    public void receiveMessage(String message) {
        LOGGER.info("收到的消息是:=====" + message);
    }
}
