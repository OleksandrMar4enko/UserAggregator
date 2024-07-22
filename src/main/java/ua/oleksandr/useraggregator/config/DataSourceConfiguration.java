package ua.oleksandr.useraggregator.config;

import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@ComponentScan("ua.oleksandr.useraggregator")
public class DataSourceConfiguration {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataSourceConfigProperties dataSourceConfigProperties;

    @Bean
    public Map<String, NamedParameterJdbcTemplate> namedParameterJdbcTemplates() {
        Map<String, NamedParameterJdbcTemplate> templates = new HashMap<>();
        for (DataSourceProperties dataSource : dataSourceConfigProperties.getDatasources()) {
            DataSource ds = dataSource.buildDataSource();
            templates.put(dataSource.getName(), new NamedParameterJdbcTemplate(ds));
        }
        return templates;
    }

    @Bean
    public Map<String, DataSource> dataSources() {
        List<DataSourceProperties> dataSourceConfigPropertiesList = dataSourceConfigProperties.getDatasources();
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        for (DataSourceProperties properties : dataSourceConfigPropertiesList) {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setUrl(properties.getUrl());
            dataSource.setUsername(properties.getUser());
            dataSource.setPassword(properties.getPassword());
            dataSource.setCatalog(properties.getTable());
            dataSourceMap.put(properties.getName(), dataSource);
        }

        return dataSourceMap;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        return dataSources().values().iterator().next();
    }

    @Bean
    public Map<String, LocalContainerEntityManagerFactoryBean> entityManagerFactories(
            EntityManagerFactoryBuilder builder, JpaVendorAdapter jpaVendorAdapter
    ) {
        Map<String, LocalContainerEntityManagerFactoryBean> factoryBeans = new HashMap<>();
        for (Map.Entry<String, DataSource> entry : dataSources().entrySet()) {
            LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
            factoryBean.setDataSource(entry.getValue());
            factoryBean.setPackagesToScan("ua.oleksandr.useraggregator.db.entity");
            factoryBean.setPersistenceUnitName(entry.getKey());
            factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
            factoryBean.afterPropertiesSet();

            factoryBeans.put(entry.getKey(), factoryBean);
            LOGGER.info(String.format("Created EntityManagerFactory for: %s", entry.getKey()));
        }
        return factoryBeans;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setGenerateDdl(true);
        adapter.setShowSql(true);
        adapter.setDatabase(Database.POSTGRESQL);
        return adapter;
    }

    @Bean
    public Map<String, PlatformTransactionManager> transactionManagers(EntityManagerFactoryBuilder builder,
                                                                       JpaVendorAdapter jpaVendorAdapter) {
        Map<String, PlatformTransactionManager> transactionManagers = new HashMap<>();
        for (Map.Entry<String, LocalContainerEntityManagerFactoryBean> entry : entityManagerFactories(builder,
                jpaVendorAdapter).entrySet()) {
            LocalContainerEntityManagerFactoryBean factoryBean = entry.getValue();
            EntityManagerFactory entityManagerFactory = factoryBean.getObject();
            if (entityManagerFactory != null) {
                transactionManagers.put(entry.getKey(), new JpaTransactionManager(entityManagerFactory));
            } else {
                LOGGER.error(String.format("EntityManagerFactory is null for: %s", entry.getKey()));
            }
        }
        return transactionManagers;
    }

}
