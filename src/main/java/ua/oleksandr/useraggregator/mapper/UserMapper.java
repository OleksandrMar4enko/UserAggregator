package ua.oleksandr.useraggregator.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ua.oleksandr.useraggregator.db.entity.User;
import ua.oleksandr.useraggregator.dto.UserReadDto;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserReadDto toUserReadDto(User user);

    List<UserReadDto> toReadDtoList(List<User> users);

}
