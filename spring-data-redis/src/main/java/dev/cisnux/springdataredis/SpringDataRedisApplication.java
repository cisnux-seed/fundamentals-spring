package dev.cisnux.springdataredis;

import dev.cisnux.springdataredis.entity.Order;
import dev.cisnux.springdataredis.listener.CustomerListener;
import dev.cisnux.springdataredis.listener.OrderListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Duration;

@RequiredArgsConstructor
@Slf4j
@EnableScheduling
@SpringBootApplication
@EnableRedisRepositories
@EnableCaching
public class SpringDataRedisApplication {
    private final StringRedisTemplate redisTemplate;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public StreamMessageListenerContainer<String, ObjectRecord<String, Order>> orderContainer(RedisConnectionFactory connectionFactory) {
        final var options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(5))
                .targetType(Order.class)
                .build();

        return StreamMessageListenerContainer.create(connectionFactory, options);
    }

    @Bean
    public Subscription orderSubscription(StreamMessageListenerContainer<String, ObjectRecord<String, Order>> orderContainer, OrderListener orderListener) {
        try {
            redisTemplate.opsForStream().createGroup("orders", "my-group");
        } catch (Exception _) {
        }

        var offset = StreamOffset.create("orders", ReadOffset.lastConsumed());
        var consumer = Consumer.from("my-group", "consumer-1");
        var readRequest = StreamMessageListenerContainer.StreamReadRequest
                .builder(offset)
                .consumer(consumer)
                .autoAcknowledge(true)
                .cancelOnError(_ -> false)
                .errorHandler(throwable -> log.warn(throwable.getMessage()))
                .build();

        return orderContainer.register(readRequest, orderListener);
    }

    @Bean
    public RedisMessageListenerContainer messageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                  CustomerListener customerListener) {
        var container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(customerListener, new ChannelTopic("customers"));
        return container;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringDataRedisApplication.class, args);
    }
}
