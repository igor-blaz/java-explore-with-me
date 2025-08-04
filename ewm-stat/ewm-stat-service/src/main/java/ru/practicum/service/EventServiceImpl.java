package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.storage.EventStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl {

    private final EventStorage eventStorage;

    public List<EventShortDto> getUserEvents(Long userId,Integer from, Integer size){
return null;
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
