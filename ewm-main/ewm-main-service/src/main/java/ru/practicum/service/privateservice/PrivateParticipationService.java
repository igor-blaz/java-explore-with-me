package ru.practicum.service.privateservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.State;
import ru.practicum.dto.participation.ParticipationRequestDto;
import ru.practicum.dto.participation.ParticipationStatus;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.mapper.ParticipationMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Participation;
import ru.practicum.storage.EventStorage;
import ru.practicum.storage.ParticipationStorage;
import ru.practicum.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivateParticipationService {

    private final ParticipationStorage participationStorage;
    private final EventStorage eventStorage;
    private final UserStorage userStorage;

    @Transactional
    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {
        Participation participation = participationStorage.getParticipationByUserIdAndRequestId(userId, requestId);
        if (participation.getStatus() == ParticipationStatus.CANCELED) {
            return ParticipationMapper.toDto(participation);
        }
        if (participation.getStatus() == ParticipationStatus.REJECTED) {
            throw new ConflictException("Заявка уже отклонена");
        }
        if (participation.getStatus() == ParticipationStatus.CONFIRMED) {
            Event event = participation.getEvent();
            long confirmedRequests = event.getConfirmedRequests();
            if (confirmedRequests > 0) {
                event.setConfirmedRequests(confirmedRequests - 1);
            }

        }
        participation.setStatus(ParticipationStatus.CANCELED);
        return ParticipationMapper.toDto(participation);

    }

    public ParticipationRequestDto addUserRequest(Long userId, Long eventId) {
        isRepeatedRequest(userId, eventId);
        Event event = eventStorage.getEventById(eventId);
        isGreaterThanLimit(event);
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Нельзя участвовать в том событии, которые вы сами создали");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Данное событие не опубликовано. В нем нельзя участвовать");
        }
        ParticipationStatus status = event.getRequestModeration()
                ? ParticipationStatus.PENDING
                : ParticipationStatus.CONFIRMED;

        Participation participation = Participation.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(userStorage.getUserById(userId))
                .status(status)
                .build();


        Participation saved = participationStorage.addParticipation(participation);


        return ParticipationMapper.toDto(saved);


    }

    private void isRepeatedRequest(Long userId, Long eventId) {
        if (participationStorage.isExistsByUserIdAndEventId(userId, eventId)) {
            throw new ConflictException("Данный запрос на участие уже существует");
        }
    }

    private void isGreaterThanLimit(Event event) {
        long limit = event.getParticipantLimit();
        if (limit <= 0) return;
        long participantsCount = participationStorage.countByEventId(event.getId());
        if (participantsCount >= limit) {
            throw new ConflictException("У события достигнут лимит");
        }
    }


    public List<ParticipationRequestDto> getUserRequestDto(Long userId) {
        List<Participation> participations = participationStorage.getParticipationsByUserId(userId);
        return ParticipationMapper.toDtoList(participations);
    }
}
