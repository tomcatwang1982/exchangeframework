package com.tomcatwang.redis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TestService extends BaseService implements IRedisService {

    private static Logger LOGGER = LoggerFactory.getLogger(TestService.class);
    @Override
    public void receiveMessage(String message) {
        LOGGER.info("收到的消息是:=====" + message);
    }
}
