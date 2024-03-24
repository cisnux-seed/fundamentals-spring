package dev.cisnux.springasync.async;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static java.lang.StringTemplate.STR;

@Slf4j
@Component
public class HelloAsync {

    @SneakyThrows
    @Async
    public void hello() {
        Thread.sleep(Duration.ofSeconds(2));
        log.info("hello after 2 seconds {}", Thread.currentThread());
    }

    @SneakyThrows
    @Async(value = "singleTaskExecutor")
    public Future<String> hello(final String name) {
        final CompletableFuture<String> completableFuture = new CompletableFuture<>();
        Thread.sleep(Duration.ofSeconds(2));
        completableFuture.complete(STR."Hello \{name} from \{Thread.currentThread()}");
        return completableFuture;
    }
}
