package ua.oleksandr.useraggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ua.oleksandr.useraggregator.config.DataSourceConfigProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties(DataSourceConfigProperties.class)
public class UserAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAggregatorApplication.class, args);
	}

}
