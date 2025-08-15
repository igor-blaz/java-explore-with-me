package ru.practicum.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.participation.ParticipationStatus;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.model.Participation;
import ru.practicum.repository.ParticipationRepository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ParticipationStorage {
    private final ParticipationRepository participationRepository;

    public Participation getParticipationById(Long id) {
        return participationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("запрос (participationn) с id: " + id + " не найден"));
    }

    public List<Participation> getParticipationByUserIdAndEventId(Long userId, Long eventId) {
        return participationRepository.findAllByRequester_IdAndEvent_Id(userId, eventId);
    }

    public List<Participation> getParticipationsForStatusUpdate(Long eventId, List<Long> participationIds) {
        return participationRepository.findAllForUpdate(eventId, participationIds);
    }
    public int countByEventAndStatus(Long eventId, ParticipationStatus participationStatus){
        return participationRepository.countByEvent_IdAndStatus(eventId, participationStatus);
    }
}
