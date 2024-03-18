package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.data.FunctionalBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {FunctionalBean.class, FunctionalConfiguration.class})
public class FunctionalComponent {
}
