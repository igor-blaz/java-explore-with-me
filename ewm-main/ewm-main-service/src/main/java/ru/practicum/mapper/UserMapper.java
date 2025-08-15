package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.User;

@UtilityClass
public class UserMapper {
    public static UserDto toUserDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
    public static UserShortDto toUserShortDto(User user){
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
