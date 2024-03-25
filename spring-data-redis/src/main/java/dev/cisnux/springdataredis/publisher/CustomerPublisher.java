package dev.cisnux.springdataredis.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.lang.StringTemplate.STR;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerPublisher {
    private final StringRedisTemplate redisTemplate;

    @Scheduled(fixedRate = 3, timeUnit = TimeUnit.SECONDS)
    public void publish(){
        redisTemplate.convertAndSend("customers", STR."Fajra \{UUID.randomUUID()} from \{Thread.currentThread()}");
    }
}
