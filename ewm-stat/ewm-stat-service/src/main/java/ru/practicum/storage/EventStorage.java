package ru.practicum.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.User;
import ru.practicum.repository.EventRepository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EventStorage {
    private final EventRepository eventRepository;

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

    public Event getUserEventsByEventId(Long eventId, Long userId) {
        return eventRepository.findEventByIdAndInitiatorId(eventId, userId);
    }
}
