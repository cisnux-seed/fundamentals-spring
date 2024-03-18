package dev.cisnux.learnspringframework.data;

import lombok.Getter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public class MultiFoo {
    private List<Foo> foos;

    public MultiFoo(ObjectProvider<Foo> objectProvider){
        foos = objectProvider.stream().toList();
    }
}
