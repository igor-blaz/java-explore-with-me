package ru.practicum.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.State;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.User;
import ru.practicum.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EventStorage {
    private final EventRepository eventRepository;

    public Event getPublicEventById(Long id) {
        return eventRepository.findPublishedById(id)
                .orElseThrow(() -> new NotFoundException("Не найдено"));
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> getEventsPublicByCategories(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            boolean onlyAvailable,
            String sort,
            int from,
            int size
    ) {
        return eventRepository.getEventsPublicByCategories(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
    }

    public List<Event> getEventsPublicWithoutCategories(
            String text,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            boolean onlyAvailable,
            String sort,
            int from,
            int size
    ) {
        return eventRepository.getEventsPublicWithoutCategories(text, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    public List<Event> getEventsByIds(List<Long> ids) {
        return eventRepository.findAllById(ids);
    }

    public List<Event> getAdminEvents(boolean usersEmpty,
                                      boolean statesEmpty,
                                      boolean categoriesEmpty,

                                      List<Long> users,
                                      List<String> states,
                                      List<Long> categories,
                                      LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd,
                                      int from,
                                      int size) {
        return eventRepository.findAdminEventsNative(usersEmpty, users, statesEmpty, states, categoriesEmpty,
                categories, rangeStart, rangeEnd, from, size);

    }

    public boolean isHasCategory(Category category) {
        return eventRepository.existsAllByCategory(category);
    }

    public List<Event> getUserEvents(Long userId, Integer from, Integer size) {
        return eventRepository.findEventsNative(userId, from, size);
    }

    public Event addNewEvent(NewEventDto newEventDto,
                             User user, Category category, Location location) {
        return eventRepository.save(EventMapper.toEntity(newEventDto, user, category, location));
    }

    public Event getEventPublishedByUserId(Long eventId, Long userId) {
        return eventRepository.findPublishedByIdAndInitiatorId(eventId, userId).
                orElseThrow(() -> new NotFoundException("объект не найден"));
    }


    public Event getEventByUserId(Long eventId, Long userId) {
        return eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException(""));
    }

    @Transactional
    public Event updateEvent(UpdateEventUserRequest dto, Event event, Category category, Location location) {
        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (category != null) {
            event.setCategory(category);
        }
        if (dto.getDescription() != null) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (location != null) {
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
        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case CANCEL_REVIEW -> event.setState(State.CANCELED);
                case SEND_TO_REVIEW -> event.setState(State.PENDING);
            }

        }
        if (dto.getTitle() != null) {
            event.setTitle(dto.getTitle());
        }

        return event;
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new NotFoundException("События id " + id + " не существует"));
    }


}
