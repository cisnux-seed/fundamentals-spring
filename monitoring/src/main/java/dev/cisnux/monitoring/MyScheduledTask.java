package dev.cisnux.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyScheduledTask {
    private final MeterRegistry meterRegistry;

    @Scheduled(fixedRate = 5_000L)
    public void hello() {
        log.info(STR."Hello World!! \{Thread.currentThread()}");
        meterRegistry.counter("my.scheduled.task").increment();
    }
}
