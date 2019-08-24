package com.tomcatwang.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@SpringBootApplication
public class CoreApplication {

    private Logger logger = LoggerFactory.getLogger(CoreApplication.class);

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    @Qualifier("kafkaTemplate")
    private KafkaTemplate kafkaTemplate;


    @RequestMapping(value = "/redisTest", method = RequestMethod.GET)
    public String redisTest(@RequestParam String redisValue) {
        redisTemplate.opsForValue().set("redisTest", redisValue);
        return "SUCCESS";
    }

    @RequestMapping(value = "/kafkaProducer", method = RequestMethod.GET)
    public String kafkaProducer(@RequestParam String kafkaProducerValue) {
        kafkaTemplate.send("kafka.tut", kafkaProducerValue);
//        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("kafka.tut", kafkaProducerValue);
//        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
//            @Override
//            public void onFailure(Throwable throwable) {
//                logger.info("Produce: The message failed to be sent:" + throwable.getMessage());
//            }
//
//            @Override
//            public void onSuccess(SendResult<String, String> stringObjectSendResult) {
//                //TODO 业务处理
//                logger.info("Produce: The message was sent successfully:");
//                logger.info("Produce: _+_+_+_+_+_+_+ result: " + stringObjectSendResult.toString());
//            }
//        });
        return "SUCCESS";
    }

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }
}
