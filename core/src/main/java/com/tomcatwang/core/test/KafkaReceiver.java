package com.tomcatwang.common.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaReceiver {

    private static Logger log = LoggerFactory.getLogger(KafkaReceiver.class);

    @KafkaListener(topics = "kafka.tut", id = "executor")
    public void listen(String message) {
        log.info("------------------接收消息 message =" + message);
        log.info("------------------ message =" + message);
    }

}
