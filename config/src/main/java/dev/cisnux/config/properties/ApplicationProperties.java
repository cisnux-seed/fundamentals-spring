package dev.cisnux.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    private String name;
    private int version;
    private boolean productionMode;
    private DatabaseProperties database;
    private List<Role> defaultRoles;
    private Map<String, Role> roles;
    private Duration timeout;
    private LocalDate expireDate;

    @Getter
    @Setter
    public static class DatabaseProperties {
        private String username;
        private String password;
        private String database;
        private String url;
        private List<String> whitelistTables;
        private Map<String, Integer> maxTablesSize;
    }

    @Getter
    @Setter
    public static class Role {
        private String id;
        private String name;
    }
}