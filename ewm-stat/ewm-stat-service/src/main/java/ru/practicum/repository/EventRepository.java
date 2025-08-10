package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.Category;
import ru.practicum.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsAllByCategory(Category category);

    Event findEventByIdAndInitiatorId(Long eventId, Long initiatorId);

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
