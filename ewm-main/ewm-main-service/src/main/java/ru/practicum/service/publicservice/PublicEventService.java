package ru.practicum.service.publicservice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsClient;
import ru.practicum.controller.publiccontroller.SortType;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.StringIlikeSqlPattern;
import ru.practicum.model.Event;
import ru.practicum.storage.EventStorage;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicEventService {
    private final EventStorage eventStorage;
    private final StatsClient statsClient;

    public EventFullDto getEventByIdPublic(Long id) {
        Event event = eventStorage.getEventById(id);
        return EventMapper.toEventDto(event);
    }

    public List<EventShortDto> getEventsPublic(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            boolean onlyAvailable,
            SortType sort,
            int from,
            int size,
            HttpServletRequest request
    ) {
        EndpointHitDto hitDto = new EndpointHitDto(
                null,
                "ewm-main-service",
                "/events",
                request.getRemoteAddr(),
                LocalDateTime.now()
        );
        statsClient.saveHit(hitDto);
        String textPattern = StringIlikeSqlPattern.makeIlikePattern(text);
        boolean categoriesIsEmpty = categories == null || categories.isEmpty();
        List<Event> events;
        String sortType = null;
        LocalDateTime now = null;
        if (rangeStart == null && rangeEnd == null) {
            now = LocalDateTime.now();
        }
        if (sort != null) {
            sortType = sort.name();
        }


        if (categoriesIsEmpty) {
            events = eventStorage.getEventsPublicWithoutCategories(textPattern,
                    paid, rangeStart, rangeEnd, now, onlyAvailable, sortType,
                    from, size);
        } else {
            events = eventStorage.getEventsPublicByCategories(textPattern, categories,
                    paid, rangeStart, rangeEnd, now, onlyAvailable, sortType,
                    from, size);
        }


        return EventMapper.eventShortDtoList(events);
    }
}
