package dev.cisnux.learnspringframework;


import dev.cisnux.learnspringframework.data.cyclic.CyclicA;
import dev.cisnux.learnspringframework.data.cyclic.CyclicB;
import dev.cisnux.learnspringframework.data.cyclic.CyclicC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CyclicConfiguration {

    @Bean
    public CyclicA cyclicA(CyclicB cyclicB) {
        return new CyclicA(cyclicB);
    }

    @Bean
    public CyclicB cyclicB(CyclicC cyclicC) {
        return new CyclicB(cyclicC);
    }

    @Bean
    public CyclicC cyclicC(CyclicA cyclicA) {
        return new CyclicC(cyclicA);
    }
}
