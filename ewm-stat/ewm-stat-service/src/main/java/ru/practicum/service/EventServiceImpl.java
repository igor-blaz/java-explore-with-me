package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.User;
import ru.practicum.storage.CategoryStorage;
import ru.practicum.storage.EventStorage;
import ru.practicum.storage.LocationStorage;
import ru.practicum.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl {

    private final EventStorage eventStorage;
    private final LocationStorage locationStorage;
    private final CategoryStorage categoryStorage;
    private final UserStorage userStorage;

    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {
        List<Event> events = eventStorage.getUserEvents(userId, from, size);
        return EventMapper.eventShortDtoList(events);
    }

    public EventFullDto addNewEventDto(Long userId, NewEventDto newEventDto
    ) {
        //TODO; Сделать валидацию по времени event
        Location location = LocationMapper.toEntity(newEventDto.getLocationDto());
        Category category = categoryStorage.getCategory(newEventDto.getCategory());
        User user = userStorage.getUserById(userId);

        Event event = eventStorage.addNewEvent(newEventDto, user, category, location);
        return EventMapper.toEventDto(event);
    }

    public EventFullDto getUserEventsByEventId(Long eventId, Long userId) {
        Event event = eventStorage.getUserEventsByEventId(eventId, userId);
        return EventMapper.toEventDto(event);
    }

    public UpdateEventUserRequest updateEventUserRequest(
            UpdateEventUserRequest updateEventUserRequest, Long eventId, Long userId){


    }






    public EventFullDto adminUpdateEvent(EventFullDto eventFullDto, Long eventId) {
        return null;
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
        return null;
    }
}
