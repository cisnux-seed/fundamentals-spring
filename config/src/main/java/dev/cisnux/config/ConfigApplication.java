package dev.cisnux.config;

import dev.cisnux.config.converter.StringToLocalDate;
import dev.cisnux.config.properties.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;

@SpringBootApplication
@EnableConfigurationProperties({
		ApplicationProperties.class
})
public class ConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigApplication.class, args);
	}

	@Bean
	public ConversionService conversionService(StringToLocalDate stringToLocalDate) {
		final var conversionService = new ApplicationConversionService();
		conversionService.addConverter(stringToLocalDate);
		return conversionService;
	}
}
