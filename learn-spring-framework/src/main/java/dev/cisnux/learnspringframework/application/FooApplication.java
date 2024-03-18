package dev.cisnux.learnspringframework.application;

import dev.cisnux.learnspringframework.data.Foo;
import dev.cisnux.learnspringframework.listeners.AppStartingListener;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

// by default component scan in spring boot application
// will load all bean in its own packages
@SpringBootApplication
public class FooApplication {
    @Bean
    public Foo foo() {
        return new Foo();
    }

//    public static void main(String[] args) {
//        final var applicationContext = SpringApplication.run(FooApplication.class, args);
//        final var foo = applicationContext.getBean(Foo.class);
//        System.out.println(foo);
//    }

//    public static void main(String[] args) {
//        SpringApplication application = new SpringApplication(FooApplication.class);
//        application.setBannerMode(Banner.Mode.OFF);
//        application.setListeners(List.of(new AppStartingListener()));
//
//        final var applicationContext = application.run(args);
//
//        Foo foo = applicationContext.getBean(Foo.class);
//        System.out.println(foo);
//    }
}