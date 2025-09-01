package ru.practicum.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserStorage {
    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + " не найден"));
    }

    public List<User> getUsers(List<Long> ids, int from, int size) {
        boolean idsEmpty = ids == null || ids.isEmpty();
        log.info("ids{}  idsEmpty{}  from{}  size{}", ids, idsEmpty, from, size);
        return userRepository.findUsersNative(ids, from, size, idsEmpty);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
