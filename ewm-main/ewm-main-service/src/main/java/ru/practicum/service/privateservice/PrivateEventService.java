package ru.practicum.service.privateservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClient;
import ru.practicum.dto.event.*;
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

import java.time.LocalDateTime;
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
        eventTimeCheck(newEventDto.getEventDate());
        Location location = LocationMapper.toEntity(newEventDto.getLocation());
        locationStorage.saveLocation(location);
        Category category = categoryStorage.getCategoryById(newEventDto.getCategory());
        User user = userStorage.getUserById(userId);

        Event event = eventStorage.addNewEvent(newEventDto, user, category, location);
        if (event.getState() == State.PUBLISHED) {
            event.setPublishedOn(LocalDateTime.now());
        }
        return EventMapper.toEventDto(event);
    }

    private void eventTimeCheck(LocalDateTime eventTime) {
        LocalDateTime now = LocalDateTime.now();
        if (eventTime.isBefore(now.plusHours(2))) {
            throw new ConflictException("Время события должно быть минимум через 2 часа от текущего момента");
        }

    }

    public EventFullDto getUserEventsByEventId(Long eventId, Long userId) {
        Event event = eventStorage.getUserEventsByEventId(eventId, userId);
        return EventMapper.toEventDto(event);
    }

    @Transactional
    public EventFullDto updateEventUserRequest(UpdateEventUserRequest dto, Long eventId, Long userId) {
        Event old = eventStorage.getUserEventsByEventId(eventId, userId);

        // 1) Всегда проверяем статус редактируемости
        pendingOrCancelledCheck(old.getState());

        // 2) Бизнес-правила по времени (часто требуется "не раньше чем через 2 часа")
        if (dto.getEventDate() != null) {
            eventTimeCheck(dto.getEventDate());
        }
//TODO пофиксить
        // 3) Частичные обновления
        if (dto.getAnnotation() != null) old.setAnnotation(dto.getAnnotation());
        if (dto.getDescription() != null) old.setDescription(dto.getDescription());
        if (dto.getEventDate() != null) old.setEventDate(dto.getEventDate());
        if (dto.getPaid() != null) old.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) old.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) old.setRequestModeration(dto.getRequestModeration());
        if (dto.getTitle() != null) old.setTitle(dto.getTitle());

        if (dto.getCategory() != null) {
            Category cat = categoryStorage.getCategoryById(dto.getCategory());
            old.setCategory(cat);
        }

        if (dto.getLocation() != null) {
            Location saved = locationStorage.saveLocation(LocationMapper.toEntity(dto.getLocation()));
            old.setLocation(saved);
        }

        // 4) Обработка команд состояния
        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case SEND_TO_REVIEW -> old.setState(State.PENDING);
                case CANCEL_REVIEW -> old.setState(State.CANCELLED);
            }
        }

        return EventMapper.toEventDto(old);
    }

    private void pendingOrCancelledCheck(State state) {
        if (state == null || (state != State.PENDING && state != State.CANCELLED)) {
            throw new ConflictException(
                    "изменить можно только отмененные события или события в состоянии ожидания модерации"
            );
        }
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
        if (!event.getRequestModeration() || limit == 0) {
            throw new ConflictException("Подтверждение заявок не требуется." +
                    " Нет лимита или нет модерации");
        }
        if (limit > 0 && alreadyConfirmedCount >= limit) {
            throw new ConflictException("Достигнут лимит участников для события");
        }
        List<Participation> pendindParticipations = participationStorage.findParticipationsNotIn(event,
                PENDING, participations);
        if (eventRequestStatusUpdateRequest.getStatus() == ParticipationStatusForUpdate.REJECTED) {
            return makeAllRejectedResponse(participations, event);
        } else if (eventRequestStatusUpdateRequest.getStatus() == ParticipationStatusForUpdate.CONFIRMED) {
            return makeConfirmedOrRejected(participations, freeSpaces, pendindParticipations, event);
        } else {
            throw new ConflictException("Невозможно поменять статус заявки на " + eventRequestStatusUpdateRequest.getStatus());
        }


    }

    private EventRequestStatusUpdateResult makeConfirmedOrRejected(List<Participation> participations, int free,
                                                                   List<Participation> pendingParticipations,
                                                                   Event event) {
        List<Participation> rejectedParticipation = new ArrayList<>();
        List<Participation> confirmedParticipation = new ArrayList<>();
        for (Participation participation : participations) {
            if (free > 0) {
                participation.setStatus(CONFIRMED);
                confirmedParticipation.add(participation);
                free--;
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            } else {
                participation.setStatus(REJECTED);
                rejectedParticipation.add(participation);
            }
        }
        if (free == 0 && !pendingParticipations.isEmpty()) {
            pendingParticipations.forEach(p -> p.setStatus(REJECTED));
            rejectedParticipation.addAll(pendingParticipations);
            participationStorage.saveAll(pendingParticipations);
        }
        eventStorage.save(event);
        participationStorage.saveAll(participations);

        return EventRequestStatusUpdateResult.builder()
                .rejectedRequests(ParticipationMapper.toDtoList(rejectedParticipation))
                .confirmedRequests(ParticipationMapper.toDtoList(confirmedParticipation))
                .build();
    }


    private EventRequestStatusUpdateResult makeAllRejectedResponse(List<Participation> participations, Event event) {
        participations.forEach(p -> p.setStatus(REJECTED));
        event.setConfirmedRequests(0);
        eventStorage.save(event);
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
