package ua.oleksandr.useraggregator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "db")
public class DataSourceConfigProperties {

    private List<DataSourceProperties> datasources;

}
