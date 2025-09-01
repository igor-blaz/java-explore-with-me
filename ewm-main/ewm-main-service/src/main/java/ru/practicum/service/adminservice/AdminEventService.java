package ru.practicum.service.adminservice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.State;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.model.Event;
import ru.practicum.service.StatsConnector;
import ru.practicum.storage.CategoryStorage;
import ru.practicum.storage.EventStorage;
import ru.practicum.storage.ParticipationStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminEventService {
    private final EventStorage eventStorage;
    private final CategoryStorage categoryStorage;
    private final StatsConnector statsConnector;
    private final ParticipationStorage participationStorage;


    @Transactional
    public EventFullDto adminUpdateEvent(UpdateEventAdminRequest dto, Long eventId) {
        Event event = eventStorage.getEventById(eventId);

        log.debug("EVENT {} ", event);
        log.debug("UpdateRequest {}", dto);

        if (dto.getAnnotation() != null) event.setAnnotation(dto.getAnnotation());
        if (dto.getCategory() != null) event.setCategory(categoryStorage.getCategoryById(dto.getCategory()));
        if (dto.getDescription() != null) event.setDescription(dto.getDescription());
        if (dto.getEventDate() != null) event.setEventDate(dto.getEventDate());
        if (dto.getLocation() != null) event.setLocation(LocationMapper.toEntity(dto.getLocation()));
        if (dto.getPaid() != null) event.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) event.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) event.setRequestModeration(dto.getRequestModeration());
        if (dto.getTitle() != null) event.setTitle(dto.getTitle());
        if (dto.getEventDate() != null) {
            if (dto.getEventDate().isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Дата начала события не может быть в прошлом");
            }
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case PUBLISH_EVENT -> {
                    if (event.getState() != State.PENDING) {
                        throw new ConflictException("Опубликовать можно только событие в состоянии ожидания публикации");
                    }
                    LocalDateTime effectiveDate = event.getEventDate();
                    log.debug("Дата {}", effectiveDate);
                    if (effectiveDate == null || !effectiveDate.isAfter(LocalDateTime.now().plusHours(1))) {
                        throw new ConflictException("Дата начала события должна быть минимум через час от момента публикации");
                    }
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    log.info("Событие опубликовано");
                }
                case REJECT_EVENT -> {
                    if (event.getState() == State.PUBLISHED) {
                        throw new ConflictException("Нельзя отклонить уже опубликованное событие");
                    }
                    event.setState(State.CANCELED);
                }

            }
        }
        eventStorage.save(event);
        return EventMapper.toEventDto(event);
    }


    public List<EventFullDto> adminGetEvents(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            int from,
            int size,
            HttpServletRequest request
    ) {

        boolean usersEmpty = users == null || users.isEmpty() || users.get(0) == 0;
        boolean statesEmpty = states == null || states.isEmpty();
        boolean catsEmpty = categories == null || categories.isEmpty() || categories.get(0) == 0;


        List<Event> events = eventStorage.getAdminEvents(usersEmpty, statesEmpty, catsEmpty, users, states,
                categories, rangeStart, rangeEnd, from, size);
        List<Event> eventsWithRequests = new ArrayList<>();
        for (Event event : events) {
            event.setConfirmedRequests(participationStorage.countByEventId(event.getId()));
            eventsWithRequests.add(event);
        }
        return statsConnector.getViewsForEvents(eventsWithRequests, false);
    }


}
