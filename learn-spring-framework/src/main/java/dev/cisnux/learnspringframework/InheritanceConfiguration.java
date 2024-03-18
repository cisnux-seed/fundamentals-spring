package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.services.MerchantServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {MerchantServiceImpl.class})
public class InheritanceConfiguration {
}
