package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.controller.publiccontroller.SortType;
import ru.practicum.model.Category;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsAllByCategory(Category category);

    Event findEventByIdAndInitiatorId(Long eventId, Long initiatorId);


    @Query(value = """
            SELECT * FROM events e
            WHERE (:text IS NULL OR e.description ILIKE :textPattern
                OR e.annotation ILIKE :textPattern
                OR e.title ILIKE :textPattern)
              AND (:categoriesIsEmpty OR e.category_id IN (:categories))
              AND (:paid IS NULL OR e.paid = :paid)
              AND (:start IS NULL OR e.event_date >= :start)
              AND (:end   IS NULL OR e.event_date <= :end)
              AND (:now IS NULL OR e.event_date >= :now)
              AND (:onlyAvailable = false
                   OR e.participant_limit = 0
                   OR e.confirmed_requests < e.participant_limit)
            ORDER BY
                  CASE WHEN :sort = 'EVENT_DATE' THEN e.event_date END,
                  CASE WHEN :sort = 'VIEWS'      THEN e.views      END,
                  e.id
            LIMIT :size OFFSET :from
            """, nativeQuery = true)
    List<Event> getEventsPublic(
            @Param("textPattern") String textPattern,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("start") LocalDateTime rangeStart,
            @Param("end") LocalDateTime rangeEnd,
            @Param("now") LocalDateTime now,
            @Param("onlyAvailable") boolean onlyAvailable,
            @Param("sort") SortType sort,
            @Param("from") int from,
            @Param("size") int size,
            @Param("categoriesIsEmpty") boolean categoriesIsEmpty

    );


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
            @Param("usersEmpty") boolean usersEmpty,
            @Param("statesEmpty") boolean statesEmpty,
            @Param("catsEmpty") boolean catsEmpty,

            @Param("users") List<Long> users,
            @Param("states") List<String> states,
            @Param("categories") List<Long> cats,
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
