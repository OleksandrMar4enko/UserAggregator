package ua.oleksandr.useraggregator.service;

//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.oleksandr.useraggregator.config.DataSourceConfigProperties;
import ua.oleksandr.useraggregator.config.DataSourceProperties;
import ua.oleksandr.useraggregator.db.entity.User;
import ua.oleksandr.useraggregator.dto.UserReadDto;
import ua.oleksandr.useraggregator.mapper.UserResultMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ua.oleksandr.useraggregator.mapper.UserMapper.INSTANCE;

@Service
@Transactional
public class UserService {

    private final DataSourceConfigProperties properties;
    @Autowired
    private ApplicationContext applicationContext;

    public UserService(@Qualifier("dataSourceConfigProperties")DataSourceConfigProperties properties) {
        this.properties = properties;
    }

    public List<UserReadDto> getAllUser() {
        List<UserReadDto> users = new ArrayList<>();
        for (DataSourceProperties dataSourceProperty : properties.getDatasources()) {
            String dataSourceName = dataSourceProperty.getName();
            NamedParameterJdbcTemplate jdbcTemplate =
                    ((Map<String, NamedParameterJdbcTemplate>)applicationContext
                            .getBean("namedParameterJdbcTemplates")).get(dataSourceName);

            // Выполняем запрос
            String sql = "SELECT * FROM users";
            UserResultMapper userMapper = new UserResultMapper(dataSourceProperty.getMapping());
            List<User> usersfFromDb = jdbcTemplate.query(sql, (rs, rowNum) -> userMapper.map(rs));
            users.addAll(INSTANCE.toReadDtoList(usersfFromDb));
        }
        return users;
    }

}
