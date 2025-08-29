package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = """
            SELECT * FROM users
            WHERE id IN (:ids)
            LIMIT :size OFFSET :from
            """, nativeQuery = true)
    List<User> getUsersNative(
            @Param("ids") List<Long> ids,
            @Param("from") int from,
            @Param("size") int size);

}
