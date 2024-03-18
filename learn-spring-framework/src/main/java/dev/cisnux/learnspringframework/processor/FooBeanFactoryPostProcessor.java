package dev.cisnux.learnspringframework.processor;

import dev.cisnux.learnspringframework.data.Foo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;

// create bean programmatically
public class FooBeanFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        final var definition = new GenericBeanDefinition();
        definition.setScope("singleton");
        definition.setBeanClass(Foo.class);

        registry.registerBeanDefinition("foo", definition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // do nothing
    }
}
