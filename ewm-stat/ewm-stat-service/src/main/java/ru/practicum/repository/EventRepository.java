package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.Category;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsAllByCategory(Category category);

    Event findEventByIdAndInitiatorId(Long eventId, Long initiatorId);

    @Query(value = """
            SELECT * FROM events e
            WHERE (:usersEmpty OR e.initiator_id IN (:users))
              AND (:statesEmpty OR e.state IN (:states))
              AND (:catsEmpty  OR e.category_id IN (:categories))
              AND (:start IS NULL OR e.event_date >= :start)
              AND (:end   IS NULL OR e.event_date <= :end)
            ORDER BY e.id
            LIMIT :size OFFSET :from
            """, nativeQuery = true)
    List<Event> findAdminEventsNative(
            @Param("users") List<Long> users,
            @Param("usersEmpty") boolean usersEmpty,
            @Param("states") List<String> states,
            @Param("statesEmpty") boolean statesEmpty,
            @Param("categories") List<Long> cats,
            @Param("catsEmpty") boolean catsEmpty,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("from") int from, @Param("size") int size
    );


    @Query(value = """
            SELECT * FROM events
            WHERE (id = :id)
            LIMIT :size OFFSET :from
            """, nativeQuery = true)
    List<Event> findEventsNative(
            @Param("id") Long id,
            @Param("from") int from,
            @Param("size") int size);

}
