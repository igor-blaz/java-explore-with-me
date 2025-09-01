package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = """
            SELECT u.*
            FROM users u
            WHERE (:idsEmpty = true OR u.id IN (:ids))
            ORDER BY u.id
            LIMIT :size OFFSET :offset
            """, nativeQuery = true)
    List<User> findUsersNative(
            @Param("ids") List<Long> ids,
            @Param("offset") int offset,
            @Param("size") int size,
            @Param("idsEmpty") boolean idsEmpty
    );

    boolean existsByEmail(String email);

}
