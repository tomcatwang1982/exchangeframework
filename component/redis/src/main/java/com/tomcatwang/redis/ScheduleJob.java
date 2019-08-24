package com.tomcatwang.redis;

import com.tomcatwang.redis.service.RedisReceiver;
import com.tomcatwang.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduleJob {

    @Autowired
    private RedisService redisService;

    @Scheduled(fixedRate = 500)
    public void readRedisData() {

        //todo 定时获取redis数据
        redisService.sendChannelMess("test", "xxxxxxxxx");
    }
}
