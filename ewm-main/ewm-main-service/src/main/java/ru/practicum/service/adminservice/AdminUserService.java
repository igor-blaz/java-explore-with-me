package ru.practicum.service.adminservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.user.UserDto;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserService {
    private final UserStorage userStorage;

    public void deleteUserById(Long userId) {
        userStorage.getUserById(userId);
        userStorage.deleteUserById(userId);
    }

    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        List<User> users = userStorage.getUsers(ids, from, size);
        return UserMapper.userDtoList(users);
    }

    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toModel(userDto);
        User savedUser = userStorage.addUser(user);
        return UserMapper.toUserDto(savedUser);
    }
}
