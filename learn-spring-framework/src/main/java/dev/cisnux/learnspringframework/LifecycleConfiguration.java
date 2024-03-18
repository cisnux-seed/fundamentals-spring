package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.data.Connection;
import dev.cisnux.learnspringframework.data.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LifecycleConfiguration {
    @Bean
    public Connection connection() {
        return new Connection();
    }

//    @Bean(initMethod = "start", destroyMethod = "stop")
    @Bean()
    public Server server() {
        return new Server();
    }
}
