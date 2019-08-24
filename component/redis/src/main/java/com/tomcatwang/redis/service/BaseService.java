package com.tomcatwang.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BaseService {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    public void sendMessage(String topic, Object message) {
        redisTemplate.convertAndSend(topic,message);
    }
}
