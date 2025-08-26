package ru.practicum.service.adminservice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsClient;
import ru.practicum.dto.event.AdminStateAction;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.State;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.storage.CategoryStorage;
import ru.practicum.storage.EventStorage;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminEventService {
    private final EventStorage eventStorage;
    private final CategoryStorage categoryStorage;
    private final StatsClient statsClient;


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
            int size,
            HttpServletRequest request
    ) {
        EndpointHitDto hitDto = new EndpointHitDto(
                null,
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()
        );
        statsClient.saveHit(hitDto);
        boolean usersEmpty = users == null || users.isEmpty();
        boolean statesEmpty = states == null || states.isEmpty();
        boolean catsEmpty = categories == null || categories.isEmpty();

        LocalDateTime start = (rangeStart != null && !rangeStart.isBlank())
                ? LocalDateTime.parse(rangeStart)
                : null;

        LocalDateTime end = (rangeEnd != null && !rangeEnd.isBlank())
                ? LocalDateTime.parse(rangeEnd)
                : null;

        List<Event> events = eventStorage.getAdminEvents(usersEmpty, statesEmpty, catsEmpty, users, states,
                categories, start, end, from, size);
        return EventMapper.eventFullDtoList(events);
    }
}
