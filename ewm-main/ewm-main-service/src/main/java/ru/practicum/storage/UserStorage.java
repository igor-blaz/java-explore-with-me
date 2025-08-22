package ru.practicum.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserStorage {
    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + " не найден"));
    }
}
