package dev.cisnux.config.propertysource;

import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@SpringBootTest(classes = {PropertySourceTest.TestApplication.class})
public class PropertySourceTest {
    @Autowired
    private TestApplication.SampleProperties sampleProperties;

    @Test
    void testPropertySource() {
        Assertions.assertEquals("Sample Project", sampleProperties.getName());
        Assertions.assertEquals(1, sampleProperties.getVersion());
    }

    @SpringBootApplication
    @PropertySources(value = {@PropertySource(value = {"classpath:/sample.properties"})})
    public static class TestApplication {
        @Component
        @Getter
        public static class SampleProperties {
            @Value("${sample.name}")
            private String name;
            @Value("${sample.version}")
            private int version;
        }
    }
}
