package dev.cisnux.springasync.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
public class Job {
    private final AtomicLong counter = new AtomicLong(0L);

    @Scheduled(timeUnit = TimeUnit.SECONDS, initialDelay = 2, fixedDelay = 2, scheduler = "taskScheduler")
    public void runJob() {
        final var value = counter.incrementAndGet();
        log.info("{} run job {}", value, Thread.currentThread());
    }

    @Scheduled(cron = "*/2 * * * * *")
    public void runCron() {
        log.info("run cron job");
    }

    public long getCounter() {
        return counter.get();
    }
}
