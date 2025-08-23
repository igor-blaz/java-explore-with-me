package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.State;
import ru.practicum.dto.participation.ParticipationRequestDto;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.mapper.ParticipationMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Participation;
import ru.practicum.storage.EventStorage;
import ru.practicum.storage.ParticipationStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParticipationServiceImpl {
    private final ParticipationStorage participationStorage;
    private final EventStorage eventStorage;


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


    }

    private void isRepeatedRequest(Long userId, Long eventId) {
        if (participationStorage.isExistsByUserIdAndEventId(userId, eventId)) {
            throw new ConflictException("Данный запрос на участие уже существует");
        }
    }

    private void isGreaterThanLimit(Event event) {
        long limit = event.getParticipantLimit();
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
