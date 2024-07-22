package ua.oleksandr.useraggregator.mapper;

import ua.oleksandr.useraggregator.config.DataSourceProperties;
import ua.oleksandr.useraggregator.db.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserResultMapper implements ResultSetMapper<User> {
    private final DataSourceProperties.Mapping mapping;

    public UserResultMapper(DataSourceProperties.Mapping mapping) {
        this.mapping = mapping;
    }

    public User map(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong(mapping.getId()));
        user.setUsername(resultSet.getString(mapping.getUsername()));
        user.setName(resultSet.getString(mapping.getName()));
        user.setSurname(resultSet.getString(mapping.getSurname()));
        return user;
    }
}
