package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.EventFullDto;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl {

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
