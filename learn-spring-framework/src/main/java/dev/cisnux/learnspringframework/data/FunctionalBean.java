package dev.cisnux.learnspringframework.data;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Getter
@Component
public class FunctionalBean {
    private final Runnable runnable;


//    public FunctionalBean(@Qualifier(value = "secondRunnable") Runnable runnable) {
//        this.runnable = runnable;
//        runnable.run();
//    }

    public FunctionalBean(@Qualifier(value = "firstRunnable") Runnable runnable) {
        this.runnable = runnable;
        runnable.run();
    }
}
