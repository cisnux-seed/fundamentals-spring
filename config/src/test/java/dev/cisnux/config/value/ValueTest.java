package dev.cisnux.config.value;

import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

@SpringBootTest(classes = ValueTest.TestApplication.class)
public class ValueTest {
    @Autowired
    private TestApplication.ApplicationProperties applicationProperties;

    @Autowired
    private TestApplication.SystemProperties systemProperties;

    @Test
    void testSystemProperties() {
        final var javaHome = systemProperties.getJavaHome();

        Assertions.assertEquals("/Users/fajrarisqulla/.sdkman/candidates/java/current", javaHome);
    }

    @Test
    void testApplicationProperties() {
        final var appName = applicationProperties.getAppName();
        final var version = applicationProperties.getVersion();
        final var isProduction = applicationProperties.isProduction();

        Assertions.assertEquals("config", appName);
        Assertions.assertEquals(1, version);
        Assertions.assertFalse(isProduction);
    }

    @SpringBootApplication
    public static class TestApplication {
        @Component
        @Getter
        public static class SystemProperties {
            @Value(value = "${JAVA_HOME}")
            private String javaHome;
        }

        @Component
        @Getter
        public static class ApplicationProperties {
            @Value(value = "${application.name}")
            private String appName;

            @Value(value = "${application.version}")
            private int version;

            @Value(value = "${application.production-mode}")
            private boolean isProduction;
        }
    }
}
