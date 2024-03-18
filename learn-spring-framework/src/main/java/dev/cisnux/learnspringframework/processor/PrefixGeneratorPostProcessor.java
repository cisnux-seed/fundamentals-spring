package dev.cisnux.learnspringframework.processor;

import dev.cisnux.learnspringframework.aware.IdAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

@Slf4j
// middleware
public class PrefixGeneratorPostProcessor implements BeanPostProcessor, Ordered {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("Id Generator Processor for Bean {}", beanName);
        if (bean instanceof IdAware idAware) {
            log.info("Set Id Generator for Bean {}", beanName);
            idAware.setId("Cisnux - " + idAware.getId());
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
