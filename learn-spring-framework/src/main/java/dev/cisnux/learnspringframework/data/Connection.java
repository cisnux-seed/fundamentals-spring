package dev.cisnux.learnspringframework.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

@Slf4j
public class Connection implements InitializingBean, DisposableBean {

    @Override
    public void afterPropertiesSet() {
        log.info("Connection is ready to be used");
    }

    @Override
    public void destroy() {
        log.info("Connection is closed");
    }
}
