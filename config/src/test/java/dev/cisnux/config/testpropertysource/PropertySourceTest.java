package dev.cisnux.config.testpropertysource;

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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;

@SpringBootTest(classes = {PropertySourceTest.TestApplication.class})
@TestPropertySources(value = {@TestPropertySource(value = {"classpath:/test.properties"})})
public class PropertySourceTest {
    @Autowired
    private TestApplication.SampleProperties sampleProperties;

    @Test
    void testPropertySource() {
        Assertions.assertEquals("Sample Project Test", sampleProperties.getName());
        Assertions.assertEquals(1, sampleProperties.getVersion());
    }

    @SpringBootApplication
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
