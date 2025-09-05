package ru.practicum.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.participation.ParticipationStatus;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.model.Event;
import ru.practicum.model.Participation;
import ru.practicum.repository.ParticipationRepository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ParticipationStorage {
    private final ParticipationRepository participationRepository;

    public Participation getParticipationByUserIdAndRequestId(Long userId, Long requestId) {
        return participationRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request with id= " + requestId + " was not found"));
    }

    public boolean existsByIdAndAndStatus(Long eventId, List<Long> ids, ParticipationStatus status) {
        return participationRepository.existsByEventIdAndIdInAndStatus(eventId, ids, status);

    }

    public Participation addParticipation(Participation participation) {
        return participationRepository.save(participation);
    }

    public List<Participation> getParticipationsByUserId(Long userId) {
        return participationRepository.findAllByRequesterId(userId);
    }

    public long countByEventId(Long eventId) {
        return participationRepository.countByEventId(eventId);
    }

    public boolean isExistsByUserIdAndEventId(Long userId, Long eventId) {
        return participationRepository.existsByRequesterIdAndEventId(userId, eventId);
    }

    public Participation getParticipationById(Long id) {
        return participationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("запрос (participation) с id: " + id + " не найден"));
    }

    public List<Participation> getParticipationByUserIdAndEventId(Long userId, Long eventId) {
        return participationRepository.findAllByEvent_Initiator_IdAndEvent_Id(userId, eventId);
    }

    public List<Participation> getParticipationsForStatusUpdate(Long eventId, List<Long> participationIds) {
        return participationRepository.findAllForUpdate(eventId, participationIds);
    }

    public int countByEventAndStatus(Long eventId, ParticipationStatus participationStatus) {
        return participationRepository.countByEvent_IdAndStatus(eventId, participationStatus);
    }

    public List<Participation> saveAll(List<Participation> participations) {
        return participationRepository.saveAll(participations);
    }

    public List<Participation> findParticipationsNotIn(Event event, ParticipationStatus status, List<Participation> participations) {
        return participationRepository.findPendingNotIn(event, status, participations);
    }
}
