package dev.cisnux.learnspringframework.data;

import dev.cisnux.learnspringframework.aware.IdAware;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Car implements IdAware {
    private String id;

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
