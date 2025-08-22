package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.*;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.practicum.dto.participation.ParticipationStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl {

    private final EventStorage eventStorage;
    private final LocationStorage locationStorage;
    private final CategoryStorage categoryStorage;
    private final UserStorage userStorage;
    private final ParticipationStorage participationStorage;

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
    public EventFullDto adminUpdateEvent(UpdateEventAdminRequest dto, Long eventId) {
        Event event = eventStorage.getEventById(eventId);

        if (dto.getEventDate() != null) {
            LocalDateTime now = LocalDateTime.now();
            if (dto.getEventDate().isBefore(now.plusHours(1))) {
                throw new ConflictException("Дата начала события должна быть минимум за час до публикации");
            }
        }
        if (dto.getAdminStateAction() == AdminStateAction.PUBLISH_EVENT && event.getState() != State.PENDING) {
            throw new ConflictException("Опубликовать можно только событие в состоянии ожидания публикации");
        }
        if (dto.getAdminStateAction() == AdminStateAction.REJECT_EVENT && event.getState() == State.PUBLISHED) {
            throw new ConflictException("Нельзя отклонить уже опубликованное событие");
        }

        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getCategory() != null) {
            Category category = categoryStorage.getCategoryById(dto.getCategory());
            event.setCategory(category);
        }
        if (dto.getDescription() != null) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getLocation() != null) {
            Location location = LocationMapper.toEntity(dto.getLocation());
            event.setLocation(location);
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
        if (dto.getParticipantLimit() != null) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }
        if (dto.getAdminStateAction() != null) {
            switch (dto.getAdminStateAction()) {
                case PUBLISH_EVENT -> event.setState(State.PUBLISHED);
                case REJECT_EVENT -> event.setState(State.CANCELED);
            }
        }
        if (dto.getTitle() != null) {
            event.setTitle(dto.getTitle());
        }
        return EventMapper.toEventDto(event);
    }


    public List<EventFullDto> adminGetEvents(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            String rangeStart,
            String rangeEnd,
            int from,
            int size
    ) {
        boolean usersEmpty = users == null || users.isEmpty();
        boolean statesEmpty = states == null || states.isEmpty();
        boolean catsEmpty = categories == null || categories.isEmpty();

        LocalDateTime start = (rangeStart != null && !rangeStart.isBlank())
                ? LocalDateTime.parse(rangeStart)
                : null;

        LocalDateTime end = (rangeEnd != null && !rangeEnd.isBlank())
                ? LocalDateTime.parse(rangeEnd)
                : null;

        List<Event> events = eventStorage.getAdminEvents(usersEmpty, statesEmpty, catsEmpty, users, states, categories, start, end, from, size);
        return EventMapper.eventFullDtoList(events);
    }
}
