package ua.oleksandr.useraggregator.config;

import lombok.Data;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Data
public class DataSourceProperties {
    private String name;
    private String strategy;
    private String url;
    private String table;
    private String user;
    private String password;
    private Mapping mapping;

    @Data
    public static class Mapping {
        private String id;
        private String username;
        private String name;
        private String surname;
    }

    public DataSource buildDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        return dataSource;
    }
}
