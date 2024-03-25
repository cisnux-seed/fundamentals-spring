package dev.cisnux.springdataredis.publisher;

import dev.cisnux.springdataredis.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPublisher {
    private final StringRedisTemplate redisTemplate;

    @Scheduled(fixedRate = 2, timeUnit = TimeUnit.SECONDS)
    public void publish() {
        Order order = new Order(UUID.randomUUID().toString(), new Random().nextLong() + 5_000L);
        log.info("order: {} from {}", order, Thread.currentThread());
        ObjectRecord<String, Order> record = ObjectRecord.create("orders", order);
        redisTemplate.opsForStream().add(record);
    }
}
