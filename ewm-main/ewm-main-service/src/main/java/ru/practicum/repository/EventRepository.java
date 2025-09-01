package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.Category;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsAllByCategory(Category category);


    Optional<Event> findByIdAndInitiator_Id(Long eventId, Long initiatorId);

    @Query(value = """
            select * from events e
            where e.id = :eventId
              and e.published_on IS NOT NULL
              and e.state = 'PUBLISHED'
            """, nativeQuery = true)
    Optional<Event> findPublishedById(@Param("eventId") Long eventId
    );

    @Query(value = """
            select * from events e
            where e.id = :eventId
              and e.initiator_id = :initiatorId
              and e.state = 'PUBLISHED'
            """, nativeQuery = true)
    Optional<Event> findPublishedByIdAndInitiatorId(
            @Param("eventId") Long eventId,
            @Param("initiatorId") Long initiatorId);


    @Query(value = """
            SELECT e.*
            FROM events e
            WHERE
              (:textPattern IS NULL OR e.description ILIKE :textPattern
                 OR e.annotation ILIKE :textPattern
                 OR e.title      ILIKE :textPattern)
              AND e.state = 'PUBLISHED'
              AND e.published_on IS NOT NULL
              AND e.category_id IN (:categories)
              AND e.paid      = COALESCE(:paid, e.paid)
              AND e.event_date >= :start
              AND e.event_date <= COALESCE(:end, e.event_date)
              AND (
                    NOT :onlyAvailable
                    OR e.participant_limit = 0
                    OR e.confirmed_requests < e.participant_limit
                  )
            ORDER BY
              CASE WHEN :sort = 'EVENT_DATE' THEN e.event_date END NULLS LAST,
              CASE WHEN :sort = 'VIEWS'      THEN e.views      END NULLS LAST,
              e.id
            LIMIT :size OFFSET :offset
            """, nativeQuery = true)
    List<Event> getEventsPublicByCategories(
            @Param("textPattern") String textPattern,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("onlyAvailable") boolean onlyAvailable,
            @Param("sort") String sort,
            @Param("offset") int offset,
            @Param("size") int size
    );


    @Query(value = """
            SELECT e.*
            FROM events e
            WHERE
              (:textPattern IS NULL OR e.description ILIKE :textPattern
                 OR e.annotation ILIKE :textPattern
                 OR e.title      ILIKE :textPattern)
              AND e.state = 'PUBLISHED'
              AND e.published_on IS NOT NULL
              AND e.paid        = COALESCE(:paid,  e.paid)
              AND e.event_date >= :start
              AND e.event_date <= COALESCE(:end, e.event_date)
              AND (
                    NOT :onlyAvailable
                    OR e.participant_limit = 0
                    OR e.confirmed_requests < e.participant_limit
                  )
            ORDER BY
              CASE WHEN :sort = 'EVENT_DATE' THEN e.event_date END NULLS LAST,
              CASE WHEN :sort = 'VIEWS'      THEN e.views      END NULLS LAST,
              e.id
            LIMIT :size OFFSET :offset
                        
            """, nativeQuery = true)
    List<Event> getEventsPublicWithoutCategories(
            @Param("textPattern") String textPattern,
            @Param("paid") Boolean paid,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("onlyAvailable") boolean onlyAvailable,
            @Param("sort") String sort,
            @Param("offset") int offset,
            @Param("size") int size
    );


    @Query(value = """
            SELECT e.*
            FROM events e
            WHERE
              (:usersEmpty  OR e.initiator_id IN (:users))
              AND (:statesEmpty OR e.state IN (:states))
              AND (:catsEmpty   OR e.category_id IN (:categories))
              AND e.event_date >= COALESCE(:start, e.event_date)
              AND e.event_date <= COALESCE(:end,   e.event_date)
            ORDER BY e.id
            LIMIT :size OFFSET :offset
            """, nativeQuery = true)
    List<Event> findAdminEventsNative(
            @Param("usersEmpty") boolean usersEmpty,
            @Param("users") List<Long> users,
            @Param("statesEmpty") boolean statesEmpty,
            @Param("states") List<String> states,
            @Param("catsEmpty") boolean catsEmpty,
            @Param("categories") List<Long> categories,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("offset") int offset,
            @Param("size") int size
    );


    @Query(value = """
            SELECT * FROM events
            WHERE (initiator_id = :id)
            LIMIT :size OFFSET :from
            """, nativeQuery = true)
    List<Event> findEventsNative(
            @Param("id") Long id,
            @Param("from") Integer from,
            @Param("size") Integer size);


}
