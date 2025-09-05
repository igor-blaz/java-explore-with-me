package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.participation.ParticipationStatus;
import ru.practicum.model.Event;
import ru.practicum.model.Participation;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    List<Participation> findAllByRequester_IdAndEvent_Id(Long requesterId, Long eventId);

    @Query("""
             select p from Participation p
             join fetch p.event
             join fetch p.requester
             where p.event.id = :eventId and p.id in :ids
            """)
    List<Participation> findAllForUpdate(@Param("eventId") Long eventId, @Param("ids") List<Long> ids);

    boolean existsByEventIdAndIdInAndStatus(Long eventId, List<Long> ids, ParticipationStatus status);


    List<Participation> findAllByEvent_Initiator_IdAndEvent_Id(Long initiatorId, Long eventId);


    long countByEventId(Long eventId);

    List<Participation> findAllByRequesterId(Long userId);

    int countByEvent_IdAndStatus(Long eventId, ParticipationStatus status);

    boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    Optional<Participation> findByIdAndRequesterId(Long id, Long requesterId);

    @Query("""
                SELECT p
                FROM Participation p
                WHERE p.event = :event
                  AND p.status = :status
                  AND p NOT IN :excluded
            """)
    List<Participation> findPendingNotIn(
            @Param("event") Event event,
            @Param("status") ParticipationStatus status,
            @Param("excluded") List<Participation> excluded
    );

}
