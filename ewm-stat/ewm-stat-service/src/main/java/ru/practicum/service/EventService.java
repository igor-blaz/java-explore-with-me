package ru.practicum.service;

import org.springframework.stereotype.Service;
import ru.practicum.dto.event.EventFullDto;

import java.util.List;

@Service
public class EventService {

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
