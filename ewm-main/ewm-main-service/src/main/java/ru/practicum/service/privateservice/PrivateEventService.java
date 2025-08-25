package ru.practicum.service.privateservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClient;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.participation.EventRequestStatusUpdateRequest;
import ru.practicum.dto.participation.EventRequestStatusUpdateResult;
import ru.practicum.dto.participation.ParticipationRequestDto;
import ru.practicum.dto.participation.ParticipationStatusForUpdate;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.mapper.ParticipationMapper;
import ru.practicum.model.*;
import ru.practicum.storage.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.practicum.dto.participation.ParticipationStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivateEventService {
    private final EventStorage eventStorage;
    private final LocationStorage locationStorage;
    private final CategoryStorage categoryStorage;
    private final UserStorage userStorage;
    private final ParticipationStorage participationStorage;
    private final StatsClient statsClient;

    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {
        List<Event> events = eventStorage.getUserEvents(userId, from, size);
        return EventMapper.eventShortDtoList(events);
    }

    public List<ParticipationRequestDto> getUserRequests(Long userId, Long eventId) {
        List<Participation> participations = participationStorage.getParticipationByUserIdAndEventId(userId, eventId);
        return ParticipationMapper.toDtoList(participations);
    }

    public EventFullDto addNewEventDto(Long userId, NewEventDto newEventDto
    ) {
        //TODO; Сделать валидацию по времени event
        Location location = LocationMapper.toEntity(newEventDto.getLocationDto());
        locationStorage.saveLocation(location);
        Category category = categoryStorage.getCategoryById(newEventDto.getCategory());
        User user = userStorage.getUserById(userId);

        Event event = eventStorage.addNewEvent(newEventDto, user, category, location);
        return EventMapper.toEventDto(event);
    }

    public EventFullDto getUserEventsByEventId(Long eventId, Long userId) {
        Event event = eventStorage.getUserEventsByEventId(eventId, userId);
        return EventMapper.toEventDto(event);
    }

    @Transactional
    public EventFullDto updateEventUserRequest(
            UpdateEventUserRequest updateEventUserRequest, Long oldEventId, Long userId) {
        Category category;
        if (updateEventUserRequest.getCategory() != null) {
            category = categoryStorage.getCategoryById(updateEventUserRequest.getCategory());
        } else {
            category = null;
        }
        Location location;
        if (updateEventUserRequest.getLocation() != null) {
            LocationDto locationDto = updateEventUserRequest.getLocation();

            location = locationStorage.saveLocation(LocationMapper.toEntity(locationDto));
        } else {
            location = null;
        }

        Event oldEvent = eventStorage.getUserEventsByEventId(oldEventId, userId);
        Event event = eventStorage.updateEvent(updateEventUserRequest, oldEvent, category, location);
        return EventMapper.toEventDto(event);
    }

    @Transactional
    public EventRequestStatusUpdateResult updateRequestStatus(
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest, Long userId, Long eventId
    ) {
        Event event = eventStorage.getUserEventsByEventId(userId, eventId);
        isRequestModerationValidation(event);


        List<Long> participationIds = eventRequestStatusUpdateRequest.getRequestIds();
        List<Participation> participations =
                participationStorage.getParticipationsForStatusUpdate(eventId, participationIds);
        isAllPendingStatusValidation(participations);

        int limit = event.getParticipantLimit();
        int alreadyConfirmedCount = participationStorage.countByEventAndStatus(eventId, CONFIRMED);
        int freeSpaces = limit - alreadyConfirmedCount;

        if (limit > 0 && alreadyConfirmedCount >= limit) {
            throw new ConflictException("Достигнут лимит участников для события");
        }

        if (eventRequestStatusUpdateRequest.getStatus() == ParticipationStatusForUpdate.REJECTED) {
            return makeAllRejectedResponse(participations);
        } else if (eventRequestStatusUpdateRequest.getStatus() == ParticipationStatusForUpdate.CONFIRMED) {
            return makeConfirmedOrRejected(participations, freeSpaces);
        } else {
            throw new ConflictException("Невозможно поменять статус заявки на " + eventRequestStatusUpdateRequest.getStatus());
        }


    }

    private EventRequestStatusUpdateResult makeConfirmedOrRejected(List<Participation> participations, int limit) {
        List<Participation> rejectedParticipation = new ArrayList<>();
        List<Participation> confirmedParticipation = new ArrayList<>();
        for (Participation participation : participations) {
            if (limit > 0) {
                participation.setStatus(CONFIRMED);
                confirmedParticipation.add(participation);
                limit--;
            } else {
                participation.setStatus(REJECTED);
                rejectedParticipation.add(participation);
            }
        }
        return EventRequestStatusUpdateResult.builder()
                .rejectedRequests(ParticipationMapper.toDtoList(rejectedParticipation))
                .confirmedRequests(ParticipationMapper.toDtoList(confirmedParticipation))
                .build();
    }

    private EventRequestStatusUpdateResult makeAllRejectedResponse(List<Participation> participations) {
        participations.forEach(p -> p.setStatus(REJECTED));
        return EventRequestStatusUpdateResult.builder()
                .rejectedRequests(ParticipationMapper.toDtoList(participations))
                .confirmedRequests(Collections.emptyList())
                .build();
    }

    public void isAllPendingStatusValidation(List<Participation> participationList) {
        boolean notPendingStatus = participationList.stream().anyMatch(r -> r.getStatus() != PENDING);
        if (notPendingStatus) {
            throw new ConflictException("Обновить можно только запросы со статусом PENDING");
        }

    }

    public void isRequestModerationValidation(Event event) {
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new ConflictException("Подтверждение заявок не требуется");
        }
    }

}
