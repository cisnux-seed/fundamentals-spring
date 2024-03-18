package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.factory.PaymentGatewayClientFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {
        PaymentGatewayClientFactoryBean.class
})
public class FactoryConfiguration {
}
