package com.tomcatwang.redis.controller;

import com.tomcatwang.redis.service.TestService;
import com.tomcatwang.redis.spring.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class PubController {

    private Logger LOGGER = LoggerFactory.getLogger(PubController.class);

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    private static AtomicInteger count = new AtomicInteger();

    @RequestMapping(value = "/redistest", method = RequestMethod.GET)
    public void sendMessage() {
        int i = count.incrementAndGet();
        LOGGER.info("消息======>" + i);
        redisTemplate.convertAndSend("topictest", "消息" + i);
    }

    @RequestMapping(value = "/sub", method = RequestMethod.GET)
    public void sub(@RequestParam String topics, @RequestParam  String event) {
        if (null != event && !event.equals("")) {
            TestService service = new TestService();
            MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter();
            messageListenerAdapter.setDefaultListenerMethod("receiveMessage");
            messageListenerAdapter.setDelegate(service);
            RedisMessageListenerContainer container = ApplicationContextProvider.getBean("redisMessageListenContainer", RedisMessageListenerContainer.class);
            container.addMessageListener(messageListenerAdapter,new PatternTopic(topics));
        }
    }

    @RequestMapping(value = "/cancelSub", method = RequestMethod.GET)
    public void cancelSub(@RequestParam String topics, @RequestParam  String event) {
        if (null != event && !event.equals("")) {
            TestService service = new TestService();
            MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter();
            messageListenerAdapter.setDefaultListenerMethod("receiveMessage");
            messageListenerAdapter.setDelegate(service);
            RedisMessageListenerContainer container = ApplicationContextProvider.getBean("redisMessageListenContainer", RedisMessageListenerContainer.class);
            container.removeMessageListener(messageListenerAdapter,new PatternTopic(topics));
        }
    }


}
