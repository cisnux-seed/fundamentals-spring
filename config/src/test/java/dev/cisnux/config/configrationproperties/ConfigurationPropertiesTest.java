package dev.cisnux.config.configrationproperties;

import dev.cisnux.config.converter.StringToLocalDate;
import dev.cisnux.config.properties.ApplicationProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@SpringBootTest(classes = ConfigurationPropertiesTest.TestApplication.class)
public class ConfigurationPropertiesTest {
    @Autowired
    private ApplicationProperties properties;

    @Test
    void testConfigurationProperties() {
        Assertions.assertEquals("config", properties.getName());
        Assertions.assertEquals(1, properties.getVersion());
        Assertions.assertFalse(properties.isProductionMode());
    }

    @Test
    void testDatabaseProperties() {
        Assertions.assertEquals("fajra", properties.getDatabase().getUsername());
        Assertions.assertEquals("cisnux123", properties.getDatabase().getPassword());
        Assertions.assertEquals("database_test", properties.getDatabase().getDatabase());
        Assertions.assertEquals("jdbc:example", properties.getDatabase().getUrl());
    }

    @Test
    void testCollection() {
        Assertions.assertEquals(List.of("products", "customers", "categories"), properties.getDatabase().getWhitelistTables());
        Assertions.assertEquals(100, properties.getDatabase().getMaxTablesSize().get("products"));
        Assertions.assertEquals(80, properties.getDatabase().getMaxTablesSize().get("customers"));
        Assertions.assertEquals(50, properties.getDatabase().getMaxTablesSize().get("categories"));
    }

    @Test
    void testEmbeddedCollection() {
        Assertions.assertEquals("default", properties.getDefaultRoles().get(0).getId());
        Assertions.assertEquals("Default Role", properties.getDefaultRoles().get(0).getName());
        Assertions.assertEquals("guest", properties.getDefaultRoles().get(1).getId());
        Assertions.assertEquals("Guest Role", properties.getDefaultRoles().get(1).getName());

        Assertions.assertEquals("admin", properties.getRoles().get("admin").getId());
        Assertions.assertEquals("Admin Role", properties.getRoles().get("admin").getName());
        Assertions.assertEquals("finance", properties.getRoles().get("finance").getId());
        Assertions.assertEquals("Finance Role", properties.getRoles().get("finance").getName());
    }

    @Test
    void testDuration() {
        Assertions.assertEquals(Duration.ofSeconds(30), properties.getTimeout());
    }

    @Test
    void testCustomConverter() {
        final var expireDate = properties.getExpireDate();
        final var formattedExpireDate = DateTimeFormatter.ISO_LOCAL_DATE
                .withLocale(Locale.getDefault())
                .format(expireDate);

        System.out.println(formattedExpireDate);

        Assertions.assertEquals("2020-10-10", formattedExpireDate);
    }

    @SpringBootApplication
    @EnableConfigurationProperties({
            ApplicationProperties.class
    })
    @Import(value = {StringToLocalDate.class})
    public static class TestApplication {
        @Bean
        public ConversionService conversionService(StringToLocalDate stringToLocalDate) {
            final var conversionService = new ApplicationConversionService();
            conversionService.addConverter(stringToLocalDate);
            return conversionService;
        }
    }
}
