package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.Comment;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndUser_Id(Long id,
                                         Long userId
    );

    Set<Comment> findAllByUser_IdAndEvent_Id(Long userId,
                                             Long eventId);

    Set<Comment> findAllByUser_Id(Long userId);

    @Query(value = """
            SELECT c.*
            FROM comments c
            WHERE c.author_id = :userId
              AND c.published_on BETWEEN :start AND :end
            ORDER BY c.id
            """, nativeQuery = true)
    Set<Comment> findCommentsByUserIdAndTime(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query(value = """
            SELECT c.*
            FROM comments c
            WHERE c.text ILIKE :text
            AND c.author_id = :userId
            ORDER BY c.id
            """, nativeQuery = true)
    Set<Comment> findCommentByText(@Param("userId") Long userId,
                                   @Param("text") String text);


}
